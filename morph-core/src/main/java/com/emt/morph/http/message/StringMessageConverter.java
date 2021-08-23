package com.emt.morph.http.message;

import com.emt.morph.annotation.api.message.api.FormBodyPartProvider;
import com.emt.morph.exception.MorphException;
import com.emt.morph.http.message.api.HttpMessageConverter;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class StringMessageConverter implements HttpMessageConverter<Object, String>, FormBodyPartProvider<FormBodyPart, Object> {

    private final static String[] MIME_TYPE = {"text/plain"};
    private final static int BUFFER = 2048;
    private ContentType contentType = ContentType.create("text/plain", StandardCharsets.UTF_8);

    @Override
    public HttpEntity write(Object body) {
        return null;
    }

    @Override
    public boolean supportsWrite() {
        return false;
    }

    @Override
    public String read(Method method, ContentType type, InputStream inputStream) throws Exception {

        if (!method.getGenericReturnType().equals(String.class)) {
            //todo this should validate maybe
            throw new MorphException("Method should be return string reflection exception");
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] bytes = new byte[BUFFER];

        int len;

        while ((len = inputStream.read(bytes)) != -1) {
            byteArrayOutputStream.write(bytes, 0, len);
        }

        return new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);

    }

    @Override
    public String[] supportsMimeType() {
        return MIME_TYPE;
    }

    @Override
    public FormBodyPart createFormBodyPart(String name, Object o) {
        FormBodyPartBuilder formBodyPartBuilder = FormBodyPartBuilder.create();
        formBodyPartBuilder.setName(name);
        formBodyPartBuilder.setBody(new StringBody(o.toString(), contentType));
        return formBodyPartBuilder.build();
    }
}
