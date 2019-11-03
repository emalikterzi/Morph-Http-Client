package com.emtdev.morph.converter;


import com.emtdev.morph.exception.MessageConverterException;
import com.emtdev.morph.meta.ImmutableParameterMeta;
import com.emtdev.morph.utils.ParameterMetaUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AbstractJsonMessageConverter implements MessageConverter {

   private String[] MEDIA_TYPES = {"application/json"};

   protected abstract ObjectMapper getObjectMapper();

   @Override
   public String[] getSupportedMediaTypes() {
      return MEDIA_TYPES;
   }

   @Override
   public Object read(Method method, InputStream responseBody) throws MessageConverterException {
      Type type = method.getGenericReturnType();
      JavaType javaType = getObjectMapper().getTypeFactory()
              .constructType(type);
      try {
         return getObjectMapper().readValue(responseBody, javaType);
      } catch (IOException e) {
         throw new MessageConverterException(e);
      }
   }


   @Override
   public HttpEntity write(List<ImmutableParameterMeta> immutableParameterMetas,
                           ContentType contentType, Object... args) throws MessageConverterException {
      try {
         Object requestBody = ParameterMetaUtils.findBody(immutableParameterMetas, args);
         String bodyAsStr = getObjectMapper().writeValueAsString(requestBody);
         StringEntity stringEntity = new StringEntity(bodyAsStr, contentType);
         return stringEntity;
      } catch (Exception e) {
         throw new MessageConverterException(e);
      }
   }

}
