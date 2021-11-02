package com.blinkedgewillsun.dailyquotesapplication.RetrofitCall;




import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.AddCateGoryFavourite.CategoryFavourite;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.AllCatagoryResponse.AllCatagory;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.BackGroundResponse.BackGround;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.CatagoryToQuoteResponse.Quote;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.FavouriteResponse.Favourite;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.FontResponse.Font;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.ForgetPasswordResponse.ForgetPassword;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.PaymentApiResponse.BraintreeToken;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.QuoteCategoryLikeShowResponse.QuoteCategoryLikeShow;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.QuotesofDayResponse.QuotesofDay;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.Responses.LoginResponse.LoginForm;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.ShowQuoteFavouriteResponse.FavouriteQuotes;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.SignUpResponse.SignUp;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APISERVICES {

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginForm> Login(
            @Field("user_email") String email,
            @Field("user_password") String password
    );

    @FormUrlEncoded
    @POST("signup.php")
    Call<SignUp> SignUp(
            @Field("user_email") String email,
            @Field("user_password") String password,
            @Field("user_name") String username,
            @Field("gender") String gender
    );

     @GET("category.php")
    Call<AllCatagory> AllCatagory();

    @GET("package.php")
    Call<com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.PackagesResponse.Package> AllPackage();


    @FormUrlEncoded
    @POST("quote.php")
    Call<Quote> QuoteShow(
            @Field("cat_name") String catagory,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("forgot_password.php")
    Call<ForgetPassword> ForgetPassword(
            @Field("user_email") String email
    );

    @GET("day_quote.php")
    Call<QuotesofDay> SingalQuotesOfDay();


    @FormUrlEncoded
    @POST("like.php")
    Call<Favourite> AddToFavourite(
        @Field("user_id") String userid,
        @Field("cat_id") String category_id,
        @Field("quote_id") String quote_id,
        @Field("value") String value
    );



    @FormUrlEncoded
    @POST("fetch_like.php")
    Call<FavouriteQuotes> showFavourite(
            @Field("user_id") String userid
    );


    @FormUrlEncoded
    @POST("cat_like.php")
    Call<CategoryFavourite> addCategoryFavourite(
            @Field("user_id") String userid,
            @Field("cat_id") String cateID,
            @Field("value") String value
    );

    @FormUrlEncoded
    @POST("fetch_cat_like.php")
    Call<QuoteCategoryLikeShow> showCategoryFavourite(
            @Field("user_id") String userid
    );

    @FormUrlEncoded
    @POST("font_insert.php")
    Call<Font> setFont(
            @Field("user_id") String userid,
            @Field("quote_id") String quoteid,
            @Field("font") String fontStyle
    );

    @FormUrlEncoded
    @POST("image_insert.php")
    Call<BackGround> setBackground(
            @Field("user_id") String userid,
            @Field("quote_id") String quoteid,
            @Field("image") String fontStyle
    );


    @FormUrlEncoded
    @POST("braintree/checkout.php")
    Call<ResponseBody> checkout(
            @Field("user_id") String userid,
            @Field("pkg_id") String packageid,
            @Field("payment_type") String paymentType,
            @Field("total_amount") String amount,
            @Field("nonce") String nonce
    );
    @GET("braintree/main.php")
    Call<BraintreeToken> braintreeTokenApi();













}
