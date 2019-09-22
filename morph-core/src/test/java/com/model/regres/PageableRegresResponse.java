package com.model.regres;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageableRegresResponse<T> extends RegresResponse<T> {

   private int page;
   private long total;

   @JsonProperty("per_page")
   private int perPage;

   @JsonProperty("total_pages")
   private int totalPage;

   public int getPage() {
      return page;
   }

   public void setPage(int page) {
      this.page = page;
   }

   public int getPerPage() {
      return perPage;
   }

   public void setPerPage(int perPage) {
      this.perPage = perPage;
   }

   public long getTotal() {
      return total;
   }

   public void setTotal(long total) {
      this.total = total;
   }

   public int getTotalPage() {
      return totalPage;
   }

   public void setTotalPage(int totalPage) {
      this.totalPage = totalPage;
   }


}
