package com.emtdev.morph.impl;

import com.emtdev.morph.PathPropertyResolver;

public class SystemEnvironmentPropertyResolver implements PathPropertyResolver {

   @Override
   public String resolve(String propertyKey) {
      return System.getProperty(propertyKey);
   }
}
