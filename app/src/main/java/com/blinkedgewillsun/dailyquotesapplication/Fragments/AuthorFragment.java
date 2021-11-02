package com.blinkedgewillsun.dailyquotesapplication.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.blinkedgewillsun.dailyquotesapplication.Modal.AllCatagoryModal;
import com.blinkedgewillsun.dailyquotesapplication.R;

import java.util.ArrayList;
import java.util.List;

public class AuthorFragment extends Fragment {
    private SearchView scrollView ;
    private RecyclerView recyclerView;
    private List<AllCatagoryModal> allCatagoryModals ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_author, container, false);
        innit(view);
        setupAdapter();
        return view;
    }


    private void setupAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        allCatagoryModals.add(new AllCatagoryModal("","Muzammal Hussain","50"));
//        recyclerView.setAdapter(new AllCatagoryCode(allCatagoryModals,getContext()));

    }

    private void innit(View view) {
        scrollView = view.findViewById(R.id.search_view);
        recyclerView = view.findViewById(R.id.catagory_recyclarview);
        allCatagoryModals = new ArrayList<>();

    }
}