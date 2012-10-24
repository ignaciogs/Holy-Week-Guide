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

package com.ignaciogs.semanasanta;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.ignaciogs.semanasanta.imagegallery.customGallery;
import greendroid.app.GDActivity;
import greendroid.widget.ActionBar;

import java.util.List;

public class ImagesViewActivity extends GDActivity {
	
	private Cofradia currentCofradia;
	private customGallery gallery;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.images_view_activity);
        
        Bundle extras = getIntent().getExtras();
        Bundle params = extras.getBundle("datos");
        
        currentCofradia = (Cofradia) params.getSerializable("cofradia");
        
        gallery = (customGallery)findViewById(R.id.images_view_gallery);
        AdapterImages ai= new AdapterImages(this, currentCofradia.getImages());
        gallery.setAdapter(ai);
        
        ActionBar ab = getActionBar();
        Typeface fontFace = Typeface.createFromAsset(getAssets(), "fonts/chris.ttf");
      //Trabajo ab.setTypeFace(fontFace, 26.f);
        ab.setTitle(currentCofradia.getNombre_corto());
	}
	
	private class AdapterImages extends BaseAdapter {
		
		private Context context;
		private List<String> listImages;
		
		public AdapterImages(Context context, List<String> lista) {
			this.context = context;
			this.listImages = lista;
		}

		@Override
		public int getCount() {
			return listImages.size();
		}

		@Override
		public Object getItem(int arg0) {
			return listImages.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.item_image_view, null);
			}
			
			ImageView img = (ImageView)convertView.findViewById(R.id.item_image_view_imageLarge);
			int idImage = context.getResources().getIdentifier(listImages.get(position), "drawable", "com.ignaciogs.semanasanta");
			if (idImage > 0) {
				img.setImageResource(idImage);
			} else {
				img.setImageResource(R.drawable.main_cadiz);
			}
			
			return convertView;
		}
		
	}
	
}
