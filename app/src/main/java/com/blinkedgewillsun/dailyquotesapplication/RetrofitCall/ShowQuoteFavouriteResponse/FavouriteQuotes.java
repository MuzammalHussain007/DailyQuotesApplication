
package com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.ShowQuoteFavouriteResponse;

import java.util.List;
 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavouriteQuotes {

   @SerializedName("status")
   @Expose
   private Boolean status;
   @SerializedName("msg")
   @Expose
   private String msg;
   @SerializedName("Likes")
   @Expose
   private List<Like> likes = null;

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

   public List<Like> getLikes() {
       return likes;
   }

   public void setLikes(List<Like> likes) {
       this.likes = likes;
   }

}
