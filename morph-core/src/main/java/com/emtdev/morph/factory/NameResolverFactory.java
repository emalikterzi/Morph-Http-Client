package com.emtdev.morph.factory;

import com.emtdev.morph.NameResolver;

public interface NameResolverFactory {

   NameResolver createNameResolver(Class<? extends NameResolver> nameResolverClass);

}
