package com.blinkedgewillsun.dailyquotesapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blinkedgewillsun.dailyquotesapplication.Adapter.PagerAdapter.PagerAdapter;
import com.blinkedgewillsun.dailyquotesapplication.R;
import com.google.android.material.tabs.TabLayout;

public class TabCateQuoteFragment extends Fragment {
    private TabLayout tabLayout ;
    private ViewPager viewPager ;
    private PagerAdapter pagerAdapter ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tab_cate_quote, container, false);
        innit(view);
        setUpTab();
        return view;
    }

    private void setUpTab() {
        pagerAdapter = new PagerAdapter(getChildFragmentManager(),tabLayout.getTabCount());


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition()==0 || tab.getPosition()==1)
                {
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(pagerAdapter);


    }

    private void innit(View view) {
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
    }
}