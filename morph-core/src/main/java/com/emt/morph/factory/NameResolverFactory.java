package com.emt.morph.factory;

import com.emt.morph.NameResolver;

public interface NameResolverFactory {

   NameResolver createNameResolver(Class<? extends NameResolver> nameResolverClass);

}
