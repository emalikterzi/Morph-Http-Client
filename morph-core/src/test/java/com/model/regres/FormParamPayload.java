package com.model.regres;

import javax.ws.rs.FormParam;

public class FormParamPayload {

   @FormParam("objectPayload")
   private String objectPayload;

   public FormParamPayload(String objectPayload) {
      this.objectPayload = objectPayload;
   }

   public String getObjectPayload() {
      return objectPayload;
   }

   public void setObjectPayload(String objectPayload) {
      this.objectPayload = objectPayload;
   }
}
