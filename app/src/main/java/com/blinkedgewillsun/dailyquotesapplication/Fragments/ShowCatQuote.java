package com.blinkedgewillsun.dailyquotesapplication.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.blinkedgewillsun.dailyquotesapplication.Adapter.CatagoryQuoteShowAdapter;
import com.blinkedgewillsun.dailyquotesapplication.OtherClass.UserUtils;
import com.blinkedgewillsun.dailyquotesapplication.R;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.CatagoryToQuoteResponse.Quote;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.CatagoryToQuoteResponse.Quote__1;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShowCatQuote extends Fragment {
    private RecyclerView recyclerView ;
    private List<Quote__1> quote__1List ;
    private String catagory_name  , cate_id;
    private ProgressDialog progressDialog ;
    private TextView textView ;
    private UserUtils userUtils ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_show_cat_quote, container, false);
        innit(view);
        hitApi();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
            {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void hitApi() {
        progressDialog.show();
        RetrofitClient.getInstance().getapi().QuoteShow(catagory_name, userUtils.getUserId())
                .enqueue(new Callback<Quote>() {
                    @Override
                    public void onResponse(Call<Quote> call, Response<Quote> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body()!=null)
                            {
                                if (response.body().getStatus())
                                {
                                    progressDialog.dismiss();
                                    quote__1List.addAll(response.body().getQuote());
                                    if (!quote__1List.isEmpty())
                                    {
                                        setAdzapter();
                                    }else
                                    {
                                        textView.setVisibility(View.VISIBLE);
                                        progressDialog.dismiss();

                                    }


                                }else
                                {
                                    Toast.makeText(getContext(), ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    textView.setVisibility(View.VISIBLE);

                                }
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<Quote> call, Throwable t) {
                        Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                        progressDialog.dismiss();
                        textView.setVisibility(View.VISIBLE);


                    }
                });

    }

    private void setAdzapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new CatagoryQuoteShowAdapter(getContext(),quote__1List , cate_id));
    }

    private void innit(View view) {
        recyclerView = view.findViewById(R.id.showcat_recyclar_view);
        quote__1List = new ArrayList<>();
        progressDialog = new ProgressDialog(requireContext(),R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please Wait..");
        textView = view.findViewById(R.id.error_text);
        catagory_name = getArguments().getString("catname");
        cate_id = getArguments().getString("catid");
        userUtils = new UserUtils(getContext());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(catagory_name);
    }
}