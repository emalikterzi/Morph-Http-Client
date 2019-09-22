package com.emt.morph.meta;

import com.emt.morph.http.ClientHttpMethod;

import java.util.List;

public class ImmutableExecutionMeta {

   private final String path;
   private final ClientHttpMethod clientHttpMethod;
   private final List<ImmutableParameterMeta> methodParameterMeta;
   private final String consumes;
   private final String produces;

   public ImmutableExecutionMeta(String path, ClientHttpMethod clientHttpMethod, List<ImmutableParameterMeta> methodParameterMeta, String consumes, String produces) {
      this.path = path;
      this.clientHttpMethod = clientHttpMethod;
      this.methodParameterMeta = methodParameterMeta;
      this.consumes = consumes;
      this.produces = produces;
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
