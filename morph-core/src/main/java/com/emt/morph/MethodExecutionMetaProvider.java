package com.emt.morph;

import com.emt.morph.meta.ImmutableExecutionMeta;

import java.lang.reflect.Method;

public interface MethodExecutionMetaProvider {

   ImmutableExecutionMeta getMetaData(Method method);
}
