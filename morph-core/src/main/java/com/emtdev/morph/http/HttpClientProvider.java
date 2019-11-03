package com.emtdev.morph.http;

import com.emtdev.morph.proxy.invocations.MorphContext;
import org.apache.http.impl.client.CloseableHttpClient;

public interface HttpClientProvider {

   CloseableHttpClient getClient(MorphContext invocationContext);

}
