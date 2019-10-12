package com.emt.morph.proxy;

import com.emt.morph.http.ClientHttpMethod;
import com.emt.morph.proxy.invocations.InvocationSessionImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExecutionHandler implements InvocationHandler {

   private static Class<?> handlerClass = ExecutionHandler.class;
   private final List<Invocation> invocationList;
   private final Set<Class<? extends Annotation>> clientAnnotations;

   public ExecutionHandler(List<Invocation> invocationList) {
      this.invocationList = invocationList;
      this.clientAnnotations = Arrays.stream(ClientHttpMethod.values())
              .map(ClientHttpMethod::getTypeClass)
              .collect(Collectors.toSet());
   }

   @Override
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (hasClientAnnotation(method))
         return new InvocationSessionImpl(invocationList.iterator(), method, args)
                 .invoke(proxy, method, args);

      /**
       * important this method important for debugging , debugging panel calling toString method
       */
      if (method.getName().equals("toString") &&
              method.getDeclaringClass().getName().equals("java.lang.Object")) {
         return handlerClass.getName() + "#com.sun.proxy.$Proxy10";
      }

      return method.invoke(proxy, method, args);
   }

   private boolean hasClientAnnotation(Method method) {
      return Arrays.stream(method.getAnnotations())
              .anyMatch(x -> clientAnnotations.contains(x.annotationType()));
   }

}
