package com.emt.morph;

import java.util.Iterator;

public abstract class AbstractListener implements Listener
{

    public abstract Iterator<ImmutableRemoteAddressGroup> getRemoteAddressGroups();

}
