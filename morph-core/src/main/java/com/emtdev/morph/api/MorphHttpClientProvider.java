package com.emtdev.morph.api;

import com.emtdev.morph.http.HttpClientProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MorphHttpClientProvider {

   Class<? extends HttpClientProvider> value();

}
