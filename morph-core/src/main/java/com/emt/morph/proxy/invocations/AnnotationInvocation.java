package com.emt.morph.proxy.invocations;

import com.emt.morph.annotation.AnnotationMetaProcessor;
import com.emt.morph.annotation.BaseAnnotationMeta;
import com.emt.morph.annotation.MethodAnnotationConfig;
import com.emt.morph.annotation.ParameterAnnotationConfig;
import com.emt.morph.annotation.ParameterAnnotationMeta;
import com.emt.morph.common.HttpMethod;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;
import com.emt.morph.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AnnotationInvocation implements Invocation {

    private final AnnotationMetaProcessor annotationProcessor;

    public AnnotationInvocation(AnnotationMetaProcessor annotationProcessor) {
        this.annotationProcessor = annotationProcessor;
    }

    @Override
    public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {

        Annotation[] annotations = method.getDeclaringClass().getDeclaredAnnotations();

        BaseAnnotationMeta classConfigs = annotationProcessor.processAnnotation(annotations, new BaseAnnotationMeta());
        BaseAnnotationMeta methodConfigs = annotationProcessor.processAnnotation(method.getAnnotations(), new BaseAnnotationMeta());

        List<ParameterAnnotationMeta> parameterAnnotationMetaList =
                annotationProcessor.processParamAnnotations(method.getParameterAnnotations(), method.getParameterTypes());

        List<ParameterAnnotationConfig> parameterAnnotationConfigs =
                parameterAnnotationMetaList.stream().map(ParameterAnnotationConfig::from).collect(Collectors.toList());

        String consumes = "application/json";
        String produces = null;

        if (StringUtils.isEmpty(methodConfigs.getConsumes())) {
            if (StringUtils.isNotEmpty(classConfigs.getConsumes())) {
                consumes = classConfigs.getConsumes();
            }
        } else {
            consumes = methodConfigs.getConsumes();
        }

        if (StringUtils.isEmpty(methodConfigs.getProduces())) {
            if (StringUtils.isNotEmpty(classConfigs.getProduces())) {
                consumes = classConfigs.getProduces();
            }
        } else {
            consumes = methodConfigs.getProduces();
        }

        StringBuilder pathBuilder = new StringBuilder();

        if (StringUtils.isNotEmpty(classConfigs.getPath())) {
            pathBuilder.append(classConfigs.getPath());
        }

        if (StringUtils.isNotEmpty(methodConfigs.getPath())) {
            pathBuilder.append(methodConfigs.getPath());
        }

        HttpMethod httpMethod = null;

        if (Objects.isNull(methodConfigs.getMethod())) {
            if (!Objects.isNull(classConfigs.getMethod())) {
                httpMethod = classConfigs.getMethod();
            }
        } else {
            httpMethod = methodConfigs.getMethod();
        }

        MethodAnnotationConfig annotationConfig =
                new MethodAnnotationConfig(pathBuilder.toString(), httpMethod, consumes, produces, parameterAnnotationConfigs);

        chain.getContext().setMethodAnnotationConfig(annotationConfig);

        chain.invoke(callee, method, args);
        return null;
    }

}
