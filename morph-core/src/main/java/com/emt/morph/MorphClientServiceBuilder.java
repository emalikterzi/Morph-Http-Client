package com.emt.morph;

import com.emt.morph.converter.MessageConverter;
import com.emt.morph.factory.HttpClientProviderFactory;
import com.emt.morph.factory.LoadBalancerFactory;
import com.emt.morph.factory.NameResolverFactory;
import com.emt.morph.http.HttpClientProvider;
import com.emt.morph.impl.DefaultSystemEnvironmentPropertyResolver;
import com.emt.morph.utils.Asserts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MorphClientServiceBuilder {

   private HttpClientProviderFactory httpClientProviderFactory;
   private LoadBalancerFactory loadBalancerFactory;
   private NameResolverFactory nameResolverFactory;

   private Set<Class<? extends NameResolver>> nameResolvers = new HashSet<>();
   private Set<Class<? extends LoadBalancer>> loadBalancers = new HashSet<>();
   private Set<Class<? extends HttpClientProvider>> httpClientProviders = new HashSet<>();
   //
   private List<MessageConverter<?>> messageConverters = new ArrayList<>();
   private PathPropertyResolver pathPropertyResolver = new DefaultSystemEnvironmentPropertyResolver();

   private MorphClientServiceBuilder() {

   }

   public static MorphClientServiceBuilder newBuilder() {
      return new MorphClientServiceBuilder();
   }

   public <T extends MessageConverter<?>> MorphClientServiceBuilder addMessageConverter(T t) {
      this.addMessageConverter(Collections.singleton(t));
      return this;
   }

   public <T extends MessageConverter<?>> MorphClientServiceBuilder addMessageConverter(Collection<T> t) {
      Asserts.notNull(t, "MessageConverter");
      this.messageConverters.addAll(t);
      return this;
   }

   public MorphClientServiceBuilder addNameResolver(Class<? extends NameResolver> nameResolverClass) {
      Asserts.notNull(nameResolverClass, "NameResolver Class");
      this.nameResolvers.add(nameResolverClass);
      return this;
   }

   public MorphClientServiceBuilder addLoadBalancer(Class<? extends LoadBalancer> loadBalancerClass) {
      Asserts.notNull(loadBalancerClass, "LoadBalanacer Class");
      this.loadBalancers.add(loadBalancerClass);
      return this;
   }

   public MorphClientServiceBuilder addHttpClientProvider(Class<? extends HttpClientProvider> httpClientProvider) {
      Asserts.notNull(httpClientProvider, "HttpClientProvider Class");
      this.httpClientProviders.add(httpClientProvider);
      return this;
   }

   public MorphClientServiceBuilder setPathPropertyResolver(PathPropertyResolver pathPropertyResolver) {
      this.pathPropertyResolver = pathPropertyResolver;
      return this;
   }

   public MorphClientServiceBuilder setHttpClientProviderFactory(HttpClientProviderFactory httpClientProviderFactory) {
      this.httpClientProviderFactory = httpClientProviderFactory;
      return this;
   }

   public MorphClientServiceBuilder setLoadBalancerFactory(LoadBalancerFactory loadBalancerFactory) {
      this.loadBalancerFactory = loadBalancerFactory;
      return this;
   }

   public MorphClientServiceBuilder setNameResolverFactory(NameResolverFactory nameResolverFactory) {
      this.nameResolverFactory = nameResolverFactory;
      return this;
   }

   public MorphClient build() {
      return new MorphClientServiceImpl(this.httpClientProviderFactory, this.loadBalancerFactory, this.nameResolverFactory, this.nameResolvers, this.loadBalancers, this.httpClientProviders, this.messageConverters, this.pathPropertyResolver);
   }
}
