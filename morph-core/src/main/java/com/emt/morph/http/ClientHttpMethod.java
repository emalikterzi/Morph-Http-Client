package com.emt.morph.http;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;

public enum ClientHttpMethod {

   GET(GET.class),
   POST(POST.class),
   PUT(PUT.class),
   DELETE(DELETE.class);

   private final Class<? extends Annotation> typeClass;

   ClientHttpMethod(Class<? extends Annotation> typeClass) {
      this.typeClass = typeClass;
   }

   public static Optional<ClientHttpMethod> findByClass(Class<?> classs) {
      return Arrays.stream(ClientHttpMethod.values()).filter(x -> x.getTypeClass().equals(classs))
              .findFirst();
   }

   public Class<? extends Annotation> getTypeClass() {
      return typeClass;
   }
}
