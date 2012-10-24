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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import greendroid.app.GDActivity;
import greendroid.widget.ActionBar;
import greendroid.widget.ActionBarItem;

public class Main extends GDActivity {
	
	private final String SHARED_PREFERENCES_ID = "PREFERENCE_SEMANA_SANTA";
	private final String SHARED_PREFERENCES_CITY = "PREFERENCE_CITY";

	private ImageView imgFondo;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.main);
        
        imgFondo = (ImageView)findViewById(R.id.main_imageFondo);
        
        /* Cargamos los datos desde el xml */
        SharedPreferences mySharedPreferences = getSharedPreferences(SHARED_PREFERENCES_ID, Activity.MODE_PRIVATE);
		if (mySharedPreferences != null) {
			String file = mySharedPreferences.getString(SHARED_PREFERENCES_CITY, "");
			if (file.equals("")) {
				Toast.makeText(this, getString(R.string.msgSelectCity), Toast.LENGTH_LONG).show();
				Utils.fileDataCofradias = "data/cadiz.xml";
			} else {
				Utils.fileDataCofradias = file;
				loadImageFondo(imgFondo);
			}
            ApplicationSemanaSanta app = (ApplicationSemanaSanta)getApplication();
            app.setNameActiveCity(Utils.fileDataCofradias.substring(Utils.fileDataCofradias.indexOf("/")+1, Utils.fileDataCofradias.indexOf(".xml")));
		} else {
			Toast.makeText(this, getString(R.string.msgSelectCity), Toast.LENGTH_LONG).show();
		}
        Utils.loadDataCofradias(this);
        
        /* Click en la opcion de itinerarios */
        TextView tvItinerarios = (TextView)findViewById(R.id.main_Itinerario);
        tvItinerarios.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Main.this, ItinerarioListActivity.class);
				startActivity(i);
			}
		});
        
        /* Click en la opcion de Ir a */
        TextView tvDondeIr = (TextView)findViewById(R.id.main_Encontrar);
        tvDondeIr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(Main.this, getString(R.string.dondeir_desarrollo), Toast.LENGTH_LONG).show();
			}
		});
        
        /* Click en la opcion de acerca de */
        TextView tvAcercaDe = (TextView)findViewById(R.id.main_AcercaDe);
        tvAcercaDe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Main.this, AcercaDeActivity.class);
				startActivity(i);
			}
		});
        
        ActionBar ab = getActionBar();
        ab.addItem(greendroid.widget.ActionBarItem.Type.Add);
        ab.getItem(0).setDrawable(R.drawable.action_bar_mapa_espana);
        
        /* Nos guardamos los diferentes dias de la semana que hay en el xml */
        Utils.loadDaysOfWeeks(this);
    }
    
    @Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int pos) {
		switch (pos) {
		case 0:
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.select_city));
            builder.setItems( new CharSequence[]{"Cádiz", "Córdoba", "Jerez de la Frontera", "Granada", "San Fernando"},
                new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int itemIndex) {
                    ApplicationSemanaSanta app = (ApplicationSemanaSanta)getApplication();
                    switch (itemIndex) {
                        case 0:
                            Utils.fileDataCofradias = "data/cadiz.xml";
                            app.setNameActiveCity("cadiz");
                            break;
                        case 1:
                            Utils.fileDataCofradias = "data/cordoba.xml";
                            app.setNameActiveCity("cordoba");
                            break;
                        case 2:
                            Utils.fileDataCofradias = "data/jerez.xml";
                            app.setNameActiveCity("jerez");
                            break;
                        case 3:
                            Utils.fileDataCofradias = "data/granada.xml";
                            app.setNameActiveCity("granada");
                            break;
                        case 4:
                            Utils.fileDataCofradias = "data/san_fernando.xml";
                            app.setNameActiveCity("sanfernando");
                            break;
                    }
                    loadImageFondo(imgFondo);
                    Utils.loadDataCofradias(Main.this);
                    Utils.loadDaysOfWeeks(Main.this);
                    saveSelectionPreference();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
			break;
		default:
			break;
	    }
	    return super.onHandleActionBarItemClick(item, pos);
	}
	
	private void saveSelectionPreference() {
		SharedPreferences mySharedPreferences = getSharedPreferences(SHARED_PREFERENCES_ID, Activity.MODE_PRIVATE);
		if (mySharedPreferences != null) {
            SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putString(SHARED_PREFERENCES_CITY, Utils.fileDataCofradias);
			editor.commit();
		}
	}
	
	private void loadImageFondo(ImageView img) {
		String name = Utils.fileDataCofradias;
		name = name.replace("data/", "");
		name = name.replace(".xml", "");
		name = "main_" + name;
		int idImg = getResources().getIdentifier(name, "drawable", "com.ignaciogs.semanasanta");
		if (idImg > 0) {
			img.setImageResource(idImg);
		}
	}
}