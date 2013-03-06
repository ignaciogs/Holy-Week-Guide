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

package com.ignaciogs.semanasanta.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.ignaciogs.semanasanta.ApplicationSemanaSanta;
import com.ignaciogs.semanasanta.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * ImageFragment class
 */
public class ImageFragment extends Fragment {

    private String imageUrl;

    /**
     * Create a new fragment for image
     *
     * @param imageUrl Image url
     * @return
     */
    public static ImageFragment newInstance(String imageUrl) {
        ImageFragment fragment = new ImageFragment();
        fragment.imageUrl = imageUrl;
        return fragment;
    }

    /**
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment, container, false);
        ImageView ivImage = (ImageView) view.findViewById(R.id.image_fragment_iv_image);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.image_fragment_pb_loading);
        ApplicationSemanaSanta applicationSemanaSanta = (ApplicationSemanaSanta) getActivity().getApplication();
        ImageLoader.getInstance().displayImage(imageUrl, ivImage, applicationSemanaSanta.getDefaultOptionsImage(), new CustomImageLoadingListenerWithProgressBar(progressBar));
        return view;
    }

    private class CustomImageLoadingListenerWithProgressBar extends SimpleImageLoadingListener {

        private ProgressBar progressBar;

        private CustomImageLoadingListenerWithProgressBar(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        private void showProgressBar(View view) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
            }
        }

        private void hideProgressBar(View view) {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {
            showProgressBar(view);
            super.onLoadingStarted(imageUri, view);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            hideProgressBar(view);
            super.onLoadingComplete(imageUri, view, loadedImage);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            hideProgressBar(view);
            super.onLoadingFailed(imageUri, view, failReason);
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            hideProgressBar(view);
            super.onLoadingCancelled(imageUri, view);
        }
    }

}
