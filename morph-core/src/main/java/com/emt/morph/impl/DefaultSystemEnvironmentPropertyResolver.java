package com.emt.morph.impl;

import com.emt.morph.PathPropertyResolver;

public class DefaultSystemEnvironmentPropertyResolver implements PathPropertyResolver {

   @Override
   public String resolve(String propertyKey) {
      return System.getProperty(propertyKey);
   }
}
