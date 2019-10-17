package com.emt.morph;

import java.util.Iterator;

public abstract class AbstractListener implements Listener {

   private boolean started;

   public abstract Iterator<ImmutableRemoteAddressGroup> getRemoteAddressGroups();


   public boolean isStarted() {
      return started;
   }


   public void start() {
      this.started = true;
   }
}
