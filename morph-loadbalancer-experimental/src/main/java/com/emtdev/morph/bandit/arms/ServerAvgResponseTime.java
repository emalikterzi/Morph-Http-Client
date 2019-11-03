package com.emtdev.morph.bandit.arms;

import com.emtdev.morph.ImmutableRemoteAddressGroup;

import java.util.Objects;

public class ServerAvgResponseTime implements Arm {

   private final ImmutableRemoteAddressGroup addressGroup;

   public ServerAvgResponseTime(ImmutableRemoteAddressGroup addressGroup) {
      this.addressGroup = addressGroup;
   }

   @Override
   public double draw() {
      return 0;
   }

   public ImmutableRemoteAddressGroup getAddressGroup() {
      return addressGroup;
   }


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ServerAvgResponseTime that = (ServerAvgResponseTime) o;
      return Objects.equals(addressGroup, that.addressGroup);
   }

   @Override
   public int hashCode() {
      return Objects.hash(addressGroup);
   }
}
