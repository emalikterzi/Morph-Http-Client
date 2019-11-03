package com.emtdev.morph.proxy.invocations;

import com.emtdev.morph.ImmutableRemoteAddressGroup;
import com.emtdev.morph.LoadBalancer;
import com.emtdev.morph.meta.ImmutableExecutionMeta;
import org.apache.http.client.methods.HttpRequestBase;

import java.net.URI;

public final class MorphContext {

   private URI uri;
   private HttpRequestBase httpRequestBase;
   private ImmutableExecutionMeta immutableExecutionMeta;
   private LoadBalancer selectedLoadBalancer;
   private ImmutableRemoteAddressGroup selectedAddressGroup;


   ImmutableExecutionMeta getImmutableExecutionMeta() {
      return immutableExecutionMeta;
   }

   void setImmutableExecutionMeta(ImmutableExecutionMeta immutableExecutionMeta) {
      this.immutableExecutionMeta = immutableExecutionMeta;
   }

   URI getUri() {
      return uri;
   }

   void setUri(URI uri) {
      this.uri = uri;
   }


   HttpRequestBase getHttpRequestBase() {
      return httpRequestBase;
   }

   void setHttpRequestBase(HttpRequestBase httpRequestBase) {
      this.httpRequestBase = httpRequestBase;

   }

   public LoadBalancer getSelectedLoadBalancer() {
      return selectedLoadBalancer;
   }

   public void setSelectedLoadBalancer(LoadBalancer selectedLoadBalancer) {
      this.selectedLoadBalancer = selectedLoadBalancer;
   }

   public ImmutableRemoteAddressGroup getSelectedAddressGroup() {
      return selectedAddressGroup;
   }

   public void setSelectedAddressGroup(ImmutableRemoteAddressGroup selectedAddressGroup) {
      this.selectedAddressGroup = selectedAddressGroup;
   }
}
