package com.emt.morph;

import com.emt.morph.converter.MessageConverter;
import com.emt.morph.impl.DefaultHttpClientProvider;
import com.emt.morph.impl.DefaultSystemEnvironmentPropertyResolver;
import com.emt.morph.http.HttpClientProvider;

import java.util.ArrayList;
import java.util.List;

public class MorphClientServiceBuilder {

   private boolean logInvocationTime = false;
   private boolean debug = false;
   private LoadBalancer loadBalancer;
   private List<MessageConverter<?>> messageConverters = new ArrayList<>();
   private HttpClientProvider httpClientProvider = new DefaultHttpClientProvider();
   private PathPropertyResolver pathPropertyResolver = new DefaultSystemEnvironmentPropertyResolver();
   private List<NameResolver> nameResolvers = new ArrayList<>();

   private MorphClientServiceBuilder() {
   }

   public MorphClientServiceBuilder logInvocationTime() {
      this.logInvocationTime = true;
      return this;
   }

   public MorphClientServiceBuilder debug() {
      this.debug = true;
      return this;
   }

   public MorphClientServiceBuilder setLoadBalancer(LoadBalancer loadBalancer) {
      this.loadBalancer = loadBalancer;
      return this;
   }

   public MorphClientServiceBuilder setMessageConverters(List<MessageConverter<?>> messageConverters) {
      this.messageConverters = messageConverters;
      return this;
   }

   public MorphClientServiceBuilder setHttpClientProvider(HttpClientProvider httpClientProvider) {
      this.httpClientProvider = httpClientProvider;
      return this;
   }

   public MorphClientServiceBuilder setPathPropertyResolver(PathPropertyResolver pathPropertyResolver) {
      this.pathPropertyResolver = pathPropertyResolver;
      return this;
   }

   public MorphClientServiceBuilder setNameResolvers(List<NameResolver> nameResolvers) {
      this.nameResolvers = nameResolvers;
      return this;
   }

   public static MorphClientServiceBuilder newBuilder() {
      return new MorphClientServiceBuilder();
   }

   public MorphClient build() {
      return new MorphClientServiceImpl(logInvocationTime, debug, loadBalancer, messageConverters, httpClientProvider, pathPropertyResolver, nameResolvers);
   }
}
