
package com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.PackagesResponse;

import java.util.List;
 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Package {

   @SerializedName("status")
   @Expose
   private Boolean status;
   @SerializedName("msg")
   @Expose
   private String msg;
   @SerializedName("Package")
   @Expose
   private List<Package__1> _package = null;

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

   public List<Package__1> getPackage() {
       return _package;
   }

   public void setPackage(List<Package__1> _package) {
       this._package = _package;
   }

}
