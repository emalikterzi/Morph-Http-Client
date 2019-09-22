package com.emt.morph.impl;

import com.emt.morph.HttpResponseCloser;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class HttpResponseCloserImpl implements HttpResponseCloser, Runnable {

   private AtomicBoolean active = new AtomicBoolean(true);

   private Queue<CloseableHttpResponse> closeableHttpResponses = new ConcurrentLinkedQueue<>();

   public HttpResponseCloserImpl() {
      Executors.newSingleThreadExecutor()
              .execute(this);
   }

   @Override
   public void addToQueue(CloseableHttpResponse closeableHttpResponse) {
      if (active.get())
         closeableHttpResponses.add(closeableHttpResponse);
   }

   @Override
   public void closeAll() {
      active.set(false);
      while (!closeableHttpResponses.isEmpty()) {
         CloseableHttpResponse closeableHttpResponse = closeableHttpResponses.poll();
         close(closeableHttpResponse);
      }
   }

   private void close(CloseableHttpResponse closeableHttpResponse) {
      try {
         closeableHttpResponse.close();
      } catch (Exception e) {
         //swallow
      }
   }

   @Override
   public void run() {
      while (active.get()) {

         if (closeableHttpResponses.isEmpty()) {
            try {
               Thread.sleep(100);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            continue;
         }


         while (!closeableHttpResponses.isEmpty()) {
            CloseableHttpResponse closeableHttpResponse = closeableHttpResponses.poll();
            close(closeableHttpResponse);
         }

      }
   }
}
