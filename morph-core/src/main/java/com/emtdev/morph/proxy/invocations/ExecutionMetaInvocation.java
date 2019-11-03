package com.emtdev.morph.proxy.invocations;

import com.emtdev.morph.MethodExecutionMetaProvider;
import com.emtdev.morph.meta.ImmutableExecutionMeta;
import com.emtdev.morph.proxy.Invocation;
import com.emtdev.morph.proxy.InvocationSession;

import java.lang.reflect.Method;

public class ExecutionMetaInvocation implements Invocation {

   private final MethodExecutionMetaProvider methodExecutionMetaProvider;

   public ExecutionMetaInvocation(MethodExecutionMetaProvider methodExecutionMetaProvider) {
      this.methodExecutionMetaProvider = methodExecutionMetaProvider;
   }

   @Override
   public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {
      ImmutableExecutionMeta immutableExecutionMeta = methodExecutionMetaProvider.getMetaData(method);
      chain.getContext().setImmutableExecutionMeta(immutableExecutionMeta);
      chain.invoke(callee, method, args);
      return null;
   }

}
