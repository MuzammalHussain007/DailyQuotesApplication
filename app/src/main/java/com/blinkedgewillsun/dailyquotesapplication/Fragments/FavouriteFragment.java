package com.blinkedgewillsun.dailyquotesapplication.Fragments;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.blinkedgewillsun.dailyquotesapplication.Adapter.FavouriteAdapter;
import com.blinkedgewillsun.dailyquotesapplication.OtherClass.UserUtils;
import com.blinkedgewillsun.dailyquotesapplication.R;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.RetrofitClient;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.ShowQuoteFavouriteResponse.FavouriteQuotes;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.ShowQuoteFavouriteResponse.Like;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteFragment extends Fragment {
    private List<Like> favouriteList;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog ;
    private TextView textView ;
    private UserUtils userUtils;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        innit(view);
        hitFavApi();
        clickListener();
        return view;
    }

    private void hitFavApi() {
        progressDialog.show();
        RetrofitClient.getInstance().getapi().showFavourite(userUtils.getUserId()).enqueue(new Callback<FavouriteQuotes>() {
            @Override
            public void onResponse(Call<FavouriteQuotes> call, Response<FavouriteQuotes> response) {
                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        if (response.body().getStatus())
                        {
                            favouriteList.addAll(response.body().getLikes());
                            progressDialog.dismiss();
                            setUpfavouriteRecyclarView();

                        }else
                        {
                            textView.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FavouriteQuotes> call, Throwable t) {
                Toast.makeText(getContext(),""+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void clickListener() {

    }

    private void setUpfavouriteRecyclarView() {
        if (favouriteList.isEmpty())
        {
            textView.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new FavouriteAdapter(favouriteList, getContext()));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Favourite");
        super.onCreate(savedInstanceState);
    }

    private void innit(View view) {
        recyclerView = view.findViewById(R.id.favourite_recylar_view);
        favouriteList = new ArrayList<>();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Favourite");
        progressDialog = new ProgressDialog(getContext(),R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please Wait");
        textView = view.findViewById(R.id.error_favourite);
        userUtils = new UserUtils(getContext());
    }

}