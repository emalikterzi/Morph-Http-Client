package com.zookeeper.bandit;

import com.emtdev.morph.LoadBalancer;
import com.emtdev.morph.bandit.Ucb1Algorithm;
import com.emtdev.morph.bandit.loadbalancer.BanditLoadBalancer;
import com.emtdev.morph.factory.LoadBalancerFactory;

public class BanditLoadBalancerFactory implements LoadBalancerFactory {

   @Override
   public LoadBalancer createLoadBalancer(Class<? extends LoadBalancer> loadBalancerClass) {
      return new BanditLoadBalancer(new Ucb1Algorithm());
   }


}
