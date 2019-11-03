package com.emtdev.morph.http.process;

@FunctionalInterface
public interface MorphProcessListener {

   void apply(long transferred, long totalBytes);

}
