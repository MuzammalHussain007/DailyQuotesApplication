package com.blinkedgewillsun.dailyquotesapplication.MainScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.blinkedgewillsun.dailyquotesapplication.Broadcast.NetworkReceiver;
import com.blinkedgewillsun.dailyquotesapplication.Fragments.CatagoryFragment;
import com.blinkedgewillsun.dailyquotesapplication.Fragments.FavouriteFragment;
import com.blinkedgewillsun.dailyquotesapplication.Fragments.HomeFragment;
import com.blinkedgewillsun.dailyquotesapplication.OtherClass.UserUtils;
import com.blinkedgewillsun.dailyquotesapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MasterActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private BroadcastReceiver broadcastReceiver ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.master_menue,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.rate_us:
            {
                rate_app();
                break;
            }
            case R.id.share_app:
            {
                    share_app();
                break;
            }
            case R.id.privacy:
            {
                privacy_policy();
                break;
            }
            case R.id.more_app:
            {
                moreApps();
                break;
            }
            case R.id.logout:
            {

                break;
            }

            case R.id.login:
            {
                startActivity(new Intent(MasterActivity.this,LoginActivity.class));
                break;
            }
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logout = menu.findItem(R.id.logout);
        MenuItem login = menu.findItem(R.id.login);
        UserUtils userUtils = new UserUtils(getApplicationContext());
        if (userUtils.getUserStatus().equals("true"))
        {
            logout.setVisible(false);
            login.setVisible(true);
        }
        {
            login.setVisible(false);
            logout.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }



    private void moreApps() {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Mine+Apps+Studio"));
        startActivity(i);
    }

    private void privacy_policy() {

    }

    private void share_app() {
        try {
            Intent IntentShare = new Intent(Intent.ACTION_SEND);
            IntentShare.setType("text/plain");
            IntentShare.putExtra(Intent.EXTRA_SUBJECT, "Pubg Guide");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
            IntentShare.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(IntentShare, "choose one"));
        } catch (Exception e) {
        }
    }

    private void rate_app() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        innit();
        setSupportActionBar(toolbar);

        setTransaction(new HomeFragment(),"homeFragment");
        getSupportActionBar().setTitle("Home");
        broadcastReceiver = new NetworkReceiver();
        registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));




        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                 @Override
                 public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                     switch (item.getItemId()) {
                         case R.id.home: {
                             setTransaction(new HomeFragment(),"homeFragment");
                             break;
                         }
                         case R.id.catagory: {
                             setTransaction(new CatagoryFragment(),"catagoryFragment");
                             break;
                         }
                         case R.id.faviorite: {
                             setTransaction(new FavouriteFragment(),"favouriteFragment");
                             break;
                         }
                         default: {
                             return false;
                         }
                     }
                     return true;
                 }
             });
    }

      private void setTransaction(Fragment fragment ,String tag)
      {
          getSupportFragmentManager().beginTransaction().replace(R.id.master_transaction,fragment,tag).commit();
      }
    private void innit() {
        toolbar = findViewById(R.id.tool_bar);
        bottomNavigationView = findViewById(R.id.bottom_nav);
    }
}