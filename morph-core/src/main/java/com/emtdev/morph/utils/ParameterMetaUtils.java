package com.emtdev.morph.utils;

import com.emtdev.morph.meta.ImmutableParameterMeta;

import java.util.List;
import java.util.Optional;

public class ParameterMetaUtils {


   public static Object findBody(List<ImmutableParameterMeta> immutableParameterMetaList, Object... args) {

      Optional<ImmutableParameterMeta> immutableParameterMeta = immutableParameterMetaList
              .stream().filter(x -> x.getMetaType().equals(ImmutableParameterMeta.MetaType.BODY))
              .findFirst();


      if (!immutableParameterMeta.isPresent())
         return null;

      Object body = args[immutableParameterMeta.get().getIndex()];
      return body;
   }
}
