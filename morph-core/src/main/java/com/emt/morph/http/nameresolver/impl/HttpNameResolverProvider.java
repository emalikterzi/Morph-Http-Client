package com.emt.morph.http.nameresolver.impl;

import com.emt.morph.http.nameresolver.NameResolverProvider;

public class HttpNameResolverProvider implements NameResolverProvider<HttpNameResolver> {

    public static HttpNameResolverProvider INSTANCE = new HttpNameResolverProvider();

    public static String[] SCHEME = {"http", "https"};
    private final HttpNameResolver httpNameResolver = new HttpNameResolver();

    @Override
    public HttpNameResolver nameResolver() {
        return httpNameResolver;
    }

    @Override
    public String[] schemes() {
        return SCHEME;
    }

}
