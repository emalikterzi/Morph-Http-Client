package com.emt.morph.factory;

import com.emt.morph.LoadBalancer;

public interface LoadBalancerFactory {

   LoadBalancer createLoadBalancer(Class<? extends LoadBalancer> loadBalancerClass);

}
