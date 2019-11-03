package com.emtdev.morph;

import org.apache.http.client.methods.CloseableHttpResponse;

public interface HttpResponseCloser {

   void addToQueue(CloseableHttpResponse closeableHttpResponse);

   void closeAll();

}
