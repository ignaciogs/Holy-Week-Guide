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

package com.ignaciogs.semanasanta.imagegallery;

import java.text.NumberFormat;
import java.util.Locale;

public class Image {
	
	private String name = "";
	private String url = "";
	private String thumbURL="";
	private String ticket = "";
	private String nameProduct = "";
	private String price = "";
	private String productUrl = "";
	private String mark = "";
	
	public void setName(String pName) {
		name = pName;
	}
	
	public String getName() {
		return name;
	}
	
	public void setUrl(String pUrl) {
		url = pUrl;		
	}
	
	public String getUrl() {
		return url;
	}
	public String getThumbUrl(){
		return thumbURL;
	}
	public void setThumbUrl(String thumbURL){
		this.thumbURL = thumbURL;
	}
	public String getTicket() {
		return ticket;
	}
	
	public void setTicket(String pTicket) {
		ticket = pTicket;
	}
	
	public void setNameProduct(String pNameProduct) {
		nameProduct = pNameProduct;
	}
	
	public String getNameProduct() {
		return nameProduct;
	}
	
	public void setPrice(String pPrice) {
		String[] a=pPrice.split(",");
		int p=Integer.parseInt(a[0].trim());
		NumberFormat nf=NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
		price = nf.format((float)p/100);
	}
	
	public String getPrice() {
		return price;
	}
	
	public void setProductUrl(String pProductUrl) {
		productUrl = pProductUrl;
	}
	
	public String getProductUrl() {
		return productUrl;
	}
	
	public void setMark(String pMark) {
		mark = pMark;
	}
	
	public String getMark() {
		return mark;
	}

}
