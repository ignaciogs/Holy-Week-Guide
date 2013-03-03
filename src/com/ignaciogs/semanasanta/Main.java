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
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class Main extends SherlockActivity {

    private final String SHARED_PREFERENCES_ID = "PREFERENCE_SEMANA_SANTA";
    private final String SHARED_PREFERENCES_CITY = "PREFERENCE_CITY";

    private ImageView imgFondo;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        imgFondo = (ImageView) findViewById(R.id.main_imageFondo);

        getSupportActionBar().setTitle(getString(R.string.holyWeekGuide));
        
        /* Cargamos los datos desde el xml */
        SharedPreferences mySharedPreferences = getSharedPreferences(SHARED_PREFERENCES_ID, Activity.MODE_PRIVATE);
        if (mySharedPreferences != null) {
            String file = mySharedPreferences.getString(SHARED_PREFERENCES_CITY, "");
            if (file.equals("")) {
                Toast.makeText(this, getString(R.string.msgSelectCity), Toast.LENGTH_LONG).show();
                DataManager.getInstance().setFileDataCofradias("data/cadiz.xml");
            } else {
                DataManager.getInstance().setFileDataCofradias(file);
                loadImageFondo(imgFondo);
            }
            ApplicationSemanaSanta app = (ApplicationSemanaSanta) getApplication();
            final String fileCof = DataManager.getInstance().getFileDataCofradias();
            app.setNameActiveCity(fileCof.substring(fileCof.indexOf("/") + 1, fileCof.indexOf(".xml")));
        } else {
            Toast.makeText(this, getString(R.string.msgSelectCity), Toast.LENGTH_LONG).show();
        }
        DataManager.getInstance().loadDataCofradias(this);
        
        /* Click en la opcion de itinerarios */
        TextView tvItinerarios = (TextView) findViewById(R.id.main_Itinerario);
        tvItinerarios.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main.this, ItinerarioListActivity.class);
                startActivity(i);
            }
        });
        
        /* Click en la opcion de Ir a */
        TextView tvDondeIr = (TextView) findViewById(R.id.main_Encontrar);
        tvDondeIr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main.this, getString(R.string.dondeir_desarrollo), Toast.LENGTH_LONG).show();
            }
        });
        
        /* Click en la opcion de acerca de */
        TextView tvAcercaDe = (TextView) findViewById(R.id.main_AcercaDe);
        tvAcercaDe.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main.this, AcercaDeActivity.class);
                startActivity(i);
            }
        });

        
        /* Nos guardamos los diferentes dias de la semana que hay en el xml */
        DataManager.getInstance().loadDaysOfWeeks(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("")
                .setIcon(R.drawable.action_bar_mapa_espana)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        boolean handled = false;
        switch (item.getItemId()) {
            case 0:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.select_city));
                builder.setItems(new CharSequence[]{"Cádiz", "Córdoba", "Jerez de la Frontera", "Granada", "San Fernando"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int itemIndex) {
                                ApplicationSemanaSanta app = (ApplicationSemanaSanta) getApplication();
                                switch (itemIndex) {
                                    case 0:
                                        DataManager.getInstance().setFileDataCofradias("data/cadiz.xml");
                                        app.setNameActiveCity("cadiz");
                                        break;
                                    case 1:
                                        DataManager.getInstance().setFileDataCofradias("data/cordoba.xml");
                                        app.setNameActiveCity("cordoba");
                                        break;
                                    case 2:
                                        DataManager.getInstance().setFileDataCofradias("data/jerez.xml");
                                        app.setNameActiveCity("jerez");
                                        break;
                                    case 3:
                                        DataManager.getInstance().setFileDataCofradias("data/granada.xml");
                                        app.setNameActiveCity("granada");
                                        break;
                                    case 4:
                                        DataManager.getInstance().setFileDataCofradias("data/san_fernando.xml");
                                        app.setNameActiveCity("sanfernando");
                                        break;
                                }
                                loadImageFondo(imgFondo);
                                DataManager.getInstance().loadDataCofradias(Main.this);
                                DataManager.getInstance().loadDaysOfWeeks(Main.this);
                                saveSelectionPreference();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                handled = true;
                break;
            default:
                //finish();
                //handled = true;
                break;
        }
        return handled;
    }

    private void saveSelectionPreference() {
        SharedPreferences mySharedPreferences = getSharedPreferences(SHARED_PREFERENCES_ID, Activity.MODE_PRIVATE);
        if (mySharedPreferences != null) {
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString(SHARED_PREFERENCES_CITY, DataManager.getInstance().getFileDataCofradias());
            editor.commit();
        }
    }

    private void loadImageFondo(ImageView img) {
        String name = DataManager.getInstance().getFileDataCofradias();
        name = name.replace("data/", "");
        name = name.replace(".xml", "");
        name = "main_" + name;
        int idImg = getResources().getIdentifier(name, "drawable", "com.ignaciogs.semanasanta");
        if (idImg > 0) {
            img.setImageResource(idImg);
        }
    }
}