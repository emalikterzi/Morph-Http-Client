package com.emt.morph.meta;

import com.emt.morph.LoadBalancer;
import com.emt.morph.http.ClientHttpMethod;
import com.emt.morph.http.HttpClientProvider;

import java.util.List;

public class ImmutableExecutionMeta {

   private final String path;
   private final ClientHttpMethod clientHttpMethod;
   private final List<ImmutableParameterMeta> methodParameterMeta;
   private final String consumes;
   private final String produces;
   private final Class<? extends LoadBalancer> loadBalancer;
   private final Class<? extends HttpClientProvider> httpClientProvide;

   public ImmutableExecutionMeta(String path, ClientHttpMethod clientHttpMethod, List<ImmutableParameterMeta> methodParameterMeta, String consumes, String produces, Class<? extends LoadBalancer> loadBalancer, Class<? extends HttpClientProvider> httpClientProvide) {
      this.path = path;
      this.clientHttpMethod = clientHttpMethod;
      this.methodParameterMeta = methodParameterMeta;
      this.consumes = consumes;
      this.produces = produces;
      this.loadBalancer = loadBalancer;
      this.httpClientProvide = httpClientProvide;
   }

   public Class<? extends LoadBalancer> getLoadBalancer() {
      return loadBalancer;
   }

   public Class<? extends HttpClientProvider> getHttpClientProvide() {
      return httpClientProvide;
   }

   public String getPath() {
      return path;
   }

   public ClientHttpMethod getClientHttpMethod() {
      return clientHttpMethod;
   }

   public List<ImmutableParameterMeta> getMethodParameterMeta() {
      return methodParameterMeta;
   }

   public String getConsumes() {
      return consumes;
   }

   public String getProduces() {
      return produces;
   }
}
