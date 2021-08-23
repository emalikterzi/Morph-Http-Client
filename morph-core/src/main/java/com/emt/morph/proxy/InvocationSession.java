package com.emt.morph.proxy;

import java.lang.reflect.Method;

public interface InvocationSession {

    Object invoke(Object callee, Method method, Object[] args) throws Throwable;

    InvocationContext getContext();

}
