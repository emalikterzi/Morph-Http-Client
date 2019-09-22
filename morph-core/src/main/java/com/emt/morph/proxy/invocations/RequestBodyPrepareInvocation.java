package com.emt.morph.proxy.invocations;

import com.emt.morph.converter.MessageConverter;
import com.emt.morph.exception.MessageConverterException;
import com.emt.morph.meta.ImmutableParameterMeta;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.entity.ContentType;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RequestBodyPrepareInvocation extends AbstractPayloadRelatedInvocation implements Invocation {

   public RequestBodyPrepareInvocation(List<MessageConverter<?>> converters) {
      super(converters);
   }

   @Override
   public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {

      if (!chain.getContext().isBodyNeeded()) {
         chain.invoke(callee, method, args);
         return null;
      }

      Optional<ImmutableParameterMeta> immutableParameterMeta = chain.getContext()
              .getImmutableExecutionMeta().getMethodParameterMeta()
              .stream().filter(x -> x.getMetaType().equals(ImmutableParameterMeta.MetaType.BODY))
              .findFirst();

      HttpEntityEnclosingRequest httpRequestBase = (HttpEntityEnclosingRequest) chain.getContext()
              .getHttpRequestBase();


      immutableParameterMeta.ifPresent(x -> {
         String consumes = chain.getContext().getImmutableExecutionMeta()
                 .getConsumes();

         if (Objects.isNull(consumes))
            return;

         //try to create content type from consume value
         ContentType contentType = ContentType.create(consumes);

         Object currentValueOfBody = args[x.getIndex()];
         if (Objects.nonNull(currentValueOfBody)) {
            MessageConverter<?> messageConverter = findSuitableConverter(contentType);

            try {
               HttpEntity httpEntity = messageConverter.write(currentValueOfBody, contentType, x.getTypeClass());
               httpRequestBase.setEntity(httpEntity);
            } catch (MessageConverterException e) {
               e.printStackTrace();
            }
         }

      });

      chain.invoke(callee, method, args);
      return null;
   }
}
