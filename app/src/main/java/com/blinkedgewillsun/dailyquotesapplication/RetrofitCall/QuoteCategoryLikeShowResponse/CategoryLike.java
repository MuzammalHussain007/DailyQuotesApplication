
package com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.QuoteCategoryLikeShowResponse;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryLike {
    @SerializedName("Name")
    @Expose
    private String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @SerializedName("type")
    @Expose
    private String type;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

   @SerializedName("cat_like_id")
   @Expose
   private String catLikeId;
   @SerializedName("cat_id")
   @Expose
   private String catId;
   @SerializedName("value")
   @Expose
   private String value;

   public String getCatLikeId() {
       return catLikeId;
   }

   public void setCatLikeId(String catLikeId) {
       this.catLikeId = catLikeId;
   }

   public String getCatId() {
       return catId;
   }

   public void setCatId(String catId) {
       this.catId = catId;
   }

   public String getValue() {
       return value;
   }

   public void setValue(String value) {
       this.value = value;
   }

}
