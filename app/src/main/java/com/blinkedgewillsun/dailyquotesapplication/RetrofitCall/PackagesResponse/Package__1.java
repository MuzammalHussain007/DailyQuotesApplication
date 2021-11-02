
package com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.PackagesResponse;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Package__1 {
    @SerializedName("pkg_id")
    @Expose
    private String pkgId;

    @SerializedName("cat_name")
    @Expose
    private String catName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("description")
    @Expose
    private String description;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }


    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getPkgId() {
        return pkgId;
    }

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

}

