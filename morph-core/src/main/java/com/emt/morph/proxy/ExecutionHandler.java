package com.emt.morph.proxy;

import com.emt.morph.config.InvocationContextConfig;
import com.emt.morph.proxy.invocations.InvocationSessionImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class ExecutionHandler implements InvocationHandler {

    private static Class<?> handlerClass = ExecutionHandler.class;
    private final List<Invocation> invocationList;
    private final InvocationContextConfig config;

    public ExecutionHandler(List<Invocation> invocationList, InvocationContextConfig config) {
        this.invocationList = invocationList;
        this.config = config;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        /**
         * important this method important for debugging , debugging panel calling toString method
         */
        if (method.getName().equals("toString") && method.getDeclaringClass().getName().equals("java.lang.Object")) {
            return handlerClass.getName() + "#com.sun.proxy.$Proxy10";
        }

        return new InvocationSessionImpl(config, invocationList.iterator()).invoke(proxy, method, args);

    }

}
