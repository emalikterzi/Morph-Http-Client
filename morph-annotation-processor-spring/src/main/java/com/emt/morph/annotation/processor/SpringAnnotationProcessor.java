package com.emt.morph.annotation.processor;

import com.emt.morph.annotation.AbstractAnnotationProcessor;
import com.emt.morph.annotation.BaseAnnotationMeta;
import com.emt.morph.annotation.ParameterAnnotationMeta;
import com.emt.morph.common.HttpMethod;
import com.emt.morph.utils.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

public class SpringAnnotationProcessor extends AbstractAnnotationProcessor {


    static {
        createAnnotationResolver(RequestMapping.class, BaseAnnotationMeta.class, (requestMapping, baseAnnotationMeta) -> {

            if (requestMapping.path().length != 0) {
                baseAnnotationMeta.setPath(requestMapping.path()[0]);
            }

            if (baseAnnotationMeta.getPath().equals("")) {
                if (requestMapping.value().length != 0) {
                    baseAnnotationMeta.setPath(requestMapping.value()[0]);
                }
            }

            if (requestMapping.method().length != 0) {
                baseAnnotationMeta.setMethod(HttpMethod.valueOf(requestMapping.method()[0].toString()));
            }

            if (requestMapping.consumes().length != 0) {
                baseAnnotationMeta.setConsumes(requestMapping.consumes()[0]);
            }

            if (requestMapping.produces().length != 0) {
                baseAnnotationMeta.setProduces(requestMapping.produces()[0]);
            }
        });

        createAnnotationResolver(PathVariable.class, ParameterAnnotationMeta.class, (pathVariable, baseAnnotationMeta) -> {
            baseAnnotationMeta.setValue(pathVariable.value());
            baseAnnotationMeta.setType(ParameterAnnotationMeta.Type.PATH_VARIABLE);
        });

        createAnnotationResolver(RequestParam.class, ParameterAnnotationMeta.class, (requestParam, baseAnnotationMeta) -> {
            baseAnnotationMeta.setValue(requestParam.value());
            baseAnnotationMeta.setType(ParameterAnnotationMeta.Type.REQUEST_PARAM);
        });

        createAnnotationResolver(RequestBody.class, ParameterAnnotationMeta.class, (requestBody, baseAnnotationMeta) -> {
            baseAnnotationMeta.setType(ParameterAnnotationMeta.Type.BODY);
        });

        createAnnotationResolver(RequestPart.class, ParameterAnnotationMeta.class, (requestBody, baseAnnotationMeta) -> {

            if (StringUtils.isNotEmpty(requestBody.name())) {
                baseAnnotationMeta.setValue(requestBody.name());
            }

            if (StringUtils.isNotEmpty(requestBody.value())) {
                baseAnnotationMeta.setValue(requestBody.value());
            }

            if (StringUtils.isEmpty(baseAnnotationMeta.getValue())) {
                //todo maybe resovle via reflection
            }
            baseAnnotationMeta.setType(ParameterAnnotationMeta.Type.MULTI_PART_BODY);
        });

    }

}
