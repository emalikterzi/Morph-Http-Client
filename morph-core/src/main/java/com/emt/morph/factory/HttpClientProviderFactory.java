package com.emt.morph.factory;

import com.emt.morph.http.HttpClientProvider;

public interface HttpClientProviderFactory {

   HttpClientProvider createHttpClientProvider(Class<? extends HttpClientProvider> httpClientProviderClass);

}
