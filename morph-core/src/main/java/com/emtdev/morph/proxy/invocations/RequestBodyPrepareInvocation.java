package com.emtdev.morph.proxy.invocations;

import com.emtdev.morph.converter.MessageConverter;
import com.emtdev.morph.meta.ImmutableParameterMeta;
import com.emtdev.morph.proxy.Invocation;
import com.emtdev.morph.proxy.InvocationSession;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RequestBodyPrepareInvocation extends AbstractPayloadRelatedInvocation implements Invocation {

   public RequestBodyPrepareInvocation(List<MessageConverter> converters) {
      super(converters);
   }

   @Override
   public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {

      List<ImmutableParameterMeta> immutableParameterMetas
              = chain.getContext().getImmutableExecutionMeta().getMethodParameterMeta();

      List<ImmutableParameterMeta> headerParams = chain.getContext()
              .getImmutableExecutionMeta().getMethodParameterMeta()
              .stream().filter(x -> x.getMetaType().equals(ImmutableParameterMeta.MetaType.HEADER_PARAM))
              .collect(Collectors.toList());

      HttpRequestBase httpRequestBase = chain.getContext()
              .getHttpRequestBase();

      if (!headerParams.isEmpty()) {
         addHeaders(headerParams, args, httpRequestBase);
      }

      String consumes = chain.getContext().getImmutableExecutionMeta()
              .getConsumes();

      if (Objects.isNull(consumes)) {
         chain.invoke(callee, method, args);
         return null;
      }

      //try to create content type from consume value
      ContentType contentType = ContentType.create(consumes);
      MessageConverter messageConverter = findSuitableConverter(contentType);

      if (Objects.nonNull(messageConverter)
              && HttpEntityEnclosingRequest.class.isInstance(httpRequestBase)) {

         HttpEntityEnclosingRequest
                 httpEntityEnclosingRequest = (HttpEntityEnclosingRequest) httpRequestBase;
         HttpEntity httpEntity = messageConverter.write(immutableParameterMetas, contentType, args);
         httpEntityEnclosingRequest.setEntity(httpEntity);
      }


      chain.invoke(callee, method, args);
      return null;
   }

   private void addHeaders(List<ImmutableParameterMeta> headerParams, Object[] args,
                           HttpRequestBase httpEntityEnclosingRequest) {
      headerParams.stream()
              .map(x -> new BasicHeader(x.getValue(), Objects.nonNull(args[x.getIndex()])
                      ? String.valueOf(args[x.getIndex()]) : x.getDefaultValue()))
              .forEach(httpEntityEnclosingRequest::addHeader);
   }

}
