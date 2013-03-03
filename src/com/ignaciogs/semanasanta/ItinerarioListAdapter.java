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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItinerarioListAdapter extends BaseAdapter {
	
	private Context context;
	private List<Cofradia> listCofradias;

    private Boolean isPrimerDomingo = true;
	
	public ItinerarioListAdapter(Context context, List<Cofradia> list) {
		this.context = context;
		this.listCofradias = list;
	}

	@Override
	public int getCount() {
		return listCofradias.size();
	}

	@Override
	public Object getItem(int position) {
		return listCofradias.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        Cofradia cofradia;
        cofradia = listCofradias.get(position);

        /*if (isPrimerDomingo) {
            try{
                GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                gc.setTime(sdf.parse(cofradia.getFecha_salida()));
                int dayOfWeek = gc.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == 1) {
                    isPrimerDomingo = false;
                }
            } catch (Exception e) {

            }
        }   */

		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_list_itinerario, null);
		}
		
		/* Si cambia el día de la semana mostramos la division de dias */
		TextView lblDiaSemana = (TextView)convertView.findViewById(R.id.item_list_itinerario_nombreDiaSemana);
		if (position == 0) {
			lblDiaSemana.setVisibility(View.VISIBLE);
			lblDiaSemana.setText(DataManager.getInstance().getNameDayFromDate(cofradia.getFecha_salida(), cofradia.getHoraSalida(), context, isPrimerDomingo));
		} else if ( (position > 0) && (! cofradia.getFecha_salida().equals(listCofradias.get(position -1).getFecha_salida()))) {
			lblDiaSemana.setVisibility(View.VISIBLE);
			lblDiaSemana.setText(DataManager.getInstance().getNameDayFromDate(cofradia.getFecha_salida(), cofradia.getHoraSalida(), context, isPrimerDomingo));
		} else {
			//Si la fecha es la misma comprobamos la hora ya que hay que distinguir entre madrugada y tarde
			int horaSalida = Integer.valueOf(cofradia.getHoraSalida().substring(0, 2));
			int horaSalidaAnt = Integer.valueOf(listCofradias.get(position -1).getHoraSalida().substring(0, 2));
			if (  ((horaSalida < DataManager.getInstance().getHoraFinMadrugada()) && (horaSalidaAnt >= DataManager.getInstance().getHoraFinMadrugada())) ||
					((horaSalidaAnt < DataManager.getInstance().getHoraFinMadrugada()) && (horaSalida >= DataManager.getInstance().getHoraFinMadrugada())) ) {
				//Una es de madrugada y la otra es de tarde
				lblDiaSemana.setVisibility(View.VISIBLE);
				lblDiaSemana.setText(DataManager.getInstance().getNameDayFromDate(cofradia.getFecha_salida(), cofradia.getHoraSalida(), context, isPrimerDomingo));
			} else {
				lblDiaSemana.setVisibility(View.GONE); 
			}
		}
		
		/* Buscamos es escudo de la cofradia */
		ImageView imgEscudo = (ImageView)convertView.findViewById(R.id.item_list_itinerario_escudo);
		if (! cofradia.getImagenEscudo().equals("")) {
			int idEscudo = context.getResources().getIdentifier(cofradia.getImagenEscudo(), "drawable", "com.ignaciogs.semanasanta");
			if (idEscudo > 0) {
				imgEscudo.setVisibility(View.VISIBLE);
				imgEscudo.setImageResource(idEscudo);
			} else {
				imgEscudo.setVisibility(View.INVISIBLE);
			}
		} else {
			imgEscudo.setVisibility(View.INVISIBLE);
		}
		
		TextView tvNombre = (TextView)convertView.findViewById(R.id.item_list_itinerario_nombreCofradia);
		tvNombre.setText(cofradia.getNombre_corto());
		
		TextView tvIglesia = (TextView)convertView.findViewById(R.id.item_list_itinerario_nombreIglesia);
		tvIglesia.setText(cofradia.getNombre_iglesia());
		
		TextView tvHorario = (TextView)convertView.findViewById(R.id.item_list_itinerario_nombreHorario);
		tvHorario.setText(context.getString(R.string.lista_cofradias_salida) + " " + cofradia.getHoraSalida() + " -- " + 
				context.getString(R.string.lista_cofradias_recogida) + " " + cofradia.getHoraRecogida());
		
		return convertView;
	}

}
