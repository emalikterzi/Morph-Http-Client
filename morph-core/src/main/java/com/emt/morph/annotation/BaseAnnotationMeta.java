package com.emt.morph.annotation;

import com.emt.morph.common.HttpMethod;

public class BaseAnnotationMeta implements AnnotationMeta {

    private String path = "";
    private HttpMethod method;
    private String consumes;
    private String produces;

    public String getProduces() {
        return produces;
    }

    public void setProduces(String produces) {
        this.produces = produces;
    }

    public String getConsumes() {
        return consumes;
    }

    public void setConsumes(String consumes) {
        this.consumes = consumes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

}
