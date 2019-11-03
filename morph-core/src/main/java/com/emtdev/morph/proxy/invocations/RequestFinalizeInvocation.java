package com.emtdev.morph.proxy.invocations;

import com.emtdev.morph.HttpResponseCloser;
import com.emtdev.morph.converter.MessageConverter;
import com.emtdev.morph.exception.MorphException;
import com.emtdev.morph.http.HttpClientProvider;
import com.emtdev.morph.meta.ImmutableExecutionMeta;
import com.emtdev.morph.proxy.Invocation;
import com.emtdev.morph.proxy.InvocationSession;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public class RequestFinalizeInvocation extends AbstractPayloadRelatedInvocation implements Invocation {

   private final List<HttpClientProvider> httpClientProviders;
   private final HttpResponseCloser httpResponseCloser;

   public RequestFinalizeInvocation(List<MessageConverter> converters,
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
            String errorMessage = buildError(closeableHttpResponse);

            throw new MorphException(errorMessage);
         }
      } catch (HttpHostConnectException e) {
         throw new MorphException(e);
      }

      try {
         HttpEntity responseEntity = closeableHttpResponse.getEntity();
         ContentType contentType = ContentType.get(responseEntity);
         MessageConverter messageConverter = findSuitableConverter(contentType);
         if (responseEntity.getContent() != null) {
            Object result = messageConverter.read(method, responseEntity.getContent());
            return result;
         }
         return null;
      } finally {
         httpResponseCloser.addToQueue(closeableHttpResponse);
      }

   }

   private String buildError(CloseableHttpResponse closeableHttpResponse) {
      String body = null;

      if (Objects.nonNull(closeableHttpResponse.getEntity())) {
         try {
            body = EntityUtils.toString(closeableHttpResponse.getEntity());
         } catch (Exception e) {

         }
      }
      StringBuilder errorBuilder = new StringBuilder();

      errorBuilder
              .append("\n")
              .append("Error Code: ")
              .append(closeableHttpResponse.getStatusLine().getStatusCode())
              .append("\n");

      if (Objects.nonNull(body))
         errorBuilder.append("Server Message: ")
                 .append(body);


      return errorBuilder.toString();
   }

   private HttpClientProvider findSuitableHttpClientProvider(ImmutableExecutionMeta immutableExecutionMeta) {
      Class tClass = immutableExecutionMeta.getHttpClientProvide();
      return this.httpClientProviders.stream().filter(x -> x.getClass().equals(tClass))
              .findFirst().get();
   }


   //
}
