package com.emt.morph.annotation;

import com.emt.morph.annotation.api.MultiPart;
import com.emt.morph.annotation.api.RequestParamStrategy;
import com.emt.morph.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class AbstractAnnotationProcessor implements AnnotationMetaProcessor {

    private static final Map<Class<? extends Annotation>, AnnotationResolver<? extends Annotation, ? extends AnnotationMeta>> processMap =
            new HashMap<>();
    private static final AnnotationResolver<Annotation, AnnotationMeta> EMPTY = (annotation, annotationMeta) -> {
    };

    static {
        createAnnotationResolver(RequestParamStrategy.class, ParameterAnnotationMeta.class, (requestParamStrategy, parameterAnnotationMeta)
                -> parameterAnnotationMeta.setRequestParamStrategyEnum(requestParamStrategy.value()));


        createAnnotationResolver(MultiPart.class, ParameterAnnotationMeta.class, (requestBody, baseAnnotationMeta) -> {

            if (StringUtils.isNotEmpty(requestBody.name())) {
                baseAnnotationMeta.setValue(requestBody.name());
            }

            baseAnnotationMeta.setMimeType(requestBody.mimeType());

            if (StringUtils.isEmpty(baseAnnotationMeta.getValue())) {
                //todo maybe resovle via reflection
            }
            baseAnnotationMeta.setType(ParameterAnnotationMeta.Type.MULTI_PART_BODY);
        });
    }

    protected static <T extends Annotation, R extends AnnotationMeta> void createAnnotationResolver(
            Class<T> tClass, Class<R> r, AnnotationResolver<T, R> trAnnotationResolver) {
        processMap.put(tClass, trAnnotationResolver);
    }

    protected AnnotationResolver<? extends Annotation, ? extends AnnotationMeta> find(Class<?> tClass) {
        return processMap.getOrDefault(tClass, EMPTY);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends AnnotationMeta> T processAnnotation(Annotation[] annotations, T t) {
        if (annotations == null || annotations.length == 0) {
            return t;
        }
        for (Annotation eachAnnotation : annotations) {
            Class<? extends Annotation> aClass = eachAnnotation.annotationType();
            AnnotationResolver annotationResolver = find(aClass);
            annotationResolver.resolve(eachAnnotation, t);
        }

        return t;
    }

    @Override
    public List<ParameterAnnotationMeta> processParamAnnotations(Annotation[][] paramList, Class<?>[] parameterTypes) {
        List<ParameterAnnotationMeta> parameterAnnotationMetas = new ArrayList<>();

        for (int i = 0; i < paramList.length; i++) {
            ParameterAnnotationMeta parameterAnnotationMeta = new ParameterAnnotationMeta();

            parameterAnnotationMeta.setIndex(i);

            Annotation[] annotations = paramList[i];

            Class<?> parameterType = parameterTypes[i];

            if (annotations.length == 0) {
                parameterAnnotationMeta.setType(ParameterAnnotationMeta.Type.BODY);
            }

            processAnnotation(annotations, parameterAnnotationMeta);

            parameterAnnotationMeta.setCollection(Collection.class.isAssignableFrom(parameterType));
            parameterAnnotationMeta.setClassType(parameterType);
            parameterAnnotationMetas.add(parameterAnnotationMeta);
        }

        return parameterAnnotationMetas;
    }


    public interface AnnotationResolver<T extends Annotation, R extends AnnotationMeta> {

        void resolve(T t, R r);

    }

}
