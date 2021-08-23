package com.emt.morph.http.nameresolver;

public class InetSocketAddress {

    private final String hostname;
    private final int port;

    public InetSocketAddress(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }


    public int getPort() {
        return port;
    }


}
