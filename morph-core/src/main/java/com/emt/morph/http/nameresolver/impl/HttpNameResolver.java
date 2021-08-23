package com.emt.morph.http.nameresolver.impl;

import com.emt.morph.http.nameresolver.InetSocketAddress;
import com.emt.morph.http.nameresolver.NameResolver;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public class HttpNameResolver implements NameResolver {

    @Override
    public List<InetSocketAddress> resolveAvailable(URI uri) {
        return Collections.EMPTY_LIST;
    }

}
