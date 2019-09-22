package com.emt.morph.converter;

import com.emt.morph.exception.MessageConverterException;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.lang.reflect.Type;

public interface MessageConverter<T> {

   String getSupportedMediaType();

   T read(InputStream body, ContentType contentType, Type type, Class<?> tClass) throws MessageConverterException;

   HttpEntity write(Object body, ContentType contentType,Class<?> tclass) throws MessageConverterException;


}
