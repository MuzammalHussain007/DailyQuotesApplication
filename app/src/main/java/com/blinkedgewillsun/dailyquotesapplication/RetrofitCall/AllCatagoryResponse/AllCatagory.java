
package com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.AllCatagoryResponse;

import java.util.List;
 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllCatagory {

   @SerializedName("status")
   @Expose
   private Boolean status;
   @SerializedName("msg")
   @Expose
   private String msg;
   @SerializedName("Category")
   @Expose
   private List<Category> category = null;

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

   public List<Category> getCategory() {
       return category;
   }

   public void setCategory(List<Category> category) {
       this.category = category;
   }

}
