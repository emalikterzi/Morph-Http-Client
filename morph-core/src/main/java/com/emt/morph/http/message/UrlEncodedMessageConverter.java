package com.emt.morph.http.message;

import com.emt.morph.http.message.api.HttpMessageConverter;
import com.emt.morph.utils.PrimitiveUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UrlEncodedMessageConverter implements HttpMessageConverter<Object, Object> {

    private final static String[] MIME_TYPE = {"application/x-www-form-urlencoded"};

    private final static Class<?> MAP_CLASS = Map.class;

    private static Object invokeMethod(Method method, Object source) {
        try {
            return method.invoke(source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    private static Method findMethod(Class cls, String methodName) {
        try {
            return cls.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @Override
    public HttpEntity write(Object body) {
        if (body == null) {
            return null;
        }
        List<BasicNameValuePair> nameValuePairs = new ArrayList<>();

        if (MAP_CLASS.isAssignableFrom(body.getClass())) {
            for (Map.Entry<?, ?> each : ((Map<?, ?>) body).entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(String.valueOf(each.getKey()), String.valueOf(each.getValue())));
            }
        } else {
            // primitive control maybe
            Class<?> sourceClass = body.getClass();
            Field[] fields = sourceClass.getDeclaredFields();

            for (Field each : fields) {

                if (!PrimitiveUtils.isPrimitiveOrWrapper(each.getType())) {
                    continue;
                }

                String methodName = each.getName().substring(0, 1).toUpperCase(Locale.ENGLISH) + each.getName().substring(1);
                Method method = findMethod(sourceClass, String.format("get%s", methodName));

                if (method == null) {
                    method = findMethod(sourceClass, String.format("is%s", methodName));
                }

                if (method != null) {
                    Object val = invokeMethod(method, body);
                    if (val != null) {
                        nameValuePairs.add(new BasicNameValuePair(each.getName(), String.valueOf(val)));
                    }
                }
            }
        }
        return new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8);
    }

    @Override
    public Object read(Method method, ContentType type, InputStream inputStream) throws Exception {
        return null;
    }

    @Override
    public String[] supportsMimeType() {
        return MIME_TYPE;
    }

    @Override
    public boolean supportsRead() {
        return false;
    }

}
