package com.emt.morph.zookeeper;

import com.emt.morph.ImmutableRemoteAddressGroup;
import com.emt.morph.LoadBalancer;

import java.util.Iterator;

public class CustomLoadBalancer implements LoadBalancer {

   @Override
   public ImmutableRemoteAddressGroup select(Iterator<ImmutableRemoteAddressGroup> inetSocketAddressList) {
      return inetSocketAddressList.next();
   }

}
