package com.emt.morph.proxy;

import com.emt.morph.proxy.invocations.MorphContext;

import java.lang.reflect.Method;

public interface InvocationSession {

   Object invoke(Object callee, Method method, Object[] args) throws Throwable;

   MorphContext getContext();

}
