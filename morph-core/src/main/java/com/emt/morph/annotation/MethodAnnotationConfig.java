package com.emt.morph.annotation;

import com.emt.morph.common.HttpMethod;

import java.util.List;
import java.util.stream.Collectors;

public class MethodAnnotationConfig {

    private final String path;
    private final HttpMethod method;
    private final String consumes;
    private final String produces;
    private final List<ParameterAnnotationConfig> parameterAnnotationConfigs;

    public MethodAnnotationConfig(String path, HttpMethod method, String consumes, String produces, List<ParameterAnnotationConfig> parameterAnnotationConfigs) {
        this.path = path;
        this.method = method;
        this.consumes = consumes;
        this.produces = produces;
        this.parameterAnnotationConfigs = parameterAnnotationConfigs;
    }

    public List<ParameterAnnotationConfig> getParameterAnnotationConfigs() {
        return parameterAnnotationConfigs;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getConsumes() {
        return consumes;
    }

    public String getProduces() {
        return produces;
    }

    public ParameterAnnotationConfig findRequestBody() {
        return parameterAnnotationConfigs.stream().filter(x -> x.getType().equals(ParameterAnnotationMeta.Type.BODY)).findFirst().orElse(null);
    }

    public List<ParameterAnnotationConfig> findByType(ParameterAnnotationMeta.Type type) {
        return parameterAnnotationConfigs.stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
    }
}
