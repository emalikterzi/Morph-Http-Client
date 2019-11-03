package com.emtdev.morph.factory;

import com.emtdev.morph.http.HttpClientProvider;

public interface HttpClientProviderFactory {

   HttpClientProvider createHttpClientProvider(Class<? extends HttpClientProvider> httpClientProviderClass);

}
