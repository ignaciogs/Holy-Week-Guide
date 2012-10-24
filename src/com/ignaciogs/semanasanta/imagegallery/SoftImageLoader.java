/*
 *
 *  * Copyright (C) 2012  Ignacio Gonzalez Sainz
 *  *
 *  * Holy Week Guide: An android application with the itineraries, images and all information of the brotherhoods Spain's holy week
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  * Ignacio Gonzalez Sainz
 *  * Cádiz (Spain)
 *  * ignacio.glez.s@gmail.com
 *  *
 *
 */

package com.ignaciogs.semanasanta.imagegallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.*;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

public class SoftImageLoader {
    public class ImageHolder{
    	public ImageView img;
    	public ProgressBar progress;
    }

    //the simplest in-memory cache implementation. This should be replaced with something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
    private HashMap<String, SoftReference<Bitmap>> cache=new HashMap<String, SoftReference<Bitmap>>();

    private File cacheDir;
    private int stub_id;;
    private int REQUIRED_SIZE=70;
    private static final int CACHE_QUALITY=40;
    private int numThreads=50;
    //PhotosLoader photoLoaderThread=new PhotosLoader();
    PhotosLoader[] photoLoaderThread;
    public SoftImageLoader(Context context, int imageSize, int loadingResource){
        REQUIRED_SIZE=imageSize;    	
    	stub_id=loadingResource;
        //Make the background thead low priority. This way it will not affect the UI performance
    	photoLoaderThread= new PhotosLoader[numThreads];
        for (int i=0; i<numThreads; i++) {
        	photoLoaderThread[i] = new PhotosLoader();
        	photoLoaderThread[i].setPriority(Thread.NORM_PRIORITY-1);
        	photoLoaderThread[i].start();
        }        
    	//photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
        
        //Find the dir to save cached images
        /*if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"LazyList");
        else
            cacheDir=context.getCacheDir();*/
        cacheDir=context.getCacheDir();
        //if(!cacheDir.exists())
            //cacheDir.mkdirs();
    }
    
    public void DisplayImage(String url, Activity activity, ImageHolder h)
    {
        if(cache.containsKey(url) && cache.get(url).get()!=null){
            h.img.setImageBitmap(cache.get(url).get());
	    	h.img.setVisibility(View.VISIBLE);
	    	h.progress.setVisibility(View.GONE);
        }else
        {
            Bitmap b=getDiskBitmap(url);
            if(b==null){
	        	queuePhoto(url, activity, h);
	            h.img.setImageResource(stub_id);
	        	h.img.setVisibility(View.GONE);
	        	h.progress.setVisibility(View.VISIBLE);
            }else{
            	h.img.setImageBitmap(b);
	        	h.img.setVisibility(View.VISIBLE);
	        	h.progress.setVisibility(View.GONE);
            }
        }    
    }
        
