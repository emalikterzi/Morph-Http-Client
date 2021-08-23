package com.emt.morph.proxy;

import com.emt.morph.annotation.MethodAnnotationConfig;
import com.emt.morph.config.InvocationContextConfig;
import com.emt.morph.http.nameresolver.InetSocketAddress;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;

import java.net.URI;
import java.util.List;

public class InvocationContext {

    private final InvocationContextConfig config;

    private String sanitizedUrl;
    private HttpEntity httpEntity;
    private List<InetSocketAddress> inetSocketAddresses;
    private MethodAnnotationConfig methodAnnotationConfig;
    private URI uri;
    private HttpUriRequest httpRequest;

    public InvocationContext(InvocationContextConfig config) {
        this.config = config;
    }

    public MethodAnnotationConfig getMethodAnnotationConfig() {
        return methodAnnotationConfig;
    }

    public void setMethodAnnotationConfig(MethodAnnotationConfig methodAnnotationConfig) {
        this.methodAnnotationConfig = methodAnnotationConfig;
    }

    public String getSanitizedUrl() {
        return sanitizedUrl;
    }

    public void setSanitizedUrl(String sanitizedUrl) {
        this.sanitizedUrl = sanitizedUrl;
    }

    public HttpEntity getHttpEntity() {
        return httpEntity;
    }

    public void setHttpEntity(HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
    }

    public List<InetSocketAddress> getInetSocketAddresses() {
        return inetSocketAddresses;
    }

    public void setInetSocketAddresses(List<InetSocketAddress> inetSocketAddresses) {
        this.inetSocketAddresses = inetSocketAddresses;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public HttpUriRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpUriRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public InvocationContextConfig getConfig() {
        return config;
    }

}
