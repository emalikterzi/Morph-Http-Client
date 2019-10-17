package com.emt.morph.impl;

import com.emt.morph.http.HttpClientProvider;
import com.emt.morph.proxy.invocations.MorphContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class DefaultHttpClientProvider implements HttpClientProvider {

   @Override
   public CloseableHttpClient getClient(MorphContext invocationContext) {
      return HttpClients.createDefault();
   }
}
