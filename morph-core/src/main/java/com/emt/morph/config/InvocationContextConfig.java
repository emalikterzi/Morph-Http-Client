package com.emt.morph.config;

public class InvocationContextConfig {

    private final boolean disableHttpRequest;

    public InvocationContextConfig(boolean disableHttpRequest) {
        this.disableHttpRequest = disableHttpRequest;
    }

    public boolean isDisableHttpRequest() {
        return disableHttpRequest;
    }
}
