package com.emtdev.morph;

import com.emtdev.morph.meta.ImmutableExecutionMeta;

import java.lang.reflect.Method;

public interface MethodExecutionMetaProvider {

   ImmutableExecutionMeta getMetaData(Method method);
}
