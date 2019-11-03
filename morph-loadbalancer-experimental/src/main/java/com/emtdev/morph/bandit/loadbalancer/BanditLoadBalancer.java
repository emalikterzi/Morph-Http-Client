package com.emtdev.morph.bandit.loadbalancer;

import com.emtdev.morph.ImmutableRemoteAddressGroup;
import com.emtdev.morph.LoadBalancer;
import com.emtdev.morph.bandit.BanditAlgorithm;
import com.emtdev.morph.bandit.arms.ServerAvgResponseTime;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BanditLoadBalancer implements LoadBalancer {

   private final BanditAlgorithm banditAlgorithm;

   public BanditLoadBalancer(BanditAlgorithm banditAlgorithm) {
      this.banditAlgorithm = banditAlgorithm;
   }

   private List<ServerAvgResponseTime> algorithmArms = new ArrayList<>();


   @Override
   public ImmutableRemoteAddressGroup select(Iterator<ImmutableRemoteAddressGroup> inetSocketAddressList) {
      List<ServerAvgResponseTime> currentArms = new ArrayList<>();

      while (inetSocketAddressList.hasNext()) {
         currentArms.add(new ServerAvgResponseTime(inetSocketAddressList.next()));
      }

      if (!algorithmArms.equals(currentArms)) {
         banditAlgorithm.renewArms(currentArms.size());
         algorithmArms = currentArms;
      }

      int selectedArm = banditAlgorithm.selectArm();
      ServerAvgResponseTime serverAvgResponseTime = algorithmArms.get(selectedArm);
      double reward = serverAvgResponseTime.draw();
      banditAlgorithm.update(selectedArm, reward);

      return serverAvgResponseTime.getAddressGroup();
   }

   @Override
   public void updateStatistic(Method method, ImmutableRemoteAddressGroup selectedAddress, long executionTime, boolean hasError) {
      System.out.println("");
   }


}
