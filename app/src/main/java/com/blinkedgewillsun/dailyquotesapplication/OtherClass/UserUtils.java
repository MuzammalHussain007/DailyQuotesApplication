package com.blinkedgewillsun.dailyquotesapplication.OtherClass;

import android.content.Context;
import android.content.SharedPreferences;

public class UserUtils {
    private static final String FILE_NAME ="Data" ;
    private static final String USER_ID ="userID" ;
    private static final String USER_STATUS ="UserStatus" ;
    private static final String USER_PAYMENT_PAY = "UserPayment";
    private SharedPreferences sharedPreferences ;
    private SharedPreferences.Editor editor ;
    private Context context  ;

    public UserUtils(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUserID(String userID)
    {
        editor.putString(USER_ID,userID).commit();
    }
    public String getUserId()
    {
        return sharedPreferences.getString(USER_ID,"");
    }

    public void setUserStatus(String userStatus)
    {
        editor.putString(USER_STATUS,userStatus).commit();
    }
    public String getUserStatus()
    {
        return sharedPreferences.getString(USER_STATUS,"false");
    }

    public void setPaymentPay(String paymentPay)
    {
        editor.putString(USER_PAYMENT_PAY,paymentPay).commit();
    }
    public String getPaymentPay()
    {
        return sharedPreferences.getString(USER_PAYMENT_PAY,"false");
    }

}
