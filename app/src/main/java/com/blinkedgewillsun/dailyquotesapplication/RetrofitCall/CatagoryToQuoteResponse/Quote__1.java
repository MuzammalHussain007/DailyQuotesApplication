
package com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.CatagoryToQuoteResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quote__1 {
    @SerializedName("font")
    @Expose
    private String font;
    @SerializedName("img")
    @Expose
    private String img;

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @SerializedName("user_liked")
    @Expose
    private String userLiked;

    public String getUserLiked() {
        return userLiked;
    }

    public void setUserLiked(String userLiked) {
        this.userLiked = userLiked;
    }

    @SerializedName("quote_id")
    @Expose
    private String quoteId;

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    @SerializedName("author_name")
    @Expose
    private String authorName;
    @SerializedName("text")
    @Expose
    private String text;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
