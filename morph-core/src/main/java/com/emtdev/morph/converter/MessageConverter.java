package com.emtdev.morph.converter;

import com.emtdev.morph.exception.MessageConverterException;
import com.emtdev.morph.meta.ImmutableParameterMeta;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

public interface MessageConverter {

   String[] getSupportedMediaTypes();

   Object read(Method method, InputStream responseBody) throws MessageConverterException;

   HttpEntity write(List<ImmutableParameterMeta> immutableParameterMetas
           , ContentType contentType, Object... args) throws MessageConverterException;


}
