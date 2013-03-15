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

package com.ignaciogs.semanasanta.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ignaciogs.semanasanta.R;
import com.ignaciogs.semanasanta.response.InitialVideosListResponse;
import com.ignaciogs.semanasanta.response.VideoResponse;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * VideosAdapter class
 */
public class VideosAdapter extends BaseAdapter {

    private InitialVideosListResponse data;

    public VideosAdapter(InitialVideosListResponse data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.getData().getItems().size();
    }

    @Override
    public VideoResponse getItem(int i) {
        return data.getData().getItems().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        VideoResponse item = data.getData().getItems().get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view_row, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.video_view_row_tv_title);
            holder.ivPreview = (ImageView) convertView.findViewById(R.id.video_view_row_iv_preview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder.title != null) {
            holder.title.setText(item.getTitle());
        }
        if (item.getThumbnail() != null) {
            ImageLoader.getInstance().displayImage(item.getThumbnail().getSqDefault(), holder.ivPreview);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView title;
        ImageView ivPreview;
    }
}
