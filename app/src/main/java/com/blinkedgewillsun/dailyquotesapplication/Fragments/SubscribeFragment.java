package com.blinkedgewillsun.dailyquotesapplication.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.blinkedgewillsun.dailyquotesapplication.Adapter.PackageAdapter;
import com.blinkedgewillsun.dailyquotesapplication.Interface.MoveToFragment;
import com.blinkedgewillsun.dailyquotesapplication.MainScreen.MasterActivity;
import com.blinkedgewillsun.dailyquotesapplication.R;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.PackagesResponse.Package__1;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribeFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Package__1> package__1List;
    private ProgressDialog progressDialog;
    private TextView textView;
     private int postion = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);
        innit(view);
        ((MasterActivity) getActivity()).getSupportActionBar().setTitle("Subscribe Package");
        hitApi();
        return view;
    }

    private void hitApi() {
        progressDialog.show();
        RetrofitClient.getInstance().getapi().AllPackage()
                .enqueue(new Callback<com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.PackagesResponse.Package>() {
                    @Override
                    public void onResponse(Call<com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.PackagesResponse.Package> call, Response<com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.PackagesResponse.Package> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus()) {
                                    progressDialog.dismiss();
                                    package__1List.addAll(response.body().getPackage());
                                    if (package__1List.isEmpty()) {
                                        progressDialog.dismiss();
                                        textView.setVisibility(View.VISIBLE);
                                    }
                                    setAdapter();
                                } else {
                                    progressDialog.dismiss();
                                    textView.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.PackagesResponse.Package> call, Throwable t) {
                        Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        textView.setVisibility(View.VISIBLE);

                    }
                });
    }

    private void setAdapter() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(new PackageAdapter(getContext(), package__1List));
        if (!package__1List.isEmpty()) {
            PackageDetailFragment packageDetailFragment = new PackageDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title", package__1List.get(0).getCatName());
            bundle.putString("price", package__1List.get(0).getPrice());
            bundle.putString("abw",package__1List.get(0).getPkgId());
            bundle.putString("duration", package__1List.get(0).getDuration());
            bundle.putString("description", package__1List.get(0).getDescription());
            packageDetailFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.change_frame, packageDetailFragment, "packageDetailFragment").commit();
        }
    }

    private void innit(View view) {
        recyclerView = view.findViewById(R.id.subs_recyclar_view);
        package__1List = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please Wait..");
        textView = view.findViewById(R.id.error_text);


    }

}