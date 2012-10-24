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
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.ignaciogs.semanasanta.imagegallery.ImageGalleryView;
import com.ignaciogs.semanasanta.map.MapViewActivity;
import greendroid.app.GDActivity;
import greendroid.widget.*;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.io.Serializable;

public class FichaCofradia extends GDActivity {
	
	private Cofradia currentCodradia;
	private QuickActionGrid mMenuFicha;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.ficha_cofradia);
        
        Bundle extras = getIntent().getExtras();
        Bundle params = extras.getBundle("datos");
        
        currentCodradia = (Cofradia) params.getSerializable("cofradia");
        
        /* Buscamos es escudo de la cofradia */
		ImageView imgEscudo = (ImageView)findViewById(R.id.ficha_cofradia_escudo);
		if (! currentCodradia.getImagenEscudo().equals("")) {
			int idEscudo = getResources().getIdentifier(currentCodradia.getImagenEscudo(), "drawable", "com.ignaciogs.semanasanta");
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
        String nomCorto = currentCodradia.getNombre_corto().replaceAll(" ", "");
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
        if (! currentCodradia.getImagenEscudo().equals("")) {
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
        txtNombreLargo.setText(currentCodradia.getNombre_largo());
        
        TextView txtNumeroPasos = (TextView)findViewById(R.id.ficha_cofradia_numeroPasos);
        txtNumeroPasos.setText(getString(R.string.ficha_cofradia_numeroPasos) + " " + currentCodradia.getNumero_pasos());
        
        TextView txtNombreIglesia = (TextView)findViewById(R.id.ficha_cofradia_nombreIglesia);
        txtNombreIglesia.setText(currentCodradia.getNombre_iglesia());
        
        TextView txtItinerario = (TextView)findViewById(R.id.ficha_cofradia_itinerario);
        txtItinerario.setText(currentCodradia.getItinerario());
        
        /* Cargamos el irinerario */
        loadItinerario();
        
        /* Creamos los botones de la barra superior */
        ActionBar ab = getActionBar();
        ab.addItem(greendroid.widget.ActionBarItem.Type.Add);

        ab.setTitle(currentCodradia.getNombre_corto());
        
        mMenuFicha = new QuickActionGrid(this);
        mMenuFicha.setOnQuickActionClickListener(mActionListenerMenuFicha);
        if (currentCodradia.getImages().size() > 0) {
            mMenuFicha.addQuickAction(new QuickAction(this, R.drawable.gd_action_bar_take_photo_alt, getString(R.string.ficha_cofradia_imagenes)));
            if (!currentCodradia.getFicheroRecorrido().equals(""))  {
                mMenuFicha.addQuickAction(new QuickAction(this, R.drawable.gd_action_bar_locate_alt, getString(R.string.ficha_cofradia_sobre_mapa)));
            }
        } else {
            mMenuFicha.setOnQuickActionClickListener(null);
        }
	}
	
	private OnQuickActionClickListener mActionListenerMenuFicha = new OnQuickActionClickListener() {
	    public void onQuickActionClicked(QuickActionWidget widget, int position) {
	    	if (position == 0) { //Imagenes
	    		if (currentCodradia.getImages().size() > 0) {
	    			Intent i = new Intent(FichaCofradia.this, ImageGalleryView.class);
					Bundle params = new Bundle();
					params.putSerializable("cofradia", currentCodradia);
					i.putExtra("datos", params);
					startActivity(i);
	    			
	    		} else {
	    			Toast.makeText(FichaCofradia.this, getString(R.string.msgNoFoto), Toast.LENGTH_LONG).show();
	    		}
	    	} else if (position == 2) { //Lugares destacados
	    	} else if (position == 1) { //Vision Mapa
	    		Intent i = new Intent(FichaCofradia.this, MapViewActivity.class);
				Bundle params = new Bundle();
				params.putSerializable("cofradia", currentCodradia);
				i.putExtra("datos", params);
				startActivity(i);
	    	}
	    }
	};
	
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int pos) {
		switch (pos) {
		case 0:
			mMenuFicha.show(item.getItemView());
			break;
		default:
			break;
	    }
	    return super.onHandleActionBarItemClick(item, pos);
	}
	
	private void loadItinerario() {
		TableLayout tlGeneral = (TableLayout)findViewById(R.id.ficha_cofradia_tlItinerario);
		int numColumns = currentCodradia.getHorarios().size();
		
		TableRow trCabecera = new TableRow(this);
		trCabecera.setBackgroundColor(0xFFFFFFFF);
		trCabecera.setPadding(1, 1, 1, 1);
		/* creamos un textvew en blanco para la cabecer */
		TextView tvBlanco = new TextView(this);
		tvBlanco.setText("   ");
		tvBlanco.setPadding(5, 5, 5, 5);
		trCabecera.addView(tvBlanco);
		for (Horario horario : currentCodradia.getHorarios()) {
			TextView tv = new TextView(this);
			tv.setPadding(5, 5, 5, 5);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setText(horario.getDescripcion().replace(" ", "\n"));
			tv.setTextColor(0xFF000000);
			trCabecera.addView(tv);
		}
		tlGeneral.addView(trCabecera);
		
		for (int j = 0; j < currentCodradia.getHorarios().get(0).getPuntos().size(); j++) {
			Punto punto = currentCodradia.getHorarios().get(0).getPuntos().get(j);
			TableRow tr = new TableRow(this);
			tr.setBackgroundColor(0xFFFFFFFF);
			tr.setPadding(1, 1, 1, 1);
			
			TextView tvDes = new TextView(this);
			tvDes.setText(punto.getDescripcion());
			tvDes.setBackgroundColor(0xFF000000);
			tvDes.setPadding(5, 10, 5, 10);
			tr.addView(tvDes);
			
			for (int i = 0; i < numColumns; i++) {
				String hora = currentCodradia.getHorarios().get(i).getPuntos().get(j).getHora();
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
