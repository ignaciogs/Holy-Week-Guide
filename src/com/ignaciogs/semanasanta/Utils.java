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
import android.util.Log;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Utils {
	
	public static List<Cofradia> cofradias = new ArrayList<Cofradia>();
	public static List<String> daysOfWeeks = new ArrayList<String>();
	//public static String KEY_GOOGLE_MAPS = "0B8Rsd6rLL0nlCx9VT1moiWzNGYAosIa0lMNfSg"; //Desarrollo
	public static String KEY_GOOGLE_MAPS = "0EVJD0cnsmODNkBv_RRNbJlRr8g7kyTvdv3lHHw"; //Market

	public static int horaFinMadrugada = 7;
	
	public static String fileDataCofradias = "data/cadiz.xml";
	public static String directoryRutes = "rutas/";
	
	
	public static void loadDataCofradias(Context context) {
		try {
			cofradias.clear();
        	SAXParserFactory spf = SAXParserFactory.newInstance();
        	SAXParser sp = spf.newSAXParser();			
        	XMLReader xr = sp.getXMLReader();
        	HandlerXMLCofradias gwh = new HandlerXMLCofradias();
        	xr.setContentHandler(gwh);		
        	InputStream is = context.getAssets().open(fileDataCofradias);
        	xr.parse(new InputSource(is));
        } catch (Exception e) {
			 Log.d("semanasanta", e.getMessage());
			 e.printStackTrace();
		}
	}
	
	public static String getNameDayFromDate(String fecha, String horaSalida, Context context, boolean isFirstSundey) {
		String dia = "";
		GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			gc.setTime(sdf.parse(fecha));
			String desc = context .getString(R.string.text_santo);
			int dayOfWeek = gc.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek == 1) { //Domingo de ramos o resureccion
                desc = "";
                if (isFirstSundey)  { // Domingo de ramos
				    //desc = context.getString(R.string.ramos);
                } else { //Domingo de resurreccion
                    //desc = context.getString(R.string.resureccion);
                }
			}
			if ( Integer.valueOf(horaSalida.substring(0, 2)) > horaFinMadrugada ) {
				SimpleDateFormat sdfDia = new SimpleDateFormat("EEEE '" + desc + ", ' dd ' de ' MMMM ");
				dia = sdfDia.format(gc.getTime());
			} else {
				SimpleDateFormat sdfDia = new SimpleDateFormat("EEEE '" + desc + 
						" " + context.getString(R.string.text_madruga) + ", ' dd ' de ' MMMM ");
				dia = sdfDia.format(gc.getTime());
			}
			dia = dia.substring(0,1).toUpperCase() + dia.substring(1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dia;
	}
	
	public static void loadDaysOfWeeks(Context context) {
		daysOfWeeks.clear();
        Boolean isPrimerDomingo = true;
		for (Cofradia cofradia : cofradias) {
			if ( ! cofradia.getFecha_salida().equals("")) {
				String dia = getNameDayFromDate(cofradia.getFecha_salida(), cofradia.getHoraSalida(), context, isPrimerDomingo);

				if ( ! daysOfWeeks.contains(dia)) {
					daysOfWeeks.add(dia);
				}
			}
		}
	}

}
