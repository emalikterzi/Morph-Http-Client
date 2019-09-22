package com.emt.morph.converter;

import com.emt.morph.exception.MessageConverterException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

public abstract class AbstractJsonMessageConverter implements MessageConverter {


   protected abstract ObjectMapper getObjectMapper();


   @Override
   public String getSupportedMediaType() {
      return "application/json";
   }

   @Override
   public Object read(InputStream body, ContentType contentType, Type type, Class tClass) throws MessageConverterException {
      JavaType javaType = getObjectMapper().getTypeFactory()
              .constructType(type);
      try {
         return getObjectMapper().readValue(body, javaType);
      } catch (IOException e) {
         throw new MessageConverterException(e);
      }
   }

   @Override
   public HttpEntity write(Object body, ContentType contentType, Class tclass) throws MessageConverterException {
      try {
         String bodyAsStr = getObjectMapper().writeValueAsString(body);
         StringEntity stringEntity = new StringEntity(bodyAsStr, contentType);
         return stringEntity;
      } catch (Exception e) {
         throw new MessageConverterException(e);
      }
   }
}
