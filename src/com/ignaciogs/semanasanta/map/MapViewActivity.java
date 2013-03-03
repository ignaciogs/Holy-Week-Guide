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

package com.ignaciogs.semanasanta.map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.*;
import com.ignaciogs.semanasanta.Cofradia;
import com.ignaciogs.semanasanta.Poi;
import com.ignaciogs.semanasanta.R;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MapViewActivity extends SherlockMapActivity {
	
	private MapView mapView;
	private Cofradia currentCofradia;
	private PoiItemizedOverlay itemizedOverlay;
	private Drawable drawable;
	private myCoolLocationOverlay myLoc;
	List<Overlay> mapOverlays;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view_activity);
		
		Bundle extras = getIntent().getExtras();
		Bundle params = extras.getBundle("datos");
	        
	    currentCofradia = (Cofradia) params.getSerializable("cofradia");

        Typeface fontFace = Typeface.createFromAsset(getAssets(), "fonts/chris.ttf");
        //Trabajo ab.setTypeFace(fontFace, 26.f);
        getSupportActionBar().setTitle(currentCofradia.getNombre_corto());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		
	    LinearLayout layoutMapContainer = (LinearLayout) this.findViewById(R.id.LinearLayoutMap);
		mapView = new MapView(this, getString(R.string.KEY_GOOGLE_MAPS));
		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);
		layoutMapContainer.addView(mapView);
        try {
        	SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlreader = parser.getXMLReader();
			NavigationSaxHandler navSaxHandler = new NavigationSaxHandler();
            xmlreader.setContentHandler(navSaxHandler);

            InputSource is = new InputSource(this.getAssets().open(String.format("%s%s.xml", getString(R.string.directoryRoutes), currentCofradia.getFicheroRecorrido())));
            xmlreader.parse(is);
            String allCoordinates = navSaxHandler.getParsedData();
            drawPath(allCoordinates, Color.parseColor("#add331"), Color.parseColor("#ff0404"), mapView );
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mapOverlays = mapView.getOverlays();
		
		myLoc=new myCoolLocationOverlay(this, mapView);
		myLoc.runOnFirstFix(new Runnable() {
			@Override
			public void run() {
				mapView.getController().animateTo(myLoc.getMyLocation());
			}
		});
		mapOverlays.add(myLoc);
		showPOIs();
	}

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        boolean result = false;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return result;
    }
	
	private void showPOIs() {
		//mapOverlays.clear();
		for (Poi poi : currentCofradia.getPois()) {
			GeoPoint point = new GeoPoint( (int)(poi.getLat()*1E6),
		 			(int)(poi.getLon()*1E6) );
			if (poi.isInteres()) {
				drawable = getResources().getDrawable(R.drawable.bubble_interes);
			} else {
				drawable = getResources().getDrawable(R.drawable.bubble);
			}
			itemizedOverlay = new PoiItemizedOverlay(drawable, mapView);
			OverlayItem overlayItem = new OverlayItem(point, poi.getDescription(), 
					poi.getComment());
			itemizedOverlay.addOverlay(overlayItem);
			mapOverlays.add(itemizedOverlay);
		}
		
		//mapOverlays.add(myLoc);
	}

	public void drawPath(String allCoordinates, int color, int colorReturn, MapView mMapView01) {
	    // color correction for dining, make it darker
	    if (color == Color.parseColor("#add331")) color = Color.parseColor("#6C8715");

	    Collection overlaysToAddAgain = new ArrayList();
	    for (Iterator iter = mMapView01.getOverlays().iterator(); iter.hasNext();) {
	        Object o = iter.next();
	        if (!RouteOverlay.class.getName().equals(o.getClass().getName())) {
	            overlaysToAddAgain.add(o);
	        }
	    }
	    mMapView01.getOverlays().clear();
	    mMapView01.getOverlays().addAll(overlaysToAddAgain);

        String path = allCoordinates.replace(",0.000000 ", " ");
	    path = path.replace(",0 ", " ");
	    path = path.replace("\n", " ");
        path = path.replace("  ", " ");
	    if (path != null && path.trim().length() > 0) {
	        String[] tmpPairs = path.trim().split(" ");
            ArrayList<String> pairs = new ArrayList<String>();
            for (int i = 0; i < tmpPairs.length; i++) {
                if (!tmpPairs[i].equals("")) {
                    pairs.add(tmpPairs[i]);
                }
            }

	        String[] lngLat = pairs.get(0).split(",");
	        
	        if (lngLat[0].substring(0,1).equals("-")) {
	        	String vUno = lngLat[0];
	        	String vDos = lngLat[1];
	        	lngLat[0] = vDos;
	        	lngLat[1] = vUno;
	        }
	        
	        /* Centramos el mapa en la primera coordenada */
	        mMapView01.getController().setZoom(16);
			GeoPoint point = new GeoPoint((int) (Double.valueOf(lngLat[0]) * 1E6),
					(int) (Double.valueOf(lngLat[1]) * 1E6));
			mMapView01.getController().animateTo(point);
	        
	        if (lngLat.length<3) {
	        	lngLat = pairs.get(1).split(","); // if first pair is not transferred completely, take seconds pair //TODO
	        	if (lngLat[0].substring(0,1).equals("-")) {
    	        	String vUno = lngLat[0];
    	        	String vDos = lngLat[1];
    	        	lngLat[0] = vDos;
    	        	lngLat[1] = vUno;
    	        }
	        }
	        try {
	            GeoPoint startGP = new GeoPoint((int) (Double.parseDouble(lngLat[0]) * 1E6), (int) (Double.parseDouble(lngLat[1]) * 1E6));
	            mMapView01.getOverlays().add(new RouteOverlay(startGP, startGP, 1));
	            GeoPoint gp1;
	            GeoPoint gp2 = startGP;
	            boolean isReturn = false;
	            for (int i = 1; i < pairs.size() ; i++) // the last one would be crash
	            {
                    if (!pairs.get(i).equals(""))  {
                        lngLat = pairs.get(i).split(",");
                        if (lngLat[0].substring(0,1).equals("-")) {
                            String vUno = lngLat[0];
                            String vDos = lngLat[1];
                            lngLat[0] = vDos;
                            lngLat[1] = vUno;
                        }

                        gp1 = gp2;
                        /* Comprobamos si esta en la vuelta para pintarlo con otro color */
                        if ( (lngLat[1].equals(currentCofradia.getLongitudRegreso())) && (lngLat[0].equals(currentCofradia.getLatitudRegreso())) ) {
                            isReturn = true;
                        }
                        gp2 = new GeoPoint((int) (Double.parseDouble(lngLat[0]) * 1E6), (int) (Double.parseDouble(lngLat[1]) * 1E6));

                        if (gp2.getLatitudeE6() != 22200000) {
                            if (isReturn) {
                                mMapView01.getOverlays().add(new RouteOverlay(gp1, gp2, 2, colorReturn));
                            } else {
                                mMapView01.getOverlays().add(new RouteOverlay(gp1, gp2, 2, color));
                            }
                        }
                    }
	            }
	        } catch (NumberFormatException e) {
	            Log.e("semanasanta", "Cannot draw route.", e);
	        }
	    }
	    mMapView01.setEnabled(true);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		myLoc.enableMyLocation();
		myLoc.enableCompass();
	}	
	@Override
	protected void onPause() {
		super.onPause();
		myLoc.disableCompass();
		myLoc.disableMyLocation();
	}

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private class myCoolLocationOverlay extends MyLocationOverlay{

		public myCoolLocationOverlay(Context context, MapView mapView) {
			super(context, mapView);
		}

		@Override
		public synchronized void onLocationChanged(Location loc) {
			super.onLocationChanged(loc);
			/*
			if (loc != null) {
				GeoPoint p = new GeoPoint((int) (loc.getLatitude() * 1E6),
						(int) (loc.getLongitude() * 1E6));
				if(mLastMovedPosition!=null && loc!=null)
				if(mLastMovedPosition==null || loc.distanceTo(mLastMovedPosition)>mUpdateDistance*1000){
					Toast.makeText(CamerasMap.this, "Location changed by "+(loc.distanceTo(mLastMovedPosition))+" and update distance="+(mUpdateDistance*1000),Toast.LENGTH_SHORT).show();
					//android.util.Log.d("MAPVIEW","Updated last="+mLastMovedPosition+" and distance="+(loc.distanceTo(mLastMovedPosition)));
					mapView.getController().animateTo(p);
					mLastMovedPosition=new Location(loc);
				}
				mLastUpdatePosition=new Location(loc);
			}
			*/
		}
		
	}

}
