package com.emt.morph.loadbalancer;

import com.emt.morph.http.nameresolver.InetSocketAddress;

import java.util.Collection;

public interface LoadBalancer {

    InetSocketAddress select(Collection<InetSocketAddress> inetSocketAddresses);

}
