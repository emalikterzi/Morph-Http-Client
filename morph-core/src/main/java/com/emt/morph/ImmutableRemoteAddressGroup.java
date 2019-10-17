package com.emt.morph;

public class ImmutableRemoteAddressGroup {

   private final Scheme scheme;
   private final String host;
   private final int port;

   public ImmutableRemoteAddressGroup(String host, int port) {
      this(Scheme.http, host, port);
   }

   public ImmutableRemoteAddressGroup(Scheme scheme, String host, int port) {
      this.scheme = scheme;
      this.host = host;
      this.port = port;
   }

   public Scheme getScheme() {
      return scheme;
   }

   public String getHost() {
      return host;
   }

   public int getPort() {
      return port;
   }

   public enum Scheme {
      http, https
   }
}
