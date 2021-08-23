package com.emt.morph.http.message;

import com.emt.morph.annotation.api.message.api.FormBodyPartProvider;
import com.emt.morph.http.message.api.HttpMessageConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class JsonHttpMessageConverter implements HttpMessageConverter<Object, Object>, FormBodyPartProvider<FormBodyPart, Object> {

    private final static String[] MIME_TYPE = {"application/json"};
    private final static ContentType CONTENT_TYPE = ContentType.APPLICATION_JSON;

    private final ObjectMapper objectMapper = new ObjectMapper() {
        {
            super.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            super.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            super.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        }
    };

    @Override
    public HttpEntity write(Object body) {

        if (body == null) {
            return null;
        }

        try {
            String json = objectMapper.writeValueAsString(body);

            return new StringEntity(json, CONTENT_TYPE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Object read(Method method, ContentType type, InputStream inputStream) throws Exception {
        Type methodReturnType = method.getGenericReturnType();
        JavaType javaType = objectMapper.getTypeFactory().constructType(methodReturnType);
        return objectMapper.readValue(inputStream, javaType);
    }

    @Override
    public String[] supportsMimeType() {
        return MIME_TYPE;
    }

    @Override
    public FormBodyPart createFormBodyPart(String name, Object o) {
        String json;

        try {
            json = objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        FormBodyPartBuilder formBodyPartBuilder = FormBodyPartBuilder.create();
        formBodyPartBuilder.setName(name);
        formBodyPartBuilder.setBody(new StringBody(json, CONTENT_TYPE));
        return formBodyPartBuilder.build();
    }
}
