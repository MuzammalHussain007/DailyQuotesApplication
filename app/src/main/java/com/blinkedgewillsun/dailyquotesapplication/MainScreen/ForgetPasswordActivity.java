package com.blinkedgewillsun.dailyquotesapplication.MainScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.blinkedgewillsun.dailyquotesapplication.R;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.ForgetPasswordResponse.ForgetPassword;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText email ;
    private Button forgetPasswordbtn;
    private ProgressDialog progressDialog ;
    private Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        innit();
        clickListener();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                finish();
            }
        }
        return true;
    }

    private void clickListener() {
        forgetPasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = email.getText().toString();
                if (verifyEmail(em))
                {
                    hitApi(em);
                 }
            }
        });

    }

    private void hitApi(String em) {
        progressDialog.show();
        RetrofitClient.getInstance().getapi().ForgetPassword(em).enqueue(new Callback<ForgetPassword>() {
            @Override
            public void onResponse(Call<ForgetPassword> call, Response<ForgetPassword> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getStatus())
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Please Check Your Email", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                                finish();
                            }
                        },3000);
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ForgetPassword> call, Throwable t) {
                Toast.makeText(getApplicationContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean verifyEmail(String em)
    {
        if (em.isEmpty())
        {
            email.setError("Field is Empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(em).matches())
        {
            email.setError("Invalid email address");
            return false;
        }
        else
        {
            email.setError(null);
        }
        return true;

    }

    private void innit() {
        email = findViewById(R.id.forget_password);
        forgetPasswordbtn = findViewById(R.id.forget_btn);
        progressDialog = new ProgressDialog(ForgetPasswordActivity.this,R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please Wait");
        toolbar = findViewById(R.id.forget_pass);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Forget Password");
    }
}