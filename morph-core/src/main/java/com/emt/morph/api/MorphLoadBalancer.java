package com.emt.morph.api;

import com.emt.morph.LoadBalancer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MorphLoadBalancer {

   Class<? extends LoadBalancer> value();

}
