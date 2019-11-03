package com.emtdev.morph.http.process;

import java.io.IOException;
import java.io.OutputStream;

public class MorphProcessOutputStream extends OutputStream {

   private final OutputStream origin;
   private final MorphProcessListener processListener;
   private final long totalByte;
   private long transferred;

   public MorphProcessOutputStream(OutputStream origin, MorphProcessListener processListener, long totalByte) {
      this.origin = origin;
      this.processListener = processListener;
      this.totalByte = totalByte;
   }

   @Override
   public void write(int b) throws IOException {
      origin.write(b);
      transferred++;
      processListener.apply(transferred, totalByte);
   }


   @Override
   public void flush() throws IOException {
      origin.flush();
   }

   @Override
   public void close() throws IOException {
      origin.close();
   }
}
