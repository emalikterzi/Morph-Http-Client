package com.emtdev.morph.proxy.invocations;

import com.emtdev.morph.converter.MessageConverter;
import com.emtdev.morph.exception.MorphException;
import org.apache.http.entity.ContentType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

abstract class AbstractPayloadRelatedInvocation {

   private final List<MessageConverter> converters;

   AbstractPayloadRelatedInvocation(List<MessageConverter> converters) {
      this.converters = converters;
   }

   MessageConverter findSuitableConverter(ContentType contentType) {
      Optional<MessageConverter> messageConverter
              = converters.stream().filter(x -> Arrays.stream(x.getSupportedMediaTypes())
              .anyMatch(xIn -> xIn.equals(contentType.getMimeType())))
              .findFirst();

      if (messageConverter.isPresent())
         return messageConverter.get();

      throw new MorphException(String.format("could\'t find suitable converter for request payload : [ %s ]",
              contentType.getMimeType()));

   }
}
