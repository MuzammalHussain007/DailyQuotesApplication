
package com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.CatagoryToQuoteResponse;

import java.util.List;
 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quote {

   @SerializedName("status")
   @Expose
   private Boolean status;
   @SerializedName("msg")
   @Expose
   private String msg;
   @SerializedName("quote")
   @Expose
   private List<Quote__1> quote = null;

   public Boolean getStatus() {
       return status;
   }

   public void setStatus(Boolean status) {
       this.status = status;
   }

   public String getMsg() {
       return msg;
   }

   public void setMsg(String msg) {
       this.msg = msg;
   }

   public List<Quote__1> getQuote() {
       return quote;
   }

   public void setQuote(List<Quote__1> quote) {
       this.quote = quote;
   }

}
