package com.blinkedgewillsun.dailyquotesapplication.MainScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.blinkedgewillsun.dailyquotesapplication.OtherClass.UserUtils;
import com.blinkedgewillsun.dailyquotesapplication.R;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.RetrofitClient;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.SignUpResponse.SignUp;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText username, email, password;
    private Button resgistration;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int selectedID;
    private String radioValue;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private TextView textView;
    private UserUtils userUtils ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        innit();
        setListener();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
        }
        return true;
    }

    private boolean ischeckEmail() {
        String em = email.getText().toString();
        if (em.isEmpty()) {
            email.setError("No Email Found");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
            email.setError("Invalid Email Pattern");
            return false;
        }
        email.setError(null);
        return true;
    }

    private boolean ischeckPassword() {
        String pass = password.getText().toString();
        if (pass.isEmpty()) {
            password.setError("No Password Found");
            return false;
        } else if (password.getText().toString().length() < 5) {
            password.setError("Invalid password length");
            return false;
        }
        password.setError(null);
        return true;
    }

    private boolean ischeckUsername() {
        String em = username.getText().toString();
        if (em.isEmpty()) {
            username.setError("No username Found");
            return false;
        }
        email.setError(null);
        return true;
    }

    private boolean ischeckRadiobutton() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "No Gender Selected", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            selectedID = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(selectedID);
            radioValue = radioButton.getText().toString();
        }
        return true;
    }


    private void setListener() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        resgistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(!ischeckEmail() | !ischeckUsername() | !ischeckPassword() | !ischeckRadiobutton())) {
                    String user = username.getText().toString();
                    String em = email.getText().toString();
                    String pass = password.getText().toString();
                    hitApi(user, em, pass);
                }
            }
        });

    }

    private void hitApi(String user, String em, String pass) {
        progressDialog.show();
        RetrofitClient.getInstance().getapi().SignUp(em, pass, user, radioValue)
                .enqueue(new Callback<SignUp>() {
                    @Override
                    public void onResponse(Call<SignUp> call, Response<SignUp> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus()) {
                                    progressDialog.dismiss();
                                    email.setText("");
                                    username.setText("");
                                    password.setText("");
                                    radioButton.setChecked(false);
                                    userUtils.setUserID(""+response.body().getUserId());
                                    userUtils.setUserStatus("true");
                                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SignUp> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });
    }

    private void innit() {
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        resgistration = findViewById(R.id.registration);
        radioGroup = findViewById(R.id.radio_group);
        progressDialog = new ProgressDialog(RegisterActivity.this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please Wait..");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        textView = findViewById(R.id.already_account);
        userUtils = new UserUtils(getApplicationContext());

    }


}