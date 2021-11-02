package com.blinkedgewillsun.dailyquotesapplication.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blinkedgewillsun.dailyquotesapplication.MainScreen.InternetActivity;

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
        {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (noConnectivity) {
                context.startActivity(new Intent(context, InternetActivity.class));
                ((AppCompatActivity)context).finish();
            } else {
                Log.d("ad______", "onReceive: connected");
            }
        }


    }
}
