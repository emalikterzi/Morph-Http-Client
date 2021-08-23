package com.emt.morph.proxy.invocations;

import com.emt.morph.http.nameresolver.InetSocketAddress;
import com.emt.morph.http.nameresolver.NameResolverProvider;
import com.emt.morph.http.nameresolver.impl.HttpNameResolverProvider;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;
import com.emt.morph.utils.StringUtils;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NameResolverSelector implements Invocation {

    private final List<NameResolverProvider<?>> nameResolverProviders;

    public NameResolverSelector(List<NameResolverProvider<?>> nameResolverProviders) {
        this.nameResolverProviders = nameResolverProviders;
    }

    @Override
    public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {
        URI uri = URI.create(chain.getContext().getSanitizedUrl());

        NameResolverProvider<?> nameResolverProvider;

        if (StringUtils.isEmpty(uri.getScheme())) {
            nameResolverProvider = HttpNameResolverProvider.INSTANCE;
        } else {
            nameResolverProvider = nameResolverProviders.stream()
                    .filter(x -> Arrays.stream(x.schemes()).anyMatch(s -> s.equals(uri.getScheme())))
                    .findFirst().orElse(HttpNameResolverProvider.INSTANCE);
        }

        List<InetSocketAddress> inetSocketAddresses;
        try {
            inetSocketAddresses = nameResolverProvider.nameResolver()
                    .resolveAvailable(uri);
        } catch (Exception e) {
            inetSocketAddresses = Collections.EMPTY_LIST;
        }

        chain.getContext().setInetSocketAddresses(inetSocketAddresses);

        chain.invoke(callee, method, args);
        return null;
    }
}
