package com.emt.morph.proxy.invocations;

import com.emt.morph.http.nameresolver.InetSocketAddress;
import com.emt.morph.loadbalancer.LoadBalancer;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;

import java.lang.reflect.Method;
import java.net.URI;

public class LoadBalancerInvocation implements Invocation {

    private final LoadBalancer loadBalancer;

    public LoadBalancerInvocation(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {

        if (loadBalancer == null) {
            buildForDefault(chain);
        } else {

            if (chain.getContext().getInetSocketAddresses().isEmpty()) {
                buildForDefault(chain);
            } else {
                InetSocketAddress inetSocketAddress = loadBalancer.select(chain.getContext().getInetSocketAddresses());
                buildForAddress(chain, inetSocketAddress);
            }
        }
        chain.invoke(callee, method, args);
        return null;
    }

    private void buildForDefault(InvocationSession invocationSession) {
        invocationSession.getContext().setUri(URI.create(invocationSession.getContext().getSanitizedUrl()));
    }

    private void buildForAddress(InvocationSession invocationSession, InetSocketAddress inetSocketAddress) {

    }

}
