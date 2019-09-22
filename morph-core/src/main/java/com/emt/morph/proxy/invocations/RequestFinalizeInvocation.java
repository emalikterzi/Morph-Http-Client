package com.emt.morph.proxy.invocations;

import com.emt.morph.HttpResponseCloser;
import com.emt.morph.converter.MessageConverter;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;
import com.emt.morph.http.HttpClientProvider;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;

import java.lang.reflect.Method;
import java.util.List;

public class RequestFinalizeInvocation extends AbstractPayloadRelatedInvocation implements Invocation {

   private final HttpClientProvider httpClientProvider;
   private final HttpResponseCloser httpReponseCloser;

   public RequestFinalizeInvocation(List<MessageConverter<?>> converters,
                                    HttpClientProvider httpClientProvider,
                                    HttpResponseCloser httpReponseCloser) {
      super(converters);
      this.httpClientProvider = httpClientProvider;
      this.httpReponseCloser = httpReponseCloser;
   }

   @Override
   public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {

      CloseableHttpClient httpClient = httpClientProvider.getClient(chain.getContext());

      CloseableHttpResponse closeableHttpResponse = httpClient.execute(chain.getContext().getHttpRequestBase());

      if (closeableHttpResponse.getStatusLine()
              .getStatusCode() >= 300) {
         throw new RuntimeException("//status code report");
      }

      try {
         HttpEntity responseEntity = closeableHttpResponse.getEntity();

         ContentType contentType = ContentType.get(responseEntity);

         MessageConverter<?> messageConverter = findSuitableConverter(contentType);

         if (responseEntity.getContent() != null) {

            Object result = messageConverter.read(responseEntity.getContent(),
                    contentType, method.getGenericReturnType(), method.getReturnType());
            return result;
         }
         return null;
      } finally {
         httpReponseCloser.addToQueue(closeableHttpResponse);
      }

   }


   //
}
