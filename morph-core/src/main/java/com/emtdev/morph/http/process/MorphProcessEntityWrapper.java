package com.emtdev.morph.http.process;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

import java.io.IOException;
import java.io.OutputStream;

public class MorphProcessEntityWrapper extends HttpEntityWrapper {
   /**
    * Creates a new entity wrapper.
    *
    * @param wrappedEntity the entity to wrap.
    */

   private final MorphProcessListener processListener;

   public MorphProcessEntityWrapper(HttpEntity wrappedEntity, MorphProcessListener processListener) {
      super(wrappedEntity);
      this.processListener = processListener;
   }

   @Override
   public void writeTo(OutputStream outStream) throws IOException {
      super.writeTo(new MorphProcessOutputStream(outStream, processListener, getContentLength()));
   }


}
