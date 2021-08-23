package com.emt.morph;

import com.emt.morph.annotation.AnnotationMetaProcessor;
import com.emt.morph.config.InvocationContextConfig;
import com.emt.morph.http.message.FileMessageConverter;
import com.emt.morph.http.message.InputStreamMessageConverter;
import com.emt.morph.http.message.JsonHttpMessageConverter;
import com.emt.morph.http.message.MessageConverterUtils;
import com.emt.morph.http.message.MultipartMessageConverter;
import com.emt.morph.http.message.StringMessageConverter;
import com.emt.morph.http.message.UrlEncodedMessageConverter;
import com.emt.morph.http.message.api.HttpMessageConverter;
import com.emt.morph.http.nameresolver.NameResolverProvider;
import com.emt.morph.loadbalancer.LoadBalancer;
import com.emt.morph.loadbalancer.RandomLoadBalancer;
import com.emt.morph.property.PropertyResolver;
import com.emt.morph.property.SystemPropertyResolver;
import com.emt.morph.proxy.ExecutionHandler;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.invocations.AnnotationInvocation;
import com.emt.morph.proxy.invocations.LoadBalancerInvocation;
import com.emt.morph.proxy.invocations.LoggingInvocation;
import com.emt.morph.proxy.invocations.NameResolverSelector;
import com.emt.morph.proxy.invocations.ParameterInvocation;
import com.emt.morph.proxy.invocations.PropertyInvocation;
import com.emt.morph.proxy.invocations.RequestExecutionInvocation;
import com.emt.morph.proxy.invocations.RequestPrepareInvocation;
import com.emt.morph.proxy.invocations.TimerInvocation;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MorphClientServiceImpl implements MorphClient {

    private final AnnotationMetaProcessor annotationProcessor;
    private final HttpClient httpClient = HttpClients.createDefault();
    private final List<Invocation> invocations;
    private final LoadBalancer loadBalancer = new RandomLoadBalancer();
    private final PropertyResolver propertyResolver = new SystemPropertyResolver();
    private final List<NameResolverProvider<?>> nameResolverProviders = new ArrayList<>();

    private final InvocationContextConfig config;
    private final HttpMessageConverter<?, ?>[] httpMessageConverters = {
            new JsonHttpMessageConverter(),
            new UrlEncodedMessageConverter(),
            new StringMessageConverter(),
            new FileMessageConverter(),
            new InputStreamMessageConverter()
    };

    private final List<HttpMessageConverter<?, ?>> httpMessageConverterList = Arrays.asList(httpMessageConverters);
    private final MultipartMessageConverter multipartMessageConverter = new MultipartMessageConverter(httpMessageConverterList);
    private final MessageConverterUtils messageConverterUtils = new MessageConverterUtils(httpMessageConverterList, multipartMessageConverter);

    public MorphClientServiceImpl(AnnotationMetaProcessor annotationProcessor, InvocationContextConfig config) {
        this.annotationProcessor = annotationProcessor;
        this.invocations = buildInvocations();
        this.config = config;
        Runtime.getRuntime().addShutdownHook(new Thread(this::onClose));
    }

    @Override
    @SuppressWarnings("unchecked")

    public <T> T morph(Class<T> tClass) {
        try {
            return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class[]{tClass}, new ExecutionHandler(invocations, this.config));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create" + tClass.getName(), e);
        }
    }


    private List<Invocation> buildInvocations() {
        Invocation[] invocations = {
                new TimerInvocation(),
                new LoggingInvocation(),
                new AnnotationInvocation(this.annotationProcessor),
                new PropertyInvocation(this.propertyResolver),
                new ParameterInvocation(this.messageConverterUtils),
                new NameResolverSelector(nameResolverProviders),
                new LoadBalancerInvocation((this.loadBalancer)),
                new RequestPrepareInvocation(),
                new RequestExecutionInvocation(this.messageConverterUtils, this.httpClient)
        };
        return Arrays.asList(invocations);
    }

    private void onClose() {
        for (Invocation eachInvocation : invocations) {
            try {
                eachInvocation.onClose();
            } catch (Exception e) {

            }
        }
    }
}
