package com.emt.morph.http.message;

import com.emt.morph.http.message.api.HttpMessageConverter;
import com.emt.morph.utils.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;

import java.io.InputStream;
import java.lang.reflect.Method;

public class InputStreamMessageConverter implements HttpMessageConverter<InputStream, InputStream> {

    @Override
    public HttpEntity write(InputStream inputStream, String mimeType) {

        if (StringUtils.isEmpty(mimeType)) {
            return new InputStreamEntity(inputStream);
        }

        try {
            ContentType contentType = ContentType.create(mimeType);
            return new InputStreamEntity(inputStream, contentType);
        } catch (Exception e) {
            return new InputStreamEntity(inputStream);
        }
    }

    @Override
    public InputStream read(Method method, ContentType type, InputStream inputStream) throws Exception {
        return null;
    }

    @Override
    public String[] supportsMimeType() {
        return new String[0];
    }

    @Override
    public boolean supportsRead() {
        return false;
    }
}
