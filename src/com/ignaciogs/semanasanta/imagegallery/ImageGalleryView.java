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
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ignaciogs.semanasanta.Cofradia;
import com.ignaciogs.semanasanta.R;

import java.util.ArrayList;


public class ImageGalleryView extends Activity {
	
	private final int ITEM_ID_MENU_ALL_IMAGES = 1;
	private ArrayList<Image> listGalleryImages = new ArrayList<Image>();
	
	private static final String URL_BASE_IMAGES = "http://www.ignaciogs.es/images_semana_santa/";
	
	private Cofradia currentCofradia;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_gallery_view);     
        
        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            Bundle params = extras.getBundle("datos");
	        currentCofradia = (Cofradia) params.getSerializable("cofradia");
	        
	        for (String img : currentCofradia.getImages()) {
	        	Image image = new Image();
	        	image.setUrl(URL_BASE_IMAGES + img + ".jpg");
	        	image.setThumbUrl(URL_BASE_IMAGES + img + ".jpg");
	        	listGalleryImages.add(image);
	        }
	        
	        customGallery g = (customGallery) findViewById(R.id.cgImageViewGallery);        
	        g.setAdapter(new ImageGalleryViewAdapter(this, listGalleryImages, g));
	
	        GridView gridview = (GridView) findViewById(R.id.gallery_gridview_allImages);        
	        gridview.setAdapter(new LazyAdapter(this, listGalleryImages));
	        gridview.setOnItemClickListener(click_itemGallery);
	        
	        RelativeLayout rlAllImages = (RelativeLayout)findViewById(R.id.rlgallery_gridview_allImages);
	        rlAllImages.setVisibility(View.VISIBLE);
	        	        
	        TextView txtTitle = (TextView)findViewById(R.id.gallery_gridview_txtCaption);
        	txtTitle.setText(currentCofradia.getNombre_corto());
        	txtTitle.setVisibility(View.VISIBLE);
        }
    }
	
	private OnItemClickListener click_itemGallery = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			RelativeLayout rlAllImages = (RelativeLayout)findViewById(R.id.rlgallery_gridview_allImages);
	        rlAllImages.setVisibility(View.GONE);
	        
			customGallery g = (customGallery) findViewById(R.id.cgImageViewGallery);
			g.setSelection(arg2, true);
		}
	};	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, ITEM_ID_MENU_ALL_IMAGES, 0, R.string.imageGallery_all_images).setIcon(
				android.R.drawable.ic_menu_gallery).setAlphabeticShortcut('N');		
		return true; 
	}
	
	@Override
 	public boolean onMenuItemSelected(int featureId, MenuItem item) {
 		switch (item.getItemId()) {
 		case ITEM_ID_MENU_ALL_IMAGES:
 			RelativeLayout rlAllImages = (RelativeLayout)findViewById(R.id.rlgallery_gridview_allImages);
 	        rlAllImages.setVisibility(View.VISIBLE);
 			break;		
 		}
 		return super.onMenuItemSelected(featureId, item);
 	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			RelativeLayout rlAllImages = (RelativeLayout)findViewById(R.id.rlgallery_gridview_allImages);
			if(rlAllImages.getVisibility()==View.VISIBLE){
		        rlAllImages.setVisibility(View.GONE);
		        return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		GridView gridview = (GridView) findViewById(R.id.gallery_gridview_allImages);
		LazyAdapter gridLazyAdapter=(LazyAdapter) gridview.getAdapter();
		if(gridLazyAdapter!=null){
			gridLazyAdapter.terminate();
		}
	}
}
