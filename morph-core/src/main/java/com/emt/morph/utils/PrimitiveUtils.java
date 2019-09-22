package com.emt.morph.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PrimitiveUtils {

   private static final List<Class<?>> primitives;

   static {
      Class<?>[] primitiveClasses = {boolean.class, Boolean.class,
              byte.class, Byte.class,
              char.class, Character.class,
              double.class, Double.class,
              float.class, Float.class,
              int.class, Integer.class,
              long.class, Long.class,
              short.class, Short.class,
              String.class};

      primitives = Arrays.stream(primitiveClasses).collect(Collectors.toList());
   }

   public static boolean isPrimitiveOrWrapper(Class<?> o) {
      return primitives.stream().anyMatch(o::isAssignableFrom);
   }


}
