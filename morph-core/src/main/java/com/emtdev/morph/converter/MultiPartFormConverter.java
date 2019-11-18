package com.emtdev.morph.converter;

import com.emtdev.morph.exception.MessageConverterException;
import com.emtdev.morph.meta.ImmutableParameterMeta;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

public class MultiPartFormConverter implements MessageConverter {

   private final String[] mediaTypes = {"multipart/form-data"};
   private final List<MessageConverter> messageConverters;

   public MultiPartFormConverter(List<MessageConverter> messageConverters) {
      this.messageConverters = messageConverters;
   }

   @Override
   public String[] getSupportedMediaTypes() {
      return mediaTypes;
   }

   @Override
   public Object read(Method method, InputStream responseBody) throws MessageConverterException {
      throw new MessageConverterException("not supported");
   }

   @Override
   public HttpEntity write(List<ImmutableParameterMeta> immutableParameterMetas, ContentType contentType, Object... args) throws MessageConverterException {

//MultipartEntityBuilder.create()
//        .addPart()
      return null;
   }
}
