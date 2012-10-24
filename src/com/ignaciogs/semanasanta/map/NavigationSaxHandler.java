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

package com.ignaciogs.semanasanta.map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class NavigationSaxHandler extends DefaultHandler {
	
	// =========================================================== 
	 // Fields 
	 // =========================================================== 

	 private boolean in_coordinatestag = false;
    private boolean inLineString = false;

	 private StringBuffer buffer;

	 // =========================================================== 
	 // Getter & Setter 
	 // =========================================================== 

	 public String getParsedData() {
          return buffer.toString().trim();
	 } 

	 // =========================================================== 
	 // Methods 
	 // =========================================================== 
	 @Override 
	 public void startDocument() throws SAXException { 

	 } 

	 @Override 
	 public void endDocument() throws SAXException { 
	      // Nothing to do
	 }

	 @Override 
	 public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
         if (localName.equals("LineString")) {
           inLineString = true;
         } else if (localName.equals("coordinates") && inLineString) {
	          buffer = new StringBuffer();
	          this.in_coordinatestag = true;                        
	      }
	 } 

	 @Override 
	 public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
	       if (localName.equals("coordinates")) {
	           this.in_coordinatestag = false;
	       } else if (localName.equals("LineString")) {
             inLineString = false;
         }
	 } 

	 @Override 
	public void characters(char ch[], int start, int length) {
	    if(this.in_coordinatestag && inLineString){
	        buffer.append(ch, start, length);
	    }
	} 

}
