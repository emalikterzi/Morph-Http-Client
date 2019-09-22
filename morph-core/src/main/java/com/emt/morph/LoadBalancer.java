package com.emt.morph;

import java.util.Iterator;

public interface LoadBalancer
{

    ImmutableRemoteAddressGroup select(Iterator<ImmutableRemoteAddressGroup> inetSocketAddressList);

}
