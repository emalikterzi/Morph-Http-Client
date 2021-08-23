package com.emt.morph;

public interface MorphClient {

    <T> T morph(Class<T> tClass);

}
