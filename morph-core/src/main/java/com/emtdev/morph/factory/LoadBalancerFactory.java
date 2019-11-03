package com.emtdev.morph.factory;

import com.emtdev.morph.LoadBalancer;

public interface LoadBalancerFactory {

   LoadBalancer createLoadBalancer(Class<? extends LoadBalancer> loadBalancerClass);

}
