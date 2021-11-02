package com.blinkedgewillsun.dailyquotesapplication.Fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.blinkedgewillsun.dailyquotesapplication.OtherClass.UserUtils;
import com.blinkedgewillsun.dailyquotesapplication.R;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.PaymentApiResponse.BraintreeToken;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.RetrofitClient;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageDetailFragment extends Fragment {
    private static final int REQUEST_CODE = 1;
    private TextView package_price, package_duration, package_description;
    private Button subs_btn;
    private ProgressDialog progressDialog;
    private String price ;
    private String pkgID;

    String nonce = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package_detail, container, false);
        innit(view);
        subs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               hitBraintreeTokenApi();
            }
        });
        return view;
    }

    private void hitBraintreeTokenApi() {
        progressDialog.show();
        RetrofitClient.getInstance().getapi().braintreeTokenApi().enqueue(new Callback<BraintreeToken>() {
            @Override
            public void onResponse(Call<BraintreeToken> call, Response<BraintreeToken> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            DropInRequest dropInRequest = new DropInRequest().clientToken(response.body().getToken());
                            dropInRequest.disablePayPal();
                            startActivityForResult(dropInRequest.getIntent(getContext()), REQUEST_CODE);
                            progressDialog.dismiss();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BraintreeToken> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentMethodNonce = result.getPaymentMethodNonce().getNonce();
                setCheckOutApi(new UserUtils(getContext()).getUserId(), pkgID, "1", price, paymentMethodNonce);

            }
        } else if (resultCode == RESULT_CANCELED) {

        } else {
            Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
            Toast.makeText(getContext(), "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("abd____", "" + exception.getMessage());
        }
    }

    private void setCheckOutApi(String userID, String paymentid, String paymentType, String price, String nonce) {
        RetrofitClient.getInstance().getapi().checkout(userID, paymentid, paymentType, price, nonce).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String string = null;
                        try {
                            string = response.body().string();
                            JSONObject jsonObject = new JSONObject(string);
                            boolean status = jsonObject.getBoolean("status");
                            String msg = jsonObject.getString("msg");

                            if (status) {
                                Toast.makeText(getContext(), "Transaction Success", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(getContext(), "" + msg, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

    private void innit(View view) {
        package_duration = view.findViewById(R.id.days);
        subs_btn = view.findViewById(R.id.btn_subscribe);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            price = getArguments().getString("price");
            pkgID = getArguments().getString("id");
            Log.d("fsd____", ""+pkgID);
        }
        progressDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please Wait");


    }
}