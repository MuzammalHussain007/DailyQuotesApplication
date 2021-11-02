package com.blinkedgewillsun.dailyquotesapplication.MainScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.blinkedgewillsun.dailyquotesapplication.R;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.Responses.LoginResponse.LoginForm;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText email,password;
    private Button login;
    private ProgressDialog progressDialog ;
    private TextView forgetpassword , move_to_sign_up;
    private Toolbar toolbar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        innit();
        setListener();
    }

    private void setListener() {
        move_to_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(!ischeckEmail()|!ischeckPassword()))
                {
                    progressDialog.show();
                    String name =  email.getText().toString();
                    String passs = password.getText().toString();
                    setLoginApi(name,passs);
                    startActivity(new Intent(LoginActivity.this,MasterActivity.class));
                    finish();
                 }

            }
        });
    }

    private void setLoginApi(String name, String passs) {
        progressDialog.show();
        RetrofitClient.getInstance().getapi().Login(name,passs).enqueue(new Callback<LoginForm>() {
            @Override
            public void onResponse(Call<LoginForm> call, Response<LoginForm> response) {
                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        if (response.body().getStatus())
                        {
                            email.setText("");
                            password.setText("");
                            progressDialog.dismiss();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginForm> call, Throwable t) {
                Toast.makeText(getApplicationContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });

    }

    private boolean ischeckEmail() {
        String em = email.getText().toString();
        if (em.isEmpty())
        {
            email.setError("No Email Found");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(em).matches())
        {
            email.setError("Invalid Email Pattern");
            return false;
        }
        email.setError(null);
        return true;
    }

    private boolean ischeckPassword() {
        String pass = password.getText().toString();
        if (pass.isEmpty())
        {
            password.setError("No Password Found");
            return false;
        }else if (password.getText().toString().length() < 5)
        {
            password.setError("Invalid password length");
            return false;
        }
        password.setError(null);
        return true;
    }

    private void innit() {
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_button);
        progressDialog = new ProgressDialog(LoginActivity.this,R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please Wait");
        forgetpassword = findViewById(R.id.forgetpassword);
        move_to_sign_up = findViewById(R.id.move_to_sign_up);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

    }
}