package com.emt.morph.annotation;

import com.emt.morph.annotation.api.RequestParamStrategyEnum;

public class ParameterAnnotationConfig {

    private final int index;
    private final Class<?> classType;
    private final RequestParamStrategyEnum requestParamStrategyEnum;
    private final ParameterAnnotationMeta.Type type;
    private final String value;
    private final boolean collection;
    private final String mimeType;

    public ParameterAnnotationConfig(int index, Class<?> classType, RequestParamStrategyEnum requestParamStrategyEnum,
                                     ParameterAnnotationMeta.Type type, String value, boolean collection,
                                     String mimeType
    ) {
        this.index = index;
        this.classType = classType;
        this.requestParamStrategyEnum = requestParamStrategyEnum;
        this.type = type;
        this.value = value;
        this.collection = collection;
        this.mimeType = mimeType;
    }

    public static ParameterAnnotationConfig from(ParameterAnnotationMeta meta) {
        return new ParameterAnnotationConfig(meta.getIndex(), meta.getClassType(), meta.getRequestParamStrategyEnum(),
                meta.getType(), meta.getValue(), meta.isCollection(), meta.getMimeType());
    }

    public int getIndex() {
        return index;
    }

    public Class<?> getClassType() {
        return classType;
    }

    public RequestParamStrategyEnum getRequestParamStrategyEnum() {
        return requestParamStrategyEnum;
    }

    public ParameterAnnotationMeta.Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean isCollection() {
        return collection;
    }

    public String getMimeType() {
        return mimeType;
    }
}
