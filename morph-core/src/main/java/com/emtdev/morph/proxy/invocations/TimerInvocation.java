package com.emtdev.morph.proxy.invocations;

import com.emtdev.morph.proxy.Invocation;
import com.emtdev.morph.proxy.InvocationSession;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Executor;

public class TimerInvocation implements Invocation {

   private Executor executorService;

   public TimerInvocation(Executor executorService) {
      this.executorService = executorService;
   }

   @Override
   public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {
      long start_time = System.nanoTime();
      long end_time;
      boolean hasError = false;
      try {
         chain.invoke(callee, method, args);
      } catch (Exception e) {
         hasError = true;
         throw e;
      } finally {
         end_time = System.nanoTime();
         updateStatistic(method, start_time, end_time, hasError, chain);
      }

      return null;
   }

   private void updateStatistic(Method method, long startTime, long endTime, boolean hasError, InvocationSession chain) {
      System.out.println("Timer: excution took " + (endTime - startTime) / 1e6 + "ms");
      if (Objects.nonNull(chain.getContext().getSelectedAddressGroup()) && Objects.nonNull(chain.getContext().getSelectedLoadBalancer())) {
         executorService.execute(() -> chain.getContext()
                 .getSelectedLoadBalancer()
                 .updateStatistic(method, chain.getContext().getSelectedAddressGroup(), endTime - startTime
                         , hasError));
      }
   }
}
