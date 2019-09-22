package com.emt.morph.proxy.invocations;

import com.emt.morph.meta.ImmutableExecutionMeta;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpRequestBase;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.UUID;

public final class MorphContext {

   private final String uuid;
   private final Method executionMethod;
   private final Object[] args;

   private URI uri;
   private URI selectedUri;
   private HttpRequestBase httpRequestBase;
   private boolean bodyNeeded = false;

   MorphContext(Method executionMethod, Object[] args) {
      this.executionMethod = executionMethod;
      this.args = args;
      this.uuid = UUID.randomUUID().toString();
   }

   private ImmutableExecutionMeta immutableExecutionMeta;

   public ImmutableExecutionMeta getImmutableExecutionMeta() {
      return immutableExecutionMeta;
   }

   public void setImmutableExecutionMeta(ImmutableExecutionMeta immutableExecutionMeta) {
      this.immutableExecutionMeta = immutableExecutionMeta;
   }

   public URI getUri() {
      return uri;
   }

   protected void setUri(URI uri) {
      this.uri = uri;
   }

   public URI getSelectedUri() {
      return selectedUri;
   }

   protected void setSelectedUri(URI selectedUri) {
      this.selectedUri = selectedUri;
   }

   public HttpRequestBase getHttpRequestBase() {
      return httpRequestBase;
   }

   public boolean isBodyNeeded() {
      return bodyNeeded;
   }

   public String getUuid() {
      return uuid;
   }

   protected void setHttpRequestBase(HttpRequestBase httpRequestBase) {
      this.httpRequestBase = httpRequestBase;
      if (httpRequestBase instanceof HttpEntityEnclosingRequest)
         this.bodyNeeded = true;
   }
}
