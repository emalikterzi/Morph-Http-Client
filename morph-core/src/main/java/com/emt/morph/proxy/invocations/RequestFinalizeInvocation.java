package com.emt.morph.proxy.invocations;

import com.emt.morph.HttpResponseCloser;
import com.emt.morph.converter.MessageConverter;
import com.emt.morph.exception.MorphException;
import com.emt.morph.http.HttpClientProvider;
import com.emt.morph.meta.ImmutableExecutionMeta;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;

import java.lang.reflect.Method;
import java.util.List;

public class RequestFinalizeInvocation extends AbstractPayloadRelatedInvocation implements Invocation {

   private final List<HttpClientProvider> httpClientProviders;
   private final HttpResponseCloser httpResponseCloser;

   public RequestFinalizeInvocation(List<MessageConverter<?>> converters,
                                    List<HttpClientProvider> httpClientProviders,
                                    HttpResponseCloser httpResponseCloser) {
      super(converters);
      this.httpClientProviders = httpClientProviders;
      this.httpResponseCloser = httpResponseCloser;
   }

   @Override
   public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {

      ImmutableExecutionMeta immutableExecutionMeta = chain.getContext().getImmutableExecutionMeta();

      HttpClientProvider httpClientProvider = findSuitableHttpClientProvider(immutableExecutionMeta);

      CloseableHttpClient httpClient = httpClientProvider.getClient(chain.getContext());
      CloseableHttpResponse closeableHttpResponse;
      try {
         closeableHttpResponse = httpClient.execute(chain.getContext().getHttpRequestBase());

         if (closeableHttpResponse.getStatusLine()
                 .getStatusCode() >= 300) {
            throw new RuntimeException("//status code report");
         }
      } catch (HttpHostConnectException e) {
         throw new MorphException(e);
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
         httpResponseCloser.addToQueue(closeableHttpResponse);
      }

   }

   private HttpClientProvider findSuitableHttpClientProvider(ImmutableExecutionMeta immutableExecutionMeta) {
      Class tClass = immutableExecutionMeta.getHttpClientProvide();
      return this.httpClientProviders.stream().filter(x -> x.getClass().equals(tClass))
              .findFirst().get();
   }


   //
}
