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

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.ignaciogs.semanasanta.map.MapViewActivity;

public class FichaCofradia extends SherlockActivity {

    private final static int GROUP_ACTION = 1;
    private final static int ACTION_IMAGES = 10;
    private final static int ACTION_ROUTE = 11;
    private final static int ACTION_RELEASES = 12;
    private final static int ACTION_HISTORY = 13;
	private Cofradia currentCofradia;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ficha_cofradia);
        
        Bundle extras = getIntent().getExtras();
        Bundle params = extras.getBundle("datos");
        
        currentCofradia = (Cofradia) params.getSerializable("cofradia");
        
        /* Buscamos es escudo de la cofradia */
		ImageView imgEscudo = (ImageView)findViewById(R.id.ficha_cofradia_escudo);
		if (! currentCofradia.getImagenEscudo().equals("")) {
			int idEscudo = getResources().getIdentifier(currentCofradia.getImagenEscudo(), "drawable", "com.ignaciogs.semanasanta");
			if (idEscudo > 0) {
				imgEscudo.setVisibility(View.VISIBLE);
				imgEscudo.setImageResource(idEscudo);
			} else {
				imgEscudo.setVisibility(View.INVISIBLE);
			}
		} else {
			imgEscudo.setVisibility(View.INVISIBLE);
		}

        //Buscamos la imagen del color de la tunicas
        ImageView imgTunica = (ImageView)findViewById(R.id.ficha_cofradia_imgTunicas);
        ApplicationSemanaSanta app = (ApplicationSemanaSanta)getApplication();
        String nomCorto = currentCofradia.getNombre_corto().replaceAll(" ", "");
        nomCorto = nomCorto.replaceAll("-", "_");
        nomCorto = nomCorto.replaceAll("ñ", "ny");
        nomCorto = nomCorto.replaceAll("á", "a");
        nomCorto = nomCorto.replaceAll("é", "e");
        nomCorto = nomCorto.replaceAll("í", "i");
        nomCorto = nomCorto.replaceAll("ó", "o");
        nomCorto = nomCorto.replaceAll("ú", "u");
        nomCorto = nomCorto.replaceAll("Á", "a");
        nomCorto = nomCorto.replaceAll("É", "e");
        nomCorto = nomCorto.replaceAll("Í", "i");
        nomCorto = nomCorto.replaceAll("Ó", "o");
        nomCorto = nomCorto.replaceAll("Ú", "u");
        String nomImg = app.getNameActiveCity() + "_tunica_" + nomCorto;
        nomImg = nomImg.toLowerCase();
        if (! currentCofradia.getImagenEscudo().equals("")) {
            int idTunica = getResources().getIdentifier(nomImg, "drawable", getApplication().getPackageName());
            if (idTunica > 0) {
                imgTunica.setVisibility(View.VISIBLE);
                imgTunica.setImageResource(idTunica);
            } else {
                imgTunica.setVisibility(View.GONE);
            }
        } else {
            imgTunica.setVisibility(View.GONE);
        }
        
        TextView txtNombreLargo = (TextView)findViewById(R.id.ficha_cofradia_nombreLargo);
        txtNombreLargo.setText(currentCofradia.getNombre_largo());
        
        TextView txtNumeroPasos = (TextView)findViewById(R.id.ficha_cofradia_numeroPasos);
        txtNumeroPasos.setText(getString(R.string.ficha_cofradia_numeroPasos) + " " + currentCofradia.getNumero_pasos());
        
        TextView txtNombreIglesia = (TextView)findViewById(R.id.ficha_cofradia_nombreIglesia);
        txtNombreIglesia.setText(currentCofradia.getNombre_iglesia());
        
        TextView txtItinerario = (TextView)findViewById(R.id.ficha_cofradia_itinerario);
        txtItinerario.setText(currentCofradia.getItinerario());
        
        /* Cargamos el irinerario */
        loadItinerario();
        
        /* Creamos los botones de la barra superior */
        getSupportActionBar().setTitle(currentCofradia.getNombre_corto());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu subMenu = menu.addSubMenu("");

        if (currentCofradia.getImages().size() > 0) {
            subMenu.add(GROUP_ACTION, ACTION_IMAGES, 0, getString(R.string.ficha_cofradia_imagenes));
        }

        if (!currentCofradia.getFicheroRecorrido().equals(""))  {
            subMenu.add(GROUP_ACTION, ACTION_ROUTE, 0, getString(R.string.route));
        }

        if (!TextUtils.isEmpty(currentCofradia.getDescripcion()))  {
            subMenu.add(GROUP_ACTION, ACTION_HISTORY, 0, getString(R.string.history));
        }

        if (!TextUtils.isEmpty(currentCofradia.getMoreData()))  {
            subMenu.add(GROUP_ACTION, ACTION_RELEASES, 0, getString(R.string.moreData));
        }

        MenuItem subMenu1Item = subMenu.getItem();
        subMenu1Item.setIcon(R.drawable.action_bar_menu);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        boolean result = false;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case ACTION_IMAGES:
                if (currentCofradia.getImages().size() > 0) {
                    Intent intent = new Intent(FichaCofradia.this, ImageGalleryActivity.class);
                    intent.putExtra(ImageGalleryActivity.KEY_OBJECT, currentCofradia);
                    startActivity(intent);
//	    			Intent i = new Intent(FichaCofradia.this, ImageGalleryView.class);
//					Bundle params = new Bundle();
//					params.putSerializable("cofradia", currentCofradia);
//					i.putExtra("datos", params);
//					startActivity(i);

	    		} else {
	    			Toast.makeText(FichaCofradia.this, getString(R.string.msgNoFoto), Toast.LENGTH_LONG).show();
	    		}
                break;
            case ACTION_ROUTE:
                Intent i = new Intent(FichaCofradia.this, MapViewActivity.class);
				Bundle params = new Bundle();
				params.putSerializable("cofradia", currentCofradia);
				i.putExtra("datos", params);
				startActivity(i);
                break;
            case ACTION_HISTORY:
                Intent intent = new Intent(FichaCofradia.this, DescriptionActivity.class);
                intent.putExtra(DescriptionActivity.KEY_OBJECT, currentCofradia);
                startActivity(intent);
                break;
            case ACTION_RELEASES:
                Intent intentReleases = new Intent(FichaCofradia.this, ReleasesActivity.class);
                intentReleases.putExtra(DescriptionActivity.KEY_OBJECT, currentCofradia);
                startActivity(intentReleases);
                break;
        }
        return result;
    }
	
	private void loadItinerario() {
		TableLayout tlGeneral = (TableLayout)findViewById(R.id.ficha_cofradia_tlItinerario);
		int numColumns = currentCofradia.getHorarios().size();
		
		TableRow trCabecera = new TableRow(this);
		trCabecera.setBackgroundColor(0xFFFFFFFF);
		trCabecera.setPadding(1, 1, 1, 1);
		/* creamos un textvew en blanco para la cabecer */
		TextView tvBlanco = new TextView(this);
		tvBlanco.setText("   ");
		tvBlanco.setPadding(5, 5, 5, 5);
		trCabecera.addView(tvBlanco);
		for (Horario horario : currentCofradia.getHorarios()) {
			TextView tv = new TextView(this);
			tv.setPadding(5, 5, 5, 5);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setText(horario.getDescripcion().replace(" ", "\n"));
			tv.setTextColor(0xFF000000);
			trCabecera.addView(tv);
		}
		tlGeneral.addView(trCabecera);
		
		for (int j = 0; j < currentCofradia.getHorarios().get(0).getPuntos().size(); j++) {
			Punto punto = currentCofradia.getHorarios().get(0).getPuntos().get(j);
			TableRow tr = new TableRow(this);
			tr.setBackgroundColor(0xFFFFFFFF);
			tr.setPadding(1, 1, 1, 1);
			
			TextView tvDes = new TextView(this);
			tvDes.setText(punto.getDescripcion());
			tvDes.setBackgroundColor(0xFF000000);
			tvDes.setPadding(5, 10, 5, 10);
			tr.addView(tvDes);
			
			for (int i = 0; i < numColumns; i++) {
				String hora = currentCofradia.getHorarios().get(i).getPuntos().get(j).getHora();
				TextView tvHora = new TextView(this);
				tvHora.setBackgroundColor(0xFF000000);
				tvHora.setGravity(Gravity.CENTER_HORIZONTAL);
				tvHora.setPadding(5, 10, 5, 10);
				tvHora.setText(hora);
				tr.addView(tvHora);
			}
			
			tlGeneral.addView(tr);
		}
	}

}
