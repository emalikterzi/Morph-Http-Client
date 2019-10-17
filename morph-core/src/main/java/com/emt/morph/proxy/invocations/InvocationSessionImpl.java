package com.emt.morph.proxy.invocations;

import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;

import java.lang.reflect.Method;
import java.util.Iterator;

public class InvocationSessionImpl implements InvocationSession {

   private final Iterator<Invocation> tasks;
   private final MorphContext context;
   private Object result;

   public InvocationSessionImpl(Iterator<Invocation> tasks, Method executionMethod, Object[] args) {
      this.tasks = tasks;
      this.context = new MorphContext(executionMethod, args);
   }

   @Override
   public Object invoke(Object callee, Method method, Object[] args) throws Throwable {

      if (tasks.hasNext()) {
         Object result = tasks.next().invoke(callee, method, args, this);
         this.result = (this.result == null ? result : this.result);
      }

      return this.result;
   }

   @Override
   public MorphContext getContext() {
      return context;
   }


}
