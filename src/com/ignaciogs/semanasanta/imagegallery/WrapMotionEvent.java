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

import android.view.MotionEvent;

public class WrapMotionEvent {
   protected MotionEvent event;

   protected WrapMotionEvent(MotionEvent event) {
      this.event = event;
   }

   static public WrapMotionEvent wrap(MotionEvent event) {
      try {
         return new EclairMotionEvent(event);
      } catch (VerifyError e) {
         return new WrapMotionEvent(event);
      }
   }

   public int getAction() {
      return event.getAction();
   }

   public float getX() {
      return event.getX();
   }

   public float getX(int pointerIndex) {
      verifyPointerIndex(pointerIndex);
      return getX();
   }

   public float getY() {
      return event.getY();
   }

   public float getY(int pointerIndex) {
      verifyPointerIndex(pointerIndex);
      return getY();
   }

   public int getPointerCount() {
      return 1;
   }

   public int getPointerId(int pointerIndex) {
      verifyPointerIndex(pointerIndex);
      return 0;
   }

   private void verifyPointerIndex(int pointerIndex) {
      if (pointerIndex > 0) {
         throw new IllegalArgumentException(
               "Invalid pointer index for Donut/Cupcake");
      }
   }
   
}

