package com.emt.morph.proxy.invocations;

import com.emt.morph.annotation.MethodAnnotationConfig;
import com.emt.morph.annotation.ParameterAnnotationConfig;
import com.emt.morph.annotation.ParameterAnnotationMeta;
import com.emt.morph.annotation.api.RequestParamStrategyEnum;
import com.emt.morph.annotation.api.message.api.FormBodyPartProvider;
import com.emt.morph.exception.MorphException;
import com.emt.morph.http.message.MessageConverterUtils;
import com.emt.morph.http.message.api.HttpMessageConverter;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;
import com.emt.morph.utils.PrimitiveUtils;
import com.emt.morph.utils.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ParameterInvocation implements Invocation {

    private final MessageConverterUtils messageConverterUtils;

    public ParameterInvocation(MessageConverterUtils messageConverterUtils) {
        this.messageConverterUtils = messageConverterUtils;
    }

    @Override
    public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {
        MethodAnnotationConfig methodAnnotationConfig = chain.getContext().getMethodAnnotationConfig();

        ParameterAnnotationConfig requestBodyConfig = methodAnnotationConfig.findRequestBody();

        if (!Objects.isNull(requestBodyConfig)) {

            @SuppressWarnings("unchecked")
            HttpMessageConverter<Object, Object> converter = (HttpMessageConverter<Object, Object>) messageConverterUtils
                    .findSuitableConverter(requestBodyConfig.getClassType(), MessageConverterUtils.HTTP_CONVERTER_INTERFACE, 0);

            if (converter == null) {
                converter = (HttpMessageConverter<Object, Object>)
                        messageConverterUtils.findSuitableConverter(methodAnnotationConfig.getConsumes());
            }

            if (!Objects.isNull(converter)) {

                if (!converter.supportsWrite()) {
                    throw new MorphException(converter.getClass().getName() + " not supports write operation");
                }

                Object rawValue = null;
                try {
                    rawValue = args[requestBodyConfig.getIndex()];
                } catch (Exception e) {
                    // do nothing
                }
                if (!Objects.isNull(rawValue)) {
                    HttpEntity httpEntity = converter.write(rawValue, methodAnnotationConfig.getConsumes());
                    chain.getContext().setHttpEntity(httpEntity);
                }
            }

        }

        List<ParameterAnnotationConfig> multiPartConfigs = methodAnnotationConfig.findByType(ParameterAnnotationMeta.Type.MULTI_PART_BODY);

        MultipartEntityBuilder multipartEntityBuilder = null;

        if (!multiPartConfigs.isEmpty()) {

            for (ParameterAnnotationConfig each : multiPartConfigs) {
                Object rawValue = null;
                try {
                    rawValue = args[each.getIndex()];
                } catch (Exception e) {
                    // do nothing
                }
                if (!Objects.isNull(rawValue)) {
                    @SuppressWarnings("unchecked")
                    HttpMessageConverter<Object, Object> converter = (HttpMessageConverter<Object, Object>) messageConverterUtils
                            .findSuitableConverter(requestBodyConfig.getClassType(), MessageConverterUtils.FORM_BODY_PART_PROVIDER, 1);

                    if (converter == null) {
                        converter = (HttpMessageConverter<Object, Object>)
                                messageConverterUtils.findSuitableConverter(each.getMimeType());
                    }

                    if (converter != null && (converter instanceof FormBodyPartProvider)) {

                        @SuppressWarnings("unchecked")
                        FormBodyPartProvider<FormBodyPart, Object> provider = (FormBodyPartProvider<FormBodyPart, Object>) converter;
                        FormBodyPart formBodyPart = provider.createFormBodyPart(each.getValue(), rawValue);

                        if (formBodyPart != null) {

                            if (multipartEntityBuilder == null) {
                                multipartEntityBuilder = MultipartEntityBuilder.create();
                            }

                            multipartEntityBuilder.addPart(formBodyPart);
                        }
                    }
                }
            }
        }

        if (multipartEntityBuilder != null) {
            chain.getContext().setHttpEntity(multipartEntityBuilder.build());
        }

        List<ParameterAnnotationConfig> pathVariableConfigs = methodAnnotationConfig.findByType(ParameterAnnotationMeta.Type.PATH_VARIABLE);
        List<ParameterAnnotationConfig> requestParamConfigs = methodAnnotationConfig.findByType(ParameterAnnotationMeta.Type.REQUEST_PARAM);

        String url = chain.getContext().getSanitizedUrl();

        for (ParameterAnnotationConfig each : pathVariableConfigs) {

            if (PrimitiveUtils.isPrimitiveOrWrapper(each.getClassType())) {
                String rawValue;

                try {
                    Object val = args[each.getIndex()];
                    rawValue = val.toString();
                } catch (Exception e) {
                    rawValue = "";
                }

                String willReplace = new StringBuilder()
                        .append("\\{")
                        .append(each.getValue())
                        .append("}")
                        .toString();

                url = url.replaceAll(willReplace, rawValue);
            }
        }

        StringBuilder requestBuilder = new StringBuilder(url);
        boolean initial = true;

        for (int i = 0; i < requestParamConfigs.size(); i++) {

            ParameterAnnotationConfig config = requestParamConfigs.get(i);

            if (config.isCollection()) {
                Object val;

                try {
                    val = args[config.getIndex()];
                } catch (Exception e) {
                    continue;
                }

                if (val == null) {
                    continue;
                }

                @SuppressWarnings("unchecked")
                Collection<Object> collection = (Collection<Object>) val;

                if (collection.isEmpty()) {
                    continue;
                }

                Object o = collection.iterator().next();

                if (!PrimitiveUtils.isPrimitiveOrWrapper(o.getClass())) {
                    continue;
                }

                Collection<String> collectionStr = collection.stream().map(Object::toString).collect(Collectors.toList());

                if (initial) {
                    requestBuilder.append("?");
                } else {
                    requestBuilder.append("&");
                }

                initial = false;

                if (config.getRequestParamStrategyEnum().equals(RequestParamStrategyEnum.COMMA_SEPARATED)) {
                    String value = String.join(",", collectionStr);

                    requestBuilder
                            .append(config.getValue())
                            .append("=")
                            .append(value);
                } else {

                    Iterator<String> iterator = collectionStr.iterator();
                    while (iterator.hasNext()) {
                        String value = iterator.next();
                        requestBuilder
                                .append(config.getValue())
                                .append("=")
                                .append(value);

                        if (iterator.hasNext()) {
                            requestBuilder.append("&");
                        }
                    }
                }
            } else {
                String rawValue;

                try {
                    Object val = args[config.getIndex()];
                    rawValue = val.toString();
                } catch (Exception e) {
                    continue;
                }

                if (StringUtils.isEmpty(rawValue)) {
                    continue;
                }

                if (!PrimitiveUtils.isPrimitiveOrWrapper(config.getClassType())) {
                    continue;
                }

                if (initial) {
                    requestBuilder.append("?");
                } else {
                    requestBuilder.append("&");
                }

                initial = false;

                requestBuilder
                        .append(config.getValue())
                        .append("=")
                        .append(rawValue);

            }
        }

        chain.getContext().setSanitizedUrl(requestBuilder.toString());
        chain.invoke(callee, method, args);
        return null;
    }

}