    private void queuePhoto(String url, Activity activity, ImageHolder h)
    {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them. 
        photosQueue.Clean(h);
        PhotoToLoad p=new PhotoToLoad(url, h);
        synchronized(photosQueue.photosToLoad){
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }
        
        //start thread if it's not started yet
        //if(photoLoaderThread.getState()==Thread.State.NEW)
            //photoLoaderThread.start();
    }
    private Bitmap getDiskBitmap(String url){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(Math.abs(url.hashCode()));
        File f=new File(cacheDir, filename);
        if(f.exists()){
	        //from SD cache
	        Bitmap b = decodeFile(f);
	        if(b!=null)
	            return b;
    	}
        return null;
    }
    private Bitmap getBitmap(String url) 
    {      
        //from web
        String filename=String.valueOf(Math.abs(url.hashCode()));
        File f=new File(cacheDir, filename);
        try {
            Bitmap bitmap = cacheStream(url,f);
            return bitmap;
        } catch (Exception ex){
           ex.printStackTrace();
           return null;
        }
    }
    private Bitmap cacheStream(String url, File filetoCache){
        try {
            InputStream is=fetch(url);
            FileOutputStream fo=new FileOutputStream(filetoCache);
            CopyStream(is, fo);
            return decodeFile(filetoCache);
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }
    
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
 
    private Bitmap decodeAndCacheStream(String url, File filetoCache){
        try {
            InputStream is=fetch(url);
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is,null,o);
            
            //Find the correct scale value. It should be the power of 2.
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //decode with inSampleSize
            is=fetch(url);
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            Bitmap b= BitmapFactory.decodeStream(is, null, o2);
            if(b!=null){
	            FileOutputStream fo=new FileOutputStream(filetoCache);
	            if(!b.compress(Bitmap.CompressFormat.JPEG, CACHE_QUALITY, fo)){
	            	//caching to disk failed, delete the file
	            	filetoCache.delete();
	            }
	            fo.close();
            }
            return b;
        } catch (Exception e) {e.printStackTrace();}
        return null;
    }
    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            /*BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
            //Find the correct scale value. It should be the power of 2.
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }*/
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=2;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {e.printStackTrace();}
        return null;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageHolder holder;
        public PhotoToLoad(String u, ImageHolder h){
            url=u; 
            holder=h;
        }
    }
    
    PhotosQueue photosQueue=new PhotosQueue();
    
    public void stopThread()
    {
        for(int i=0;i<numThreads;i++){
        	photoLoaderThread[i].interrupt();
        }
    }
    
    //stores list of photos to download
    class PhotosQueue
    {
        private Stack<PhotoToLoad> photosToLoad=new Stack<PhotoToLoad>();
        
        //removes all instances of this ImageView
        public void Clean(ImageHolder h)
        {
            for(int j=0 ;j<photosToLoad.size();){
                if(photosToLoad.get(j).holder==h)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }
    
    class PhotosLoader extends Thread {
        public void run() {
            try {
                while(true)
                {
                    //thread waits until there are any images to load in the queue
                    if(photosQueue.photosToLoad.size()==0)
                        synchronized(photosQueue.photosToLoad){
                            photosQueue.photosToLoad.wait();
                        }
                    if(photosQueue.photosToLoad.size()!=0)
                    {
                        PhotoToLoad photoToLoad;
                        synchronized(photosQueue.photosToLoad){
                            photoToLoad=photosQueue.photosToLoad.pop();
                        }
                        Bitmap bmp=getBitmap(photoToLoad.url);
                        cache.put(photoToLoad.url, new SoftReference<Bitmap>(bmp));
                        Object tag=photoToLoad.holder.img.getTag();
                        if(tag!=null && ((String)tag).equals(photoToLoad.url)){
                            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad.holder);
                            Activity a=(Activity)photoToLoad.holder.img.getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (Exception e) {
                //allow thread to exit
            	e.printStackTrace();
            }
        }
    }
    
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        ImageHolder holder;
        public BitmapDisplayer(Bitmap b, ImageHolder h){bitmap=b;holder=h;}
        public void run()
        {
            if(bitmap!=null){
                holder.img.setImageBitmap(bitmap);
                holder.img.setVisibility(View.VISIBLE);
                holder.progress.setVisibility(View.GONE);
            }else{
                //imageView.setImageResource(stub_id);
            	holder.img.setVisibility(View.GONE);
            	holder.progress.setVisibility(View.VISIBLE);
            }
        }
    }

    public void clearCache() {
        //clear memory cache
        cache.clear();
        
        //clear SD cache
        File[] files=cacheDir.listFiles();
        for(File f:files)
            f.delete();
    }
    private InputStream fetch(String urlString) throws MalformedURLException, IOException {
		URL myFileUrl = new URL(urlString);
		HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
		//conn.setDoInput(true);
		conn.connect();
		return new FlushedInputStream(conn.getInputStream());
    	
    	/*
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	HttpGet request = new HttpGet(urlString);
    	HttpResponse response = httpClient.execute(request);
    	return response.getEntity().getContent();
    	*/
    }
    
    private class FlushedInputStream extends FilterInputStream {
	    public FlushedInputStream(InputStream inputStream) {
	        super(inputStream);
	    }

	    @Override
	    public long skip(long n) throws IOException {
	        long totalBytesSkipped = 0L;
	        while (totalBytesSkipped < n) {
	            long bytesSkipped = in.skip(n - totalBytesSkipped);
	            if (bytesSkipped == 0L) {
	                  int _byte = read();
	                  if (_byte < 0) {
	                      break;  // we reached EOF
	                  } else {
	                      bytesSkipped = 1; // we read one byte
	                  }
	           }
	            totalBytesSkipped += bytesSkipped;
	        }
	        return totalBytesSkipped;
	    }
	}
}
