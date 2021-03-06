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


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.ignaciogs.semanasanta.adapters.ImagesAdapter;

public class ImageGalleryActivity extends SherlockFragmentActivity {

    public static final String KEY_OBJECT = "KEY_OBJECT";

    private Cofradia currentCofradia;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_gallery_activity);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(KEY_OBJECT) && extras.get(KEY_OBJECT) instanceof Cofradia) {
            currentCofradia = (Cofradia) extras.get(KEY_OBJECT);
        }

        getSupportActionBar().setTitle(currentCofradia.getNombre_corto());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImagesAdapter imagesAdapter = new ImagesAdapter(getSupportFragmentManager(), currentCofradia.getImages(), ImageGalleryActivity.this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.image_gallery_vp_images);
        viewPager.setAdapter(imagesAdapter);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        boolean result = false;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                result = true;
                break;
        }
        return result;
    }

}
