package com.emt.morph.utils;

public class Asserts {

   public static void notNull(Object object, String name) {
      if (object == null) {
         throw new IllegalStateException(name + " is null");
      }
   }
}
