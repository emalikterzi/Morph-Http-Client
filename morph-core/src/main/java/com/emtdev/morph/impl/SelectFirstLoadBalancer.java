package com.emtdev.morph.impl;

import com.emtdev.morph.ImmutableRemoteAddressGroup;
import com.emtdev.morph.LoadBalancer;

import java.lang.reflect.Method;
import java.util.Iterator;

public class SelectFirstLoadBalancer implements LoadBalancer {

   @Override
   public ImmutableRemoteAddressGroup select(Iterator<ImmutableRemoteAddressGroup> inetSocketAddressList) {
      return inetSocketAddressList.hasNext() ? inetSocketAddressList.next() : null;
   }

   @Override
   public void updateStatistic(Method method, ImmutableRemoteAddressGroup selectedAddress, long executionTime, boolean hasError) {
      System.out.println("");
   }

}
