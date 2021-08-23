package com.emt.morph.http.message;

import com.emt.morph.http.message.api.HttpMessageConverter;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

public class MultipartMessageConverter implements HttpMessageConverter<Object, Object> {

    private final List<HttpMessageConverter<?, ?>> httpMessageConverterList;

    public MultipartMessageConverter(List<HttpMessageConverter<?, ?>> httpMessageConverterList) {
        this.httpMessageConverterList = httpMessageConverterList;
    }

    @Override
    public HttpEntity write(Object o) {
        return null;
    }

    @Override
    public Object read(Method method, ContentType type, InputStream inputStream) throws Exception {
        return null;
    }

    @Override
    public String[] supportsMimeType() {
        return new String[0];
    }

    //    protected final HttpMessageConverter<?, ?> findSuitableConverter(String mimeType) {
//
//        for (HttpMessageConverter<?, ?> converter : converters) {
//            Optional<String> s = Arrays.stream(converter.supportsMimeType())
//                    .filter(x -> x.equals(mimeType))
//                    .findFirst();
//
//            if (s.isPresent()) {
//                return converter;
//            }
//        }
//
//        return null;
//    }

}
