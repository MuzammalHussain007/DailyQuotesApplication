package com.blinkedgewillsun.dailyquotesapplication.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.blinkedgewillsun.dailyquotesapplication.Adapter.AllCatagoryCode;
import com.blinkedgewillsun.dailyquotesapplication.Interface.CatagoryToQuote;
import com.blinkedgewillsun.dailyquotesapplication.MainScreen.MasterActivity;
import com.blinkedgewillsun.dailyquotesapplication.R;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.AllCatagoryResponse.AllCatagory;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.AllCatagoryResponse.Category;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.RetrofitClient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CatagoryFragment extends Fragment implements CatagoryToQuote {
    private RecyclerView recyclerView;
    private List<Category> allCatagoryModals;
    private ProgressDialog progressDialog;
    private Toolbar toolbar ;
    private TextView textView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_catagory, container, false);
        innit(view);

        hitApi();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                ((AppCompatActivity)getContext()).getSupportFragmentManager().popBackStack();
                ((AppCompatActivity)getContext()).getSupportActionBar().setTitle("Category");   // TODO handle fragment back press this way
             }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
        return view;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Category");
    }



    private void hitApi() {
        progressDialog.show();
        RetrofitClient.getInstance().getapi().AllCatagory()
                .enqueue(new Callback<AllCatagory>() {
                    @Override
                    public void onResponse(Call<AllCatagory> call, Response<AllCatagory> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus()) {
                                    allCatagoryModals.addAll(response.body().getCategory());
                                    setupAdapter();
                                    progressDialog.dismiss();
                                } else {
                                    textView.setVisibility(View.VISIBLE);
                                    progressDialog.dismiss();

                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AllCatagory> call, Throwable t) {
                        Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        t.printStackTrace();
                        textView.setVisibility(View.VISIBLE);


                    }
                });
    }

    private void setupAdapter() {
        if (allCatagoryModals.isEmpty())
        {
            textView.setVisibility(View.VISIBLE);
        }else
        {

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            recyclerView.setAdapter(new AllCatagoryCode(allCatagoryModals, getContext(), this));
        }

    }

    private void innit(View view) {
        recyclerView = view.findViewById(R.id.catagory_recyclarview);
        allCatagoryModals = new ArrayList<>();
        progressDialog = new ProgressDialog(requireContext(), R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please Wait..");
        toolbar = view.findViewById(R.id.tool_bar);
        MasterActivity masterActivity = (MasterActivity) getActivity();
        masterActivity.getSupportActionBar().setTitle("Catagory");
        textView=view.findViewById(R.id.errorMesg);


    }

    @Override
    public void getrequiredQuote(int pos) {
        if (!allCatagoryModals.get(pos).getType().equals("Paid")) {
            ShowCatQuote fragment = new ShowCatQuote();
            Bundle bundle = new Bundle();
            bundle.putString("catid",allCatagoryModals.get(pos).getCatId());
            bundle.putString("catname",allCatagoryModals.get(pos).getCatName());
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.catagoryframe,fragment,"catagoryfragment").addToBackStack("catagoryfragment").commit();

        }
        else
        {
            getFragmentManager().beginTransaction().replace(R.id.catagoryframe,new SubscribeFragment(),"catagoryfragment").addToBackStack("catagoryfragment").commit();

        }

    }
}