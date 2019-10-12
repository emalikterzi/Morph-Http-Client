package com.emt.morph.impl;

import com.emt.morph.ImmutableRemoteAddressGroup;
import com.emt.morph.LoadBalancer;

import java.util.Iterator;

public class SelectFirstLoadBalancer implements LoadBalancer {

   @Override
   public ImmutableRemoteAddressGroup select(Iterator<ImmutableRemoteAddressGroup> inetSocketAddressList) {
      return inetSocketAddressList.hasNext() ? inetSocketAddressList.next() : null;
   }

}
