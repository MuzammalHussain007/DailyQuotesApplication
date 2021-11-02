
package com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.QuoteCategoryLikeShowResponse;

import java.util.List;
 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuoteCategoryLikeShow {

   @SerializedName("status")
   @Expose
   private Boolean status;
   @SerializedName("msg")
   @Expose
   private String msg;
   @SerializedName("category likes")
   @Expose
   private List<CategoryLike> categoryLikes = null;

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

   public List<CategoryLike> getCategoryLikes() {
       return categoryLikes;
   }

   public void setCategoryLikes(List<CategoryLike> categoryLikes) {
       this.categoryLikes = categoryLikes;
   }

}
