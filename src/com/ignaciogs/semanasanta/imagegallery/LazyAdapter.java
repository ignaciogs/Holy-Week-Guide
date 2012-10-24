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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import com.ignaciogs.semanasanta.R;
import com.ignaciogs.semanasanta.imagegallery.SoftImageLoader.ImageHolder;

import java.util.List;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private List<Image> data;
    private static LayoutInflater inflater=null;
    //public ImageLoader imageLoader;
    public SoftImageLoader imageLoader;
    private int itemWidth;
    public LazyAdapter(Activity a, List<Image> d) {
    	float density=a.getResources().getDisplayMetrics().density;
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader=new ImageLoader(activity.getApplicationContext(),R.drawable.logo_corte_ingles);
        itemWidth=(int)(density*75f);
        imageLoader=new SoftImageLoader(activity.getApplicationContext(),itemWidth,0);
    }
    public void terminate(){
    	imageLoader.stopThread();
    }
    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageHolder h;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.image_gallery_grid_item, null);
            convertView.setLayoutParams(new GridView.LayoutParams(itemWidth,itemWidth));
            h=imageLoader.new ImageHolder();
            h.img=(ImageView) convertView.findViewById(R.id.grid_image);
            h.img.setScaleType(ScaleType.CENTER_CROP);
            h.progress=(ProgressBar) convertView.findViewById(R.id.grid_image_load);
            convertView.setTag(h);
        }
        else
            h=(ImageHolder)convertView.getTag();
        
        final String url=data.get(position).getThumbUrl();
        
	    h.img.setTag(url);
	    imageLoader.DisplayImage(url, activity, h);
        return convertView;
    }
}