package com.blinkedgewillsun.dailyquotesapplication.MainScreen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;

import com.blinkedgewillsun.dailyquotesapplication.Broadcast.NetworkReceiver;
import com.blinkedgewillsun.dailyquotesapplication.R;


public class SplashScreen extends AppCompatActivity {
    private BroadcastReceiver broadcastReceiver ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        broadcastReceiver = new NetworkReceiver();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    startActivity(new Intent(SplashScreen.this, MasterActivity.class));
                    finish();
            }
        },3000);
    }
}