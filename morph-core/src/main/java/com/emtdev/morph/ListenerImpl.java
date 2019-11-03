package com.emtdev.morph;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class ListenerImpl extends AbstractListener {

   private final Object monitorObject = new Object();
   private final AtomicBoolean isLocked = new AtomicBoolean(false);

   private List<ImmutableRemoteAddressGroup> remoteAddressGroups;

   @Override
   public Iterator<ImmutableRemoteAddressGroup> getRemoteAddressGroups() {
      synchronized (monitorObject) {
         if (remoteAddressGroups == null) {
            isLocked.set(true);
            try {
               monitorObject.wait();
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }

         if (isLocked.get()) {
            try {
               monitorObject.wait();
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }

         return remoteAddressGroups.iterator();
      }

   }


   @Override
   public void refresh(List<ImmutableRemoteAddressGroup> remoteAddressGroup) {
      synchronized (monitorObject) {
         isLocked.set(true);
         if (Objects.nonNull(remoteAddressGroup) && !remoteAddressGroup.isEmpty()) {
            this.remoteAddressGroups = remoteAddressGroup;
            isLocked.set(false);
            monitorObject.notifyAll();
         }
      }
   }
}
