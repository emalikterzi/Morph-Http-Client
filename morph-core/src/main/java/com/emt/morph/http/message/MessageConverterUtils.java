package com.emt.morph.http.message;

import com.emt.morph.annotation.api.message.api.FormBodyPartProvider;
import com.emt.morph.http.message.api.HttpMessageConverter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MessageConverterUtils {

    public final static Class<?> HTTP_CONVERTER_INTERFACE = HttpMessageConverter.class;
    public final static Class<?> FORM_BODY_PART_PROVIDER = FormBodyPartProvider.class;

    private final List<HttpMessageConverter<?, ?>> httpMessageConverterList;
    private final MultipartMessageConverter multipartMessageConverter;

    public MessageConverterUtils(List<HttpMessageConverter<?, ?>> httpMessageConverterList, MultipartMessageConverter multipartMessageConverter) {
        this.httpMessageConverterList = httpMessageConverterList;
        this.multipartMessageConverter = multipartMessageConverter;
    }

    public HttpMessageConverter<?, ?> findSuitableConverter(String mimeType) {
        for (HttpMessageConverter<?, ?> each : httpMessageConverterList) {
            Optional<String> exist = Arrays.stream(each.supportsMimeType())
                    .filter(x -> x.equals(mimeType))
                    .findAny();

            if (exist.isPresent()) {
                return each;
            }
        }
        return null;
    }

    public HttpMessageConverter<?, ?> findSuitableConverter(Class<?> checkClass, Class<?> interfaceClass, int parameterIndex) {
        for (HttpMessageConverter<?, ?> eachConverter : httpMessageConverterList) {

            Type[] interfaces = eachConverter.getClass().getGenericInterfaces();

            for (Type eachInterface : interfaces) {

                boolean isConverter = ((ParameterizedType) eachInterface).getRawType().equals(interfaceClass);

                if (!isConverter) {
                    continue;
                }

                Type[] interfaceGenericType = ((ParameterizedType) eachInterface).getActualTypeArguments();

                if (interfaceGenericType[parameterIndex].equals(checkClass)) {
                    return eachConverter;
                }
            }
        }
        return null;
    }

}
