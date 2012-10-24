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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class BitmapManager {
	
	public int inSampleSize = 1;
	private int width = 320;
	private HashMap<String, Thread> mPool=new HashMap<String, Thread>();
	public void setWidth(int pWidth) {
		width = pWidth;
	}

    public BitmapManager() {
    }

    public Bitmap fetchDrawable(String urlString) {
    	Log.d(this.getClass().getSimpleName(), "image url:" + urlString);
    	try {
    		InputStream is = fetch(urlString);

			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
            try{
            	BitmapFactory.decodeStream(is, null, opts);
            }
            catch(Exception e)
            {
            	System.gc();
            	BitmapFactory.decodeStream(is, null, opts);
            }
            
            // now opts.outWidth and opts.outHeight are the dimension of the
            // bitmap, even though bm is null
            is = fetch(urlString);
            opts.inJustDecodeBounds = false;    // this will request the bm
                                   
            opts.inSampleSize = opts.outWidth/width;
            
            try{
            	return BitmapFactory.decodeStream(is, null, opts);
            }catch(Exception e){
            	System.gc();
            	return BitmapFactory.decodeStream(is, null, opts);
            }
           
    	} catch (MalformedURLException e) {
    		Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
    		return null;
    	} catch (IOException e) {
    		Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
    		return null;
    	}
    }

    public void fetchDrawableOnThread(final String urlString, final ImageView imageView, final ProgressBar progressBar) {
    	final Handler handler = new Handler() {
    		@Override
    		public void handleMessage(Message message) {
    			imageView.setImageBitmap((Bitmap) message.obj);
    			if (progressBar != null) progressBar.setVisibility(View.GONE);
    		}
    	};
    	if(mPool.get(urlString)==null){
	    	Thread thread = new Thread() {
	    		@Override
	    		public void run() {
	    			Bitmap drawable = fetchDrawable(urlString);
	    			Message message = handler.obtainMessage(1, drawable);
	    			handler.sendMessage(message);
	    			mPool.remove(urlString);
	    		}
	    	};
	    	thread.start();
	    	mPool.put(urlString, thread);
    	}
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