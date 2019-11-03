package com.emtdev.morph.proxy.invocations;

import com.emtdev.morph.proxy.Invocation;
import com.emtdev.morph.proxy.InvocationSession;

import java.lang.reflect.Method;
import java.util.Iterator;

public class InvocationSessionImpl implements InvocationSession {

   private final Iterator<Invocation> tasks;
   private final MorphContext context;
   private Object result;

   public InvocationSessionImpl(Iterator<Invocation> tasks) {
      this.tasks = tasks;
      this.context = new MorphContext();
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
