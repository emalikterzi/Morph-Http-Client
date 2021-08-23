package com.emt.morph.annotation.processor;

import com.emt.morph.annotation.AbstractAnnotationProcessor;
import com.emt.morph.annotation.BaseAnnotationMeta;
import com.emt.morph.annotation.ParameterAnnotationMeta;
import com.emt.morph.common.HttpMethod;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

public class JerseyAnnotationProcessor extends AbstractAnnotationProcessor {


    static {
        createAnnotationResolver(Path.class, BaseAnnotationMeta.class, (path, meta) -> meta.setPath(path.value()));

        createAnnotationResolver(DELETE.class, BaseAnnotationMeta.class, (path, meta) -> meta.setMethod(HttpMethod.DELETE));

        createAnnotationResolver(GET.class, BaseAnnotationMeta.class, (path, meta) -> meta.setMethod(HttpMethod.GET));

        createAnnotationResolver(HEAD.class, BaseAnnotationMeta.class, (path, meta) -> meta.setMethod(HttpMethod.HEAD));

        createAnnotationResolver(OPTIONS.class, BaseAnnotationMeta.class, (path, meta) -> meta.setMethod(HttpMethod.OPTIONS));

        createAnnotationResolver(POST.class, BaseAnnotationMeta.class, (path, meta) -> meta.setMethod(HttpMethod.POST));

        createAnnotationResolver(PUT.class, BaseAnnotationMeta.class, (path, meta) -> meta.setMethod(HttpMethod.PUT));

        createAnnotationResolver(Consumes.class, BaseAnnotationMeta.class, (path, meta) -> {
            if (path.value().length != 0) {
                meta.setConsumes(path.value()[0]);
            }
        });

        createAnnotationResolver(Produces.class, BaseAnnotationMeta.class, (path, meta) -> {
            if (path.value().length != 0) {
                meta.setProduces(path.value()[0]);
            }
        });

        createAnnotationResolver(PathParam.class, ParameterAnnotationMeta.class, (path, meta) -> {
            meta.setType(ParameterAnnotationMeta.Type.PATH_VARIABLE);
            meta.setValue(path.value());
        });

        createAnnotationResolver(QueryParam.class, ParameterAnnotationMeta.class, (path, meta) -> {
            meta.setType(ParameterAnnotationMeta.Type.REQUEST_PARAM);
            meta.setValue(path.value());
        });

    }

}
