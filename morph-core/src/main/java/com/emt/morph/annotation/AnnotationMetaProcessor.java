package com.emt.morph.annotation;

import java.lang.annotation.Annotation;
import java.util.List;

public interface AnnotationMetaProcessor {

    <T extends AnnotationMeta> T processAnnotation(Annotation[] annotations, T t);

    List<ParameterAnnotationMeta> processParamAnnotations(Annotation[][] annotations, Class<?>[] parameterTypes);

}
