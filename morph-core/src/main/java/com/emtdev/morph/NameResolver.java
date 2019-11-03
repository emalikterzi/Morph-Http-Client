package com.emtdev.morph;

public interface NameResolver {

   String getScheme();

   int priority();

   void start(String authority, Listener listener);


}
