package com.emtdev.morph.impl;

import com.emtdev.morph.http.HttpClientProvider;
import com.emtdev.morph.proxy.invocations.MorphContext;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class DefaultHttpClientProvider implements HttpClientProvider {

   @Override
   public CloseableHttpClient getClient(MorphContext invocationContext) {

      CloseableHttpClient closeableHttpClient = HttpClients
              .custom()
              .addInterceptorFirst(new HttpRequestInterceptor() {
                 @Override
                 public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                    System.out.println("");
                 }
              })
              .build();
      return closeableHttpClient;
   }
}
