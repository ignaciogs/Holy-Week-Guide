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

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockYouTubeBaseActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.ignaciogs.semanasanta.adapters.VideosAdapter;
import com.ignaciogs.semanasanta.response.InitialVideosListResponse;
import com.ignaciogs.semanasanta.response.VideoResponse;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * VideoViewActivity class
 */
public class VideoViewActivity extends SherlockYouTubeBaseActivity {

    public static final String KEY_OBJECT = "KEY_OBJECT";
    private Cofradia currentCofradia;
    private YouTubePlayerView youTubeView;
    private ListView lvData;
    private YouTubePlayer ytPlayer;
    private LinearLayout llData, llProgressbar;
    private VideosAdapter videosAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view_activity);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(KEY_OBJECT) && extras.get(KEY_OBJECT) instanceof Cofradia) {
            currentCofradia = (Cofradia) extras.get(KEY_OBJECT);

        }

        llData = (LinearLayout) findViewById(R.id.video_view_ll_data);
        llProgressbar = (LinearLayout) findViewById(R.id.video_view_ll_loading);
        lvData = (ListView) findViewById(R.id.video_view_lv_data);

        getSupportActionBar().setTitle(currentCofradia.getNombre_corto());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        youTubeView = (YouTubePlayerView) findViewById(R.id.video_view_ytp_player);
        youTubeView.initialize(getString(R.string.developerKey), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                ytPlayer = youTubePlayer;
                youTubePlayer.cueVideo(currentCofradia.getVideos().get(0));
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(VideoViewActivity.this, getString(R.string.youTubeNoConnected), Toast.LENGTH_LONG).show();
            }
        });

        loadVideos();

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

    private void loadVideos() {
        llData.setVisibility(View.GONE);
        llProgressbar.setVisibility(View.VISIBLE);
        InfoVideosTask infoVideosTask = new InfoVideosTask();
        infoVideosTask.execute();
    }

    private class InfoVideosTask extends AsyncTask<Void, Void, InitialVideosListResponse> {

        @Override
        protected InitialVideosListResponse doInBackground(Void... voids) {
            InitialVideosListResponse result = null;
            String codesVideos = "";
            for (String code : currentCofradia.getVideos()) {
                if (!TextUtils.isEmpty(codesVideos)) {
                    codesVideos += "%7c";
                }
                codesVideos += code;
            }
            String endPoint = "https://gdata.youtube.com/feeds/api/videos?q=" + codesVideos + "&v=2&alt=jsonc";
            try {
                HttpGet uri = new HttpGet(endPoint);
                DefaultHttpClient client = new DefaultHttpClient();
                HttpResponse resp = client.execute(uri);
                StatusLine status = resp.getStatusLine();
                if (status.getStatusCode() != 200) {
                    Log.d("holy-week", "HTTP error, invalid server status code: " + resp.getStatusLine());
                    hideProgressbar();
                } else {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
                    StringBuilder json = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        json.append(line);
                    }
                    Gson gson = new Gson();
                    result = gson.fromJson(json.toString(), InitialVideosListResponse.class);
                }
            } catch (ClientProtocolException e) {
                hideProgressbar();
            } catch (IOException e) {
                hideProgressbar();
            }
            return result;
        }

        @Override
        protected void onPostExecute(InitialVideosListResponse videosListResponse) {
            hideProgressbar();
            videosAdapter = new VideosAdapter(videosListResponse);
            lvData.setAdapter(videosAdapter);
            lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    VideoResponse videoResponse = (VideoResponse)adapterView.getAdapter().getItem(position);
                    ytPlayer.cueVideo(videoResponse.getId());
                }
            });
            super.onPostExecute(videosListResponse);
        }
    }

    private void hideProgressbar() {
        llData.setVisibility(View.VISIBLE);
        llProgressbar.setVisibility(View.GONE);
    }

}
