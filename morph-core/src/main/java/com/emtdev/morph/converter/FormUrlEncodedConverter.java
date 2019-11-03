package com.emtdev.morph.converter;


import com.emtdev.morph.exception.MessageConverterException;
import com.emtdev.morph.exception.MorphException;
import com.emtdev.morph.meta.ImmutableParameterMeta;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FormUrlEncodedConverter implements MessageConverter {

   private String[] MEDIA_TYPES = {"application/x-www-form-urlencoded"};

   @Override
   public String[] getSupportedMediaTypes() {
      return MEDIA_TYPES;
   }

   @Override
   public Object read(Method method, InputStream responseBody) throws MessageConverterException {
      throw new MorphException("unsupported operation");
   }


   @Override
   public HttpEntity write(List<ImmutableParameterMeta> immutableParameterMetas, ContentType contentType, Object... args) throws MessageConverterException {
      List<NameValuePair> nameValuePairList = getFormParams(immutableParameterMetas, args);
      UrlEncodedFormEntity urlEncodedFormEntity;
      if (Objects.isNull(contentType.getCharset())) {
         urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList, Charset.defaultCharset());
      } else {
         urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList, contentType
                 .getCharset());
      }
      return urlEncodedFormEntity;
   }


   private List<NameValuePair> getFormParams(List<ImmutableParameterMeta> formParams, Object[] args) {
      return formParams.stream()
              .map(x -> new BasicNameValuePair(x.getValue(), Objects.nonNull(args[x.getIndex()])
                      ? String.valueOf(args[x.getIndex()]) : x.getDefaultValue()))
              .collect(Collectors.toList());
   }

}
