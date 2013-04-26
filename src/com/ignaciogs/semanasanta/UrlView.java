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
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * UrlView class
 */
public class UrlView extends SherlockActivity {

    public static final String KEY_URL = "KEY_URL";
    private WebView webView;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.url_view);

        getSupportActionBar().setTitle(String.format("%s (Beta)", getString(R.string.main_onLive)));

        webView = (WebView) findViewById(R.id.url_view_wv_browser);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(KEY_URL)) {
            url = extras.getString(KEY_URL);

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.loadUrl(url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add("")
//                .setIcon(android.R.drawable.sea)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
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
