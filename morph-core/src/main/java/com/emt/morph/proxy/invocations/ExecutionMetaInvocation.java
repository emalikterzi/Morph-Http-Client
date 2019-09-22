package com.emt.morph.proxy.invocations;

import com.emt.morph.MethodExecutionMetaProvider;
import com.emt.morph.meta.ImmutableExecutionMeta;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;

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
