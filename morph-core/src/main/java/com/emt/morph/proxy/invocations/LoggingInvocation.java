package com.emt.morph.proxy.invocations;

import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public class LoggingInvocation implements Invocation {

   private final static Logger logger = Logger.getLogger(LoggingInvocation.class.getName());

   private final boolean isActive;

   public LoggingInvocation(boolean isActive) {
      this.isActive = isActive;
   }

   @Override
   public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {
      if (isActive) {
         try {
            chain.invoke(callee, method, args);
            logger.info(method.getName() + "() executed ");
         } catch (Exception e) {
            logger.severe(method.getName() + "() Failed ");
            throw e;
         }
      }
      return null;
   }
}
