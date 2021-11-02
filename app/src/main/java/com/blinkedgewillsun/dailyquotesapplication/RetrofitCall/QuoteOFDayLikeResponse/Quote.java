
package com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.QuoteOFDayLikeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quote {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("Quote of the day")
    @Expose
    private String quoteOfTheDay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuoteOfTheDay() {
        return quoteOfTheDay;
    }

    public void setQuoteOfTheDay(String quoteOfTheDay) {
        this.quoteOfTheDay = quoteOfTheDay;
    }

}
