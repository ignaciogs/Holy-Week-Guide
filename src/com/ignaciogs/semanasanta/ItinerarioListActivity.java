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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.ignaciogs.semanasanta.adapters.ItinerarioListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItinerarioListActivity extends SherlockActivity {

    private ListView listView;
    private List<Cofradia> partialList = new ArrayList<Cofradia>();
    private static final int GROUP_ICONS_DAYS = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itinerario_list_activity);

        partialList.addAll(DataManager.getInstance().getCofradiasList());
        ItinerarioListAdapter adapter = new ItinerarioListAdapter(this, partialList);
        listView = (ListView) findViewById(R.id.itinerario_list_lvDatos);
        listView.setOnItemClickListener(click_lista);
        listView.setAdapter(adapter);

        getSupportActionBar().setTitle(getString(R.string.lista_cofradias_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu subMenu = menu.addSubMenu("");
        subMenu.add(GROUP_ICONS_DAYS, 0, 0, getString(R.string.lista_cofradias_todas));
        for (int i = 0; i < DataManager.getInstance().getDaysOfWeeksList().size(); i++) {
            String dia = DataManager.getInstance().getDaysOfWeeksList().get(i);
            String desc = dia.substring(0, 3);
            if (dia.indexOf(getString(R.string.text_madruga)) > -1) { //Indicamos que es madrugada
                desc += " (" + getString(R.string.text_madruga) + ")";
            }
            MenuItem menuItem = subMenu.add(GROUP_ICONS_DAYS, i + 1, 0, desc);
        }

        MenuItem subMenu1Item = subMenu.getItem();
        subMenu1Item.setIcon(R.drawable.action_bar_itinerario);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return super.onCreateOptionsMenu(menu);
    }

    private void FilterByDayOfWeek(String dayOfWeek) {
        if (dayOfWeek.equals(getString(R.string.lista_cofradias_todas))) { //son todas las cofradias
            partialList.clear();
            partialList.addAll(DataManager.getInstance().getCofradiasList());
        } else {
            partialList.clear();
            Boolean isPrimerDomingo = true;
            for (Cofradia cofradia : DataManager.getInstance().getCofradiasList()) {
                if (dayOfWeek.equals(DataManager.getInstance().getNameDayFromDate(cofradia.getFecha_salida(), cofradia.getHoraSalida(), ItinerarioListActivity.this, isPrimerDomingo))) {
                    partialList.add(cofradia);
                }
            }
        }
        listView.setAdapter(new ItinerarioListAdapter(this, partialList));
    }

    private OnItemClickListener click_lista = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Intent i = new Intent(ItinerarioListActivity.this, FichaCofradia.class);
            Bundle params = new Bundle();
            params.putSerializable("cofradia", (Serializable) partialList.get(arg2));
            i.putExtra("datos", params);
            startActivity(i);
        }

    };

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        boolean result = false;
        if (item.getGroupId() == GROUP_ICONS_DAYS) {
            if (item.getItemId() == 0) {
                FilterByDayOfWeek(getString(R.string.lista_cofradias_todas));
            } else {
                FilterByDayOfWeek(DataManager.getInstance().getDaysOfWeeksList().get(item.getItemId() - 1));
            }
            result = true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            result = true;
        }
        return result;
    }

}
