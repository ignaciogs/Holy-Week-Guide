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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cofradia implements Serializable {

	/**
	 * Version serializable
	 */
	private static final long serialVersionUID = 1L;
	
	private String nombre_corto = "";
	private String nombre_largo = "";
	private String fecha_salida = "";
	private String nombre_iglesia = "";
	private int numero_pasos = 0;
	private String itinerario = "";
	private List<Horario> horarios = new ArrayList<Horario>();
	private String imagenEscudo = "";
	private String horaSalida = "";
	private String horaRecogida = "";
	private List<String> images = new ArrayList<String>();
	private String latitudRegreso = "0";
	private String longitudRegreso = "0";
	private String ficheroRecorrido = "";
	private List<Poi> pois = new ArrayList<Poi>();
	
	public List<Poi> getPois() {
		return pois;
	}

	public void setPois(List<Poi> pois) {
		this.pois = pois;
	}

	public String getFicheroRecorrido() {
		return ficheroRecorrido;
	}

	public void setFicheroRecorrido(String ficheroRecorrido) {
		this.ficheroRecorrido = ficheroRecorrido;
	}

	public String getLatitudRegreso() {
		return latitudRegreso;
	}

	public void setLatitudRegreso(String latitudRegreso) {
		this.latitudRegreso = latitudRegreso;
	}

	public String getLongitudRegreso() {
		return longitudRegreso;
	}

	public void setLongitudRegreso(String longitudRegreso) {
		this.longitudRegreso = longitudRegreso;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(String horaSalida) {
		this.horaSalida = horaSalida;
	}

	public String getHoraRecogida() {
		return horaRecogida;
	}

	public void setHoraRecogida(String horaRecogida) {
		this.horaRecogida = horaRecogida;
	}

	public String getImagenEscudo() {
		return imagenEscudo;
	}

	public void setImagenEscudo(String imagenEscudo) {
		this.imagenEscudo = imagenEscudo;
	}

	public String getNombre_corto() {
		return nombre_corto;
	}

	public void setNombre_corto(String nombre_corto) {
		this.nombre_corto = nombre_corto;
	}

	public String getNombre_largo() {
		return nombre_largo;
	}
	
	public void setNombre_largo(String nombre_largo) {
		this.nombre_largo = nombre_largo;
	}
	
	public String getFecha_salida() {
		return fecha_salida;
	}
	
	public void setFecha_salida(String fecha_salida) {
		this.fecha_salida = fecha_salida;
	}
	
	public String getNombre_iglesia() {
		return nombre_iglesia;
	}
	public void setNombre_iglesia(String nombre_iglesia) {
		this.nombre_iglesia = nombre_iglesia;
	}
	public int getNumero_pasos() {
		return numero_pasos;
	}
	
	public void setNumero_pasos(int numero_pasos) {
		this.numero_pasos = numero_pasos;
	}
	
	public String getItinerario() {
		return itinerario;
	}
	
	public void setItinerario(String itinerario) {
		this.itinerario = itinerario;
	}

	public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}
	
}
