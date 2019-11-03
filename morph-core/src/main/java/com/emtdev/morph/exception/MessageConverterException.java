package com.emtdev.morph.exception;

public class MessageConverterException extends MorphException {


   public MessageConverterException() {
   }

   public MessageConverterException(String message) {
      super(message);
   }

   public MessageConverterException(String message, Throwable cause) {
      super(message, cause);
   }

   public MessageConverterException(Throwable cause) {
      super(cause);
   }

   public MessageConverterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }
}
