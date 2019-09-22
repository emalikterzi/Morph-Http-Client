package com.emt.morph.impl;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.emt.morph.AbstractListener;
import com.emt.morph.AuthorityListenerProvider;
import com.emt.morph.ListenerImpl;
import com.emt.morph.NameResolver;

public class DefaultAuthorityListenerProvider implements AuthorityListenerProvider
{

    private final List<NameResolver> resolvers;
    private final ConcurrentHashMap<NameResolver, NameResolverAuthorityHolder> nameResolverHolder =
            new ConcurrentHashMap<>();

    public DefaultAuthorityListenerProvider(List<NameResolver> resolvers)
    {
        this.resolvers = resolvers;
    }

    @Override
    public AbstractListener getListener(URI uri)
    {
        NameResolver nameResolver = findSuitableNameResolver(uri);
        if (Objects.nonNull(nameResolver))
        {
            NameResolverAuthorityHolder authorityHolder = nameResolverHolder.get(nameResolver);
            if (Objects.isNull(authorityHolder))
            {
                authorityHolder = new NameResolverAuthorityHolder();
                nameResolverHolder.put(nameResolver, authorityHolder);
            }

            return authorityHolder.getListener(uri);
        }
        return null;
    }

    private NameResolver findSuitableNameResolver(URI uri)
    {
        return resolvers.stream()
                .filter(x -> x.getScheme().equals(uri.getScheme()))
                .sorted(Comparator.comparingInt(NameResolver::priority))
                .findFirst().orElse(null);
    }


    private class NameResolverAuthorityHolder
    {

        ConcurrentHashMap<String, AbstractListener> listenerMap = new ConcurrentHashMap<>();

        AbstractListener getListener(URI uri)
        {
            AbstractListener abstractListener = listenerMap.get(uri.getAuthority());

            if (abstractListener == null)
            {
                abstractListener = new ListenerImpl();
                listenerMap.put(uri.getAuthority(), abstractListener);
            }

            return abstractListener;
        }
    }
}
