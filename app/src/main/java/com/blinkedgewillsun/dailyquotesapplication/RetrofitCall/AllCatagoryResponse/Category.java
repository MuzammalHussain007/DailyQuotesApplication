
package com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.AllCatagoryResponse;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("cat_id")
    @Expose
    private String catId;
    public String getCatId() {
        return catId;
    }
    public void setCatId(String catId) {
        this.catId = catId;
    }

   @SerializedName("cat_name")
   @Expose
   private String catName;
   @SerializedName("type")
   @Expose
   private String type;

   public String getCatName() {
       return catName;
   }

   public void setCatName(String catName) {
       this.catName = catName;
   }

   public String getType() {
       return type;
   }

   public void setType(String type) {
       this.type = type;
   }

}
