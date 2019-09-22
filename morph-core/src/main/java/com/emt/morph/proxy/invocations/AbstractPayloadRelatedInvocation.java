package com.emt.morph.proxy.invocations;

import com.emt.morph.converter.MessageConverter;
import org.apache.http.entity.ContentType;

import java.util.List;
import java.util.Optional;

public abstract class AbstractPayloadRelatedInvocation {

   private final List<MessageConverter<?>> converters;

   public AbstractPayloadRelatedInvocation(List<MessageConverter<?>> converters) {
      this.converters = converters;
   }

   protected MessageConverter<?> findSuitableConverter(ContentType contentType) {
      Optional<MessageConverter<?>> messageConverter
              = converters.stream().filter(x -> x.getSupportedMediaType()
              .equals(contentType.getMimeType()))
              .findFirst();

      if (messageConverter.isPresent())
         return messageConverter.get();

      //todo fix proper error
      throw new RuntimeException("");

   }
}
