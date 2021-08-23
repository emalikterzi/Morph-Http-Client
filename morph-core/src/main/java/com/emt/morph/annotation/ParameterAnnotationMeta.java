package com.emt.morph.annotation;

import com.emt.morph.annotation.api.RequestParamStrategyEnum;

public class ParameterAnnotationMeta implements AnnotationMeta {

    private int index;
    private boolean collection;
    private Class<?> classType;
    private Type type;
    private String value;
    private String mimeType;
    private RequestParamStrategyEnum requestParamStrategyEnum;

    public ParameterAnnotationMeta() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Class<?> getClassType() {
        return classType;
    }

    public void setClassType(Class<?> classType) {
        this.classType = classType;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isCollection() {
        return collection;
    }

    public void setCollection(boolean collection) {
        this.collection = collection;
    }

    public RequestParamStrategyEnum getRequestParamStrategyEnum() {
        return requestParamStrategyEnum;
    }

    public void setRequestParamStrategyEnum(RequestParamStrategyEnum requestParamStrategyEnum) {
        this.requestParamStrategyEnum = requestParamStrategyEnum;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public enum Type {
        REQUEST_PARAM, PATH_VARIABLE, BODY, MULTI_PART_BODY
    }

}
