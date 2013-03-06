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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlerXMLCofradias extends DefaultHandler {
	private String gTextoEtiqueta = "";
	private Cofradia cofradia;
	private Horario horario;
	private Punto punto;
	private Poi poi;

	@Override
    public void startDocument() throws SAXException {
    }
	
	@Override
    public void endDocument() throws SAXException {
    }
	
	@Override 
	public void characters(char ch[], int start, int length) { 
		gTextoEtiqueta += new String(ch, start, length); 		
	} 
	
	@Override
    public void startElement(String namespaceURI, String localName,String qName, Attributes atts) throws SAXException {
		gTextoEtiqueta = "";
		if (localName.equals("cofradia")) {
			cofradia = new Cofradia();						
		} else if (localName.equals("horario")) {
			horario = new Horario();
			horario.setDescripcion(atts.getValue(0));
			cofradia.getHorarios().add(horario);
		} else if (localName.equals("punto")) {
			punto = new Punto();
			punto.setDescripcion(atts.getValue(0));
			punto.setHora(atts.getValue(1));
			horario.getPuntos().add(punto);
		} else if (localName.equals("poi")) {
			poi = new Poi();
			poi.setDescription(atts.getValue(0));
			poi.setComment(atts.getValue(1));
			poi.setLat(Float.valueOf(atts.getValue(2)));
			poi.setLon(Float.valueOf(atts.getValue(3)));
			poi.setInteres(atts.getValue(4).equals("SI"));
			cofradia.getPois().add(poi);
		}
	}
	
	@Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

		if (localName.equals("cofradia")) {
			DataManager.getInstance().getCofradiasList().add(cofradia);
		} else if (localName.equals("nombre_corto")) {
			cofradia.setNombre_corto(gTextoEtiqueta);
		} else if (localName.equals("nombre_largo")) {
			cofradia.setNombre_largo(gTextoEtiqueta);
		} else if (localName.equals("fecha_salida")) {
			cofradia.setFecha_salida(gTextoEtiqueta);
		} else if (localName.equals("nombre_iglesia")) {
			cofradia.setNombre_iglesia(gTextoEtiqueta);
		} else if (localName.equals("numero_pasos")) {
			cofradia.setNumero_pasos(Integer.valueOf(gTextoEtiqueta));
		} else if (localName.equals("itinerario")) {
			cofradia.setItinerario(gTextoEtiqueta);
		} else if (localName.equals("imagenEscudo")) {
			cofradia.setImagenEscudo(gTextoEtiqueta);
		} else if (localName.equals("horaSalida")) {
			cofradia.setHoraSalida(gTextoEtiqueta);
		} else if (localName.equals("horaRecogida")) {
			cofradia.setHoraRecogida(gTextoEtiqueta);
		} else if (localName.equals("image")) {
			cofradia.getImages().add(gTextoEtiqueta);
		} else if (localName.equals("latitudRegreso")) {
			cofradia.setLatitudRegreso(gTextoEtiqueta);
		} else if (localName.equals("longitudRegreso")) {
			cofradia.setLongitudRegreso(gTextoEtiqueta);
		} else if (localName.equals("ficheroRecorrido")) {
			cofradia.setFicheroRecorrido(gTextoEtiqueta);
		} else if (localName.equals("descripcion")) {
            cofradia.setDescripcion(gTextoEtiqueta);
        } else if (localName.equals("masdatos")) {
            cofradia.setMoreData(gTextoEtiqueta);
        }
			
	}
}
