package com.emt.morph.proxy.invocations;

import com.emt.morph.common.HttpMethod;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationContext;
import com.emt.morph.proxy.InvocationSession;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Objects;

public class RequestPrepareInvocation implements Invocation {

    @Override
    public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {
        InvocationContext invocationContext = chain.getContext();
        HttpUriRequest httpRequest = determineRequest(invocationContext.getMethodAnnotationConfig().getMethod(), invocationContext.getUri());

        if (!Objects.isNull(invocationContext.getHttpEntity())) {

            if (httpRequest instanceof HttpEntityEnclosingRequest) {
                HttpEntityEnclosingRequest request = (HttpEntityEnclosingRequest) httpRequest;
                request.setEntity(invocationContext.getHttpEntity());
            }
        }
        invocationContext.setHttpRequest(httpRequest);
        chain.invoke(callee, method, args);
        return null;
    }


    private HttpUriRequest determineRequest(HttpMethod httpMethod, URI uri) {

        switch (httpMethod) {
            case GET:
                return new HttpGet(uri);
            case PUT:
                return new HttpPut(uri);
            case HEAD:
                return new HttpHead(uri);
            case POST:
                return new HttpPost(uri);
            case PATCH:
                return new HttpPatch(uri);
            case TRACE:
                return new HttpTrace(uri);
            case DELETE:
                return new HttpDelete(uri);
            case OPTIONS:
                return new HttpOptions(uri);
        }

        return null;
    }


}
