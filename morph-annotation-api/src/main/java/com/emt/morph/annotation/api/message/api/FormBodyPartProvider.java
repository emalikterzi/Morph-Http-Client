package com.emt.morph.annotation.api.message.api;


public interface FormBodyPartProvider<T, REQUEST> {

    T createFormBodyPart(String name, REQUEST o);

}
