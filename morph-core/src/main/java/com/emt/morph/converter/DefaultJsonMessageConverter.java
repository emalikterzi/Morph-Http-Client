package com.emt.morph.converter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

public class DefaultJsonMessageConverter extends AbstractJsonMessageConverter {

   private ObjectMapper mapper;

   @Override
   protected ObjectMapper getObjectMapper() {
      if (Objects.nonNull(mapper))
         return mapper;

      mapper = new ObjectMapper();
      mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
      return mapper;
   }
}
