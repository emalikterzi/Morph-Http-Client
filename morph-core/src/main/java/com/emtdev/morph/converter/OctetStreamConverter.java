package com.emtdev.morph.converter;

import com.emtdev.morph.exception.MessageConverterException;
import com.emtdev.morph.meta.ImmutableParameterMeta;
import com.emtdev.morph.utils.ParameterMetaUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public class OctetStreamConverter implements MessageConverter {

   private final String[] mediaTypes = {"application/octet-stream"};


   @Override
   public String[] getSupportedMediaTypes() {
      return mediaTypes;
   }

   @Override
   public Object read(Method method, InputStream responseBody) throws MessageConverterException {
      throw new MessageConverterException("stream reads not supported");
   }

   @Override
   public HttpEntity write(List<ImmutableParameterMeta> immutableParameterMetas, ContentType contentType, Object... args) throws MessageConverterException {

      Object o = ParameterMetaUtils.findBody(immutableParameterMetas, args);

      if (Objects.isNull(o))
         return null;

      try {
         InputStream inputStream = tryToFindInputStream(o);
         InputStreamEntity inputStreamEntity = new InputStreamEntity(inputStream, contentType);
         return inputStreamEntity;
      } catch (FileNotFoundException e) {
         throw new MessageConverterException(e);
      }
   }

   private InputStream tryToFindInputStream(Object o) throws FileNotFoundException {
      if (o instanceof InputStream)
         return (InputStream) o;


      if (o instanceof File)
         return new FileInputStream((File) o);


      return null;
   }
}
