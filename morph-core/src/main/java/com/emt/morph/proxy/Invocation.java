package com.emt.morph.proxy;

import java.lang.reflect.Method;

public interface Invocation {

   Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable;

}
