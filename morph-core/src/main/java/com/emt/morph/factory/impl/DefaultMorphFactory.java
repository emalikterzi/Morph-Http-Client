package com.emt.morph.factory.impl;

import com.emt.morph.LoadBalancer;
import com.emt.morph.NameResolver;
import com.emt.morph.exception.MorphException;
import com.emt.morph.factory.HttpClientProviderFactory;
import com.emt.morph.factory.LoadBalancerFactory;
import com.emt.morph.factory.NameResolverFactory;
import com.emt.morph.http.HttpClientProvider;

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
