package com.emt.morph;

import com.emt.morph.meta.ImmutableExecutionMeta;

import java.lang.reflect.Method;
import java.util.Arrays;

public class LibraryPreInitializer implements MethodExecutionMetaProvider {

   private final MethodExecutionMetaProvider methodExecutionMetaProviderDelegate;

   LibraryPreInitializer(MethodExecutionMetaProvider delegate) {
      this.methodExecutionMetaProviderDelegate = delegate;
   }

   @Override
   public ImmutableExecutionMeta getMetaData(Method method) {
      return methodExecutionMetaProviderDelegate.getMetaData(method);
   }

   void initializeAndValidateMetaData(Class<?> tClass) {
      Arrays.stream(tClass.getDeclaredMethods())
              .forEach(x -> validate(methodExecutionMetaProviderDelegate.getMetaData(x)));
   }

   private void validate(ImmutableExecutionMeta immutableExecutionMeta) {
      //todo should be validate method annotations
   }
}
