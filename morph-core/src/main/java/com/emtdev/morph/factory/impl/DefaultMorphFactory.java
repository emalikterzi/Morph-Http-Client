package com.emtdev.morph.factory.impl;

import com.emtdev.morph.LoadBalancer;
import com.emtdev.morph.NameResolver;
import com.emtdev.morph.exception.MorphException;
import com.emtdev.morph.factory.HttpClientProviderFactory;
import com.emtdev.morph.factory.LoadBalancerFactory;
import com.emtdev.morph.factory.NameResolverFactory;
import com.emtdev.morph.http.HttpClientProvider;

public class DefaultMorphFactory implements HttpClientProviderFactory, LoadBalancerFactory, NameResolverFactory {

   @Override
   public HttpClientProvider createHttpClientProvider(Class<? extends HttpClientProvider> httpClientProviderClass) {
      try {
         return httpClientProviderClass.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
         throw new MorphException(e);
      }
   }

   @Override
   public LoadBalancer createLoadBalancer(Class<? extends LoadBalancer> loadBalancerClass) {
      try {
         return loadBalancerClass.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
         throw new MorphException(e);
      }
   }

   @Override
   public NameResolver createNameResolver(Class<? extends NameResolver> nameResolverClass) {
      try {
         return nameResolverClass.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
         throw new MorphException(e);
      }
   }
}
