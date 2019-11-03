package com.emtdev.morph.zookeeper.resolver;

import com.emtdev.morph.ImmutableRemoteAddressGroup;
import com.emtdev.morph.ImmutableRemoteAddressGroup.Scheme;

public class RemoteAddressGroup {
   private Scheme scheme = Scheme.http;
   private String host;
   private int port;

   public Scheme getScheme() {
      return scheme;
   }

   public void setScheme(Scheme scheme) {
      this.scheme = scheme;
   }

   public String getHost() {
      return host;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public int getPort() {
      return port;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public ImmutableRemoteAddressGroup toImmutableRemoteAddressGroup() {
      return new ImmutableRemoteAddressGroup(this.scheme, this.host, this.port);
   }
}
