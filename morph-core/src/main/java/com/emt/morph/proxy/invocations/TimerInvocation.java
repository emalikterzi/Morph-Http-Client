package com.emt.morph.proxy.invocations;

import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;

import java.lang.reflect.Method;

public class TimerInvocation implements Invocation {

   private boolean isActive;

   public TimerInvocation(boolean isActive) {
      this.isActive = isActive;
   }

   @Override
   public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {
      if (isActive) {
         long start_time = System.nanoTime();
         chain.invoke(callee, method, args);
         long end_time = System.nanoTime();
         System.out.println("Timer: excution took " + (end_time - start_time) / 1e6 + "ms");
      } else {
         chain.invoke(callee, method, args);
      }
      return null;
   }
}
