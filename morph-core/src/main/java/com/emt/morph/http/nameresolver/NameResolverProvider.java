package com.emt.morph.http.nameresolver;

public interface NameResolverProvider<T extends NameResolver> {

    T nameResolver();

    String[] schemes();

}
