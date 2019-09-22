package com.emt.morph.http;

import com.emt.morph.proxy.invocations.MorphContext;
import org.apache.http.impl.client.CloseableHttpClient;

public interface HttpClientProvider {

   CloseableHttpClient getClient(MorphContext invocationContext);

}
