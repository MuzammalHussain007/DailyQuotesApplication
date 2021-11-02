package com.blinkedgewillsun.dailyquotesapplication.Adapter.PagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.blinkedgewillsun.dailyquotesapplication.Fragments.FavouriteFragment;


public class PagerAdapter extends FragmentPagerAdapter {
    private int tabcount;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount = behavior;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
            {
                return new FavouriteFragment();
            }
            case 1:
            {
                return new FavouriteFragment();
            }

        }
        return null;

    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
