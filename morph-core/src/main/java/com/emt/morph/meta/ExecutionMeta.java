package com.emt.morph.meta;

import com.emt.morph.http.ClientHttpMethod;

import java.util.List;
import java.util.Objects;

public class ExecutionMeta {

   private String path;
   private ExecutionMeta parent;
   private ClientHttpMethod clientHttpMethod;
   private List<ImmutableParameterMeta> methodParameterMeta;
   private String consumes;
   private String produces;

   public String getPath() {
      if (Objects.isNull(path))
         return "";
      return path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public ExecutionMeta getParent() {
      return parent;
   }

   public void setParent(ExecutionMeta parent) {
      this.parent = parent;
   }

   public ClientHttpMethod getClientHttpMethod() {
      return clientHttpMethod;
   }

   public void setClientHttpMethod(ClientHttpMethod clientHttpMethod) {
      this.clientHttpMethod = clientHttpMethod;
   }

   public List<ImmutableParameterMeta> getMethodParameterMeta() {
      return methodParameterMeta;
   }

   public void setMethodParameterMeta(List<ImmutableParameterMeta> methodParameterMeta) {
      this.methodParameterMeta = methodParameterMeta;
   }

   public String getConsumes() {
      return consumes;
   }

   public void setConsumes(String consumes) {
      this.consumes = consumes;
   }

   public String getProduces() {
      return produces;
   }

   public void setProduces(String produces) {
      this.produces = produces;
   }
}
