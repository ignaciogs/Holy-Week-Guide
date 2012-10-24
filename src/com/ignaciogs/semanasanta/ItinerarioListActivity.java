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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import greendroid.app.GDActivity;
import greendroid.widget.*;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ItinerarioListActivity extends GDActivity {
	
	private QuickActionGrid mMenuDiasSemana;
	private ListView listView;
	private List<Cofradia> partialList = new ArrayList<Cofradia>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.itinerario_list_activity);
        
        partialList.addAll(Utils.cofradias);
        ItinerarioListAdapter adapter = new ItinerarioListAdapter(this, partialList);   
        listView = (ListView)findViewById(R.id.itinerario_list_lvDatos);
        listView.setOnItemClickListener(click_lista);
        listView.setAdapter(adapter);
        
        ActionBar ab = getActionBar();
        ab.addItem(greendroid.widget.ActionBarItem.Type.Add);
        ab.getItem(0).setDrawable(R.drawable.action_bar_itinerario);
        
        Typeface fontFace = Typeface.createFromAsset(getAssets(), "fonts/chris.ttf");
        ab.setTitle(getString(R.string.lista_cofradias_title));
        
        /* Creamos los quickaction dependiendo de los días de la semana */
        mMenuDiasSemana = new QuickActionGrid(this);
        mMenuDiasSemana.setOnQuickActionClickListener(mActionListenerDiasSemana);
        /* Metemos el de todos los dias */
        mMenuDiasSemana.addQuickAction(new QuickAction(this, R.drawable.action_bar_transparent, getString(R.string.lista_cofradias_todas)));
        for (String dia : Utils.daysOfWeeks) {
        	String desc = dia.substring(0,3);
        	if (dia.indexOf(getString(R.string.text_madruga)) > -1) { //Indicamos que es madrugada
        		desc += " (" + getString(R.string.text_madruga).substring(0,1) + ")";
        	}
        	mMenuDiasSemana.addQuickAction(new QuickAction(this, R.drawable.action_bar_transparent, desc));
        }
	}
	
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int pos) {
		switch (pos) {
		case 0:
			mMenuDiasSemana.show(item.getItemView());
			break;
		default:
			break;
	    }
	    return super.onHandleActionBarItemClick(item, pos);
	}
	
	private OnQuickActionClickListener mActionListenerDiasSemana = new OnQuickActionClickListener() {
	    public void onQuickActionClicked(QuickActionWidget widget, int position) {
	    	if (position == 0) {
	    		FilterByDayOfWeek(getString(R.string.lista_cofradias_todas));
	    	} else {
	    		FilterByDayOfWeek(Utils.daysOfWeeks.get(position - 1));
	    	}
	    }
	};
	
	private void FilterByDayOfWeek(String dayOfWeek) {
		if (dayOfWeek.equals(getString(R.string.lista_cofradias_todas))) { //son todas las cofradias
			partialList.clear();
			partialList.addAll(Utils.cofradias);
		} else {
			partialList.clear();
            Boolean isPrimerDomingo = true;
			for (Cofradia cofradia : Utils.cofradias) {
				if (dayOfWeek.equals(Utils.getNameDayFromDate(cofradia.getFecha_salida(), cofradia.getHoraSalida(), ItinerarioListActivity.this, isPrimerDomingo))) {
					partialList.add(cofradia);
				}
			}
		}
		listView.setAdapter(new ItinerarioListAdapter(this, partialList) );
	}
	
	private OnItemClickListener click_lista = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			Intent i = new Intent(ItinerarioListActivity.this, FichaCofradia.class);
			Bundle params = new Bundle();
			params.putSerializable("cofradia", (Serializable)partialList.get(arg2));
			i.putExtra("datos", params);
			startActivity(i);
		}
		
	};
}
