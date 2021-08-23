package com.emt.morph.http.message.api;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.lang.reflect.Method;

public interface HttpMessageConverter<BODY, RESPONSE> {

    default HttpEntity write(BODY body) {
        return null;
    }

    default HttpEntity write(BODY body, String mimeType) {
        return write(body);
    }

    RESPONSE read(Method method, ContentType type, InputStream inputStream) throws Exception;

    String[] supportsMimeType();

    default boolean supportsRead() {
        return true;
    }

    default boolean supportsWrite() {
        return true;
    }

}
