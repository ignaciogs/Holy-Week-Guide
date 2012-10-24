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

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.ignaciogs.semanasanta.R;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ImageGalleryViewAdapter extends BaseAdapter {
	
	Context gContext;
	List<Image> gImages;
	customGallery gGallery;
	static ImageView img;
	static Bitmap bm = null;
	
	private BitmapManager bManager;
	public ImageGalleryViewAdapter(Context context, List<Image> pimages, customGallery gallery)
	{
		gContext = context;
		gImages = pimages;
		gGallery = gallery;
		
		bManager = new BitmapManager();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gImages.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return gImages.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Image imageObject = gImages.get(position);
						
		if (convertView == null) {
			convertView = View.inflate(gContext, R.layout.image_view, null);
		}
		
		img = (ImageView)convertView.findViewById(R.id.gallery_img_view);
		
		ProgressBar pbLoadImage = (ProgressBar)convertView.findViewById(R.id.pgLoadImage);
		
		pbLoadImage.setVisibility(View.VISIBLE);
		bManager.fetchDrawableOnThread(imageObject.getUrl(), img, pbLoadImage);
		img.setScaleType(ImageView.ScaleType.CENTER_CROP);
		return convertView;
	}
	
	static private Handler finishImageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	img.setImageBitmap(bm);
       }
     };
	
	
	private synchronized InputStream fetch(String urlString) throws MalformedURLException, IOException
	{
		URL myFileUrl = new URL(urlString);
		HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
		//conn.setDoInput(true);
		conn.connect();
		
		
		return new FlushedInputStream(conn.getInputStream());

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
