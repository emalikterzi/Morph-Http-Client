package com.emt.morph.loadbalancer;

import com.emt.morph.http.nameresolver.InetSocketAddress;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    @Override
    public InetSocketAddress select(Collection<InetSocketAddress> inetSocketAddresses) {
        if (inetSocketAddresses == null || inetSocketAddresses.isEmpty()) {
            return null;
        }
        List<InetSocketAddress> set = new ArrayList<>(inetSocketAddresses);
        return set.get(random.nextInt(set.size()));
    }
}
