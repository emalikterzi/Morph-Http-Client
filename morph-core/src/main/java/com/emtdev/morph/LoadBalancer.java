package com.emtdev.morph;

import com.emtdev.morph.api.NotThreadSafe;

import java.lang.reflect.Method;
import java.util.Iterator;

public interface LoadBalancer {

   ImmutableRemoteAddressGroup select(Iterator<ImmutableRemoteAddressGroup> remoteAddressGroup);

   @NotThreadSafe
   void updateStatistic(Method method, ImmutableRemoteAddressGroup selectedAddress, long executionTime, boolean hasError);

}
