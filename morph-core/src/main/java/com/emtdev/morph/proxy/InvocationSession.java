package com.emtdev.morph.proxy;

import com.emtdev.morph.proxy.invocations.MorphContext;

import java.lang.reflect.Method;

public interface InvocationSession {

   Object invoke(Object callee, Method method, Object[] args) throws Throwable;

   MorphContext getContext();

}
