package com.emt.morph.impl;

import com.emt.morph.Constants;
import com.emt.morph.LoadBalancer;
import com.emt.morph.MethodExecutionMetaProvider;
import com.emt.morph.http.ClientHttpMethod;
import com.emt.morph.http.HttpClientProvider;
import com.emt.morph.meta.ExecutionMeta;
import com.emt.morph.meta.ImmutableExecutionMeta;
import com.emt.morph.meta.ImmutableParameterMeta;
import com.emt.morph.utils.PrimitiveUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ReflectionExecutionMetaProvider implements MethodExecutionMetaProvider {

   private final HashMap<Method, ExecutionMeta> methodExecutionMetaHashMap = new HashMap<>();

   @Override
   public ImmutableExecutionMeta getMetaData(Method method) {
      ExecutionMeta executionMeta = methodExecutionMetaHashMap.get(method);
      if (executionMeta == null) {
         ExecutionMeta parentMeta = buildForParent(method);

         executionMeta = buildChild(method);
         executionMeta.setParent(parentMeta);

         methodExecutionMetaHashMap.put(method, executionMeta);
      }

      String path = (executionMeta.getParent().getPath() + executionMeta.getPath()).trim();

      String consumes = executionMeta.getConsumes() == null ? executionMeta.getParent().getConsumes() :
              executionMeta.getConsumes();

      String produces = executionMeta.getProduces() == null ? executionMeta.getParent().getProduces() :
              executionMeta.getProduces();

      ClientHttpMethod clientHttpMethod = executionMeta.getClientHttpMethod() == null ? executionMeta.getParent().getClientHttpMethod() :
              executionMeta.getClientHttpMethod();

      Class<? extends LoadBalancer> loadBalancer = executionMeta.getLoadBalancer() == null ? executionMeta
              .getParent().getLoadBalancer() : executionMeta.getLoadBalancer();
      Class<? extends HttpClientProvider> httpClientProvider = executionMeta.getHttpClientProvider() == null ?
              executionMeta.getParent().getHttpClientProvider() : executionMeta.getHttpClientProvider();

      if (loadBalancer == null)
         loadBalancer = SelectFirstLoadBalancer.class;

      if (httpClientProvider == null)
         httpClientProvider = DefaultHttpClientProvider.class;

      return new ImmutableExecutionMeta(path, clientHttpMethod, executionMeta.getMethodParameterMeta(),
              consumes, produces, loadBalancer, httpClientProvider);
   }


   private ExecutionMeta buildForParent(Method method) {
      Annotation[] annotation = method.getDeclaringClass().getAnnotations();
      return buildCommons(annotation);
   }


   private ExecutionMeta buildChild(Method method) {
      Annotation[] annotation = method.getAnnotations();
      ExecutionMeta executionMeta = buildCommons(annotation);
      Annotation[][] parameterAnnotations = method.getParameterAnnotations();
      Class<?>[] paramTypes = method.getParameterTypes();
      List<ImmutableParameterMeta> methodParameterMetas = buildParamMeta(parameterAnnotations, paramTypes);
      executionMeta.setMethodParameterMeta(methodParameterMetas);
      return executionMeta;
   }

   private List<ImmutableParameterMeta> buildParamMeta(Annotation[][] annotations, Class<?>[] paramTypes) {

      List<ImmutableParameterMeta> methodParameterMetas = new ArrayList<>();

      boolean bodyFound = false;
      for (int i = 0; i < annotations.length; i++) {
         Annotation[] paramAnnotations = annotations[i];
         Class paramType = paramTypes[i];

         boolean primitiveStatus = PrimitiveUtils.isPrimitiveOrWrapper(paramType);

         /**
          * all non primitive objects will be treated as request body
          */

         if (primitiveStatus) {
            tryToFindQueryAndPathParam(paramAnnotations, i, paramType, methodParameterMetas);
         } else {
            if (!bodyFound) {
               methodParameterMetas.add(new ImmutableParameterMeta(ImmutableParameterMeta.MetaType.BODY, i, paramType));
               bodyFound = true;
            } else {
               //todo multiple body found , should this be warning ???
            }
         }

      }

      return methodParameterMetas;
   }

   private void tryToFindQueryAndPathParam(Annotation[] annotations, int index,
                                           Class<?> paramType, List<ImmutableParameterMeta> methodParameterMetas) {

      PathParam pathParam = null;
      QueryParam queryParam = null;
      DefaultValue defaultValue = null;

      for (Annotation eachParamAnnotation : annotations) {

         if (eachParamAnnotation.annotationType().equals(Constants.PATH_PARAM_CLASS)) {
            pathParam = (PathParam) eachParamAnnotation;

         }

         if (eachParamAnnotation.annotationType().equals(Constants.QUERY_PARAM_CLASS)) {
            queryParam = (QueryParam) eachParamAnnotation;

         }

         if (eachParamAnnotation.annotationType().equals(Constants.DEFAULT_VALUE_CLASS)) {
            defaultValue = (DefaultValue) eachParamAnnotation;

         }
      }

      String defValue = null;

      if (defaultValue != null)
         defValue = defaultValue.value();

      if (queryParam != null) {
         methodParameterMetas.add(new ImmutableParameterMeta(ImmutableParameterMeta.MetaType.QUERY_PARAM, index, paramType, queryParam.value(), defValue));
      } else if (pathParam != null) {
         methodParameterMetas.add(new ImmutableParameterMeta(ImmutableParameterMeta.MetaType.PATH_PARAM, index, paramType, pathParam.value(), defValue));
      }

   }

   private ExecutionMeta buildCommons(Annotation[] annotations) {


      ExecutionMeta executionMeta = new ExecutionMeta();

      for (Annotation annotation : annotations) {
         checkForMethodType(annotation, executionMeta);
         checkForPath(annotation, executionMeta);
         checkForMediaType(annotation, executionMeta);
      }

      return executionMeta;
   }

   private void checkForMediaType(Annotation annotation, ExecutionMeta executionMeta) {
      if (annotation.annotationType().equals(Constants.CONSUMES_CLASS)) {
         Consumes consumes = (Consumes) annotation;
         String s = consumes.value()[0];
         executionMeta.setConsumes(s);
      }

      if (annotation.annotationType().equals(Constants.PRODUCES_CLASS)) {
         Produces produces = (Produces) annotation;
         String s = produces.value()[0];
         executionMeta.setProduces(s);
      }

   }

   private void checkForPath(Annotation annotation, ExecutionMeta executionMeta) {
      if (annotation.annotationType().equals(Constants.PATH_CLASS)) {
         Path path = (Path) annotation;
         executionMeta.setPath(path.value());
      }
   }

   private void checkForMethodType(Annotation annotation, ExecutionMeta executionMeta) {
      Optional<ClientHttpMethod> clientHttpMethod = ClientHttpMethod.findByClass(annotation.annotationType());
      clientHttpMethod.ifPresent(executionMeta::setClientHttpMethod);
   }


}
