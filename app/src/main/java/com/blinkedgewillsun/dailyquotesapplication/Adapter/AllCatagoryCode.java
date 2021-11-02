package com.blinkedgewillsun.dailyquotesapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.blinkedgewillsun.dailyquotesapplication.Interface.CatagoryToQuote;
import com.blinkedgewillsun.dailyquotesapplication.MainScreen.LoginActivity;
import com.blinkedgewillsun.dailyquotesapplication.OtherClass.UserUtils;
import com.blinkedgewillsun.dailyquotesapplication.R;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.AddCateGoryFavourite.CategoryFavourite;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.AllCatagoryResponse.Category;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.RetrofitClient;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCatagoryCode extends RecyclerView.Adapter<AllCatagoryCode.AllCatagoryHolder> {
    private List<Category> allCatagoryModals;
    private Context context;
    private CatagoryToQuote catagoryToQuote;
    private int i = 0;
    private UserUtils userUtils;
    private int[] color = new int[]{R.color.black, R.color.Aquamarine, R.color.Brown, R.color.BlanchedAlmond};

    public AllCatagoryCode(List<Category> allCatagoryModals, Context context, CatagoryToQuote catagoryToQuote) {
        this.allCatagoryModals = allCatagoryModals;
        this.context = context;
        this.catagoryToQuote = catagoryToQuote;
        userUtils = new UserUtils(context);
    }

    @NonNull
    @Override
    public AllCatagoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_all_catagory, parent, false);
        return new AllCatagoryHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull AllCatagoryHolder holder, int position) {
        if (userUtils.getUserStatus().equals("true")) {
            ClickListener(holder);

        } else {
            context.startActivity(new Intent(context, LoginActivity.class));
            ((AppCompatActivity) context).finish();
        }


        if (allCatagoryModals.get(holder.getAbsoluteAdapterPosition()).getType().equals("Paid")) {
            holder.crawon.setVisibility(View.VISIBLE);
        }
        holder.name.setText(allCatagoryModals.get(holder.getAbsoluteAdapterPosition()).getCatName());
        String name = allCatagoryModals.get(holder.getAbsoluteAdapterPosition()).getCatName().substring(0, 1);
        holder.first_letter.setText(name);


    }

    private void ClickListener(AllCatagoryHolder holder) {
        holder.not_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.favourite.setVisibility(View.VISIBLE);
                holder.not_favourite.setVisibility(View.GONE);
                 hitCategoryFav(userUtils.getUserId(), allCatagoryModals.get(holder.getAbsoluteAdapterPosition()).getCatId(), "false");
            }
        });

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.favourite.setVisibility(View.GONE);
                holder.not_favourite.setVisibility(View.VISIBLE);
                hitCategoryFav(userUtils.getUserId(), allCatagoryModals.get(holder.getAbsoluteAdapterPosition()).getCatId(), "true");

            }
        });

    }

    private void hitCategoryFav(String id, String catId, String value) {
        RetrofitClient.getInstance().getapi().addCategoryFavourite(id, catId, value).enqueue(new Callback<CategoryFavourite>() {
            @Override
            public void onResponse(Call<CategoryFavourite> call, Response<CategoryFavourite> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {

                        } else {
                            Toast.makeText(context, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryFavourite> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return allCatagoryModals.size();
    }

    public class AllCatagoryHolder extends RecyclerView.ViewHolder {
        private TextView name, first_letter;
        private ImageView crawon, not_favourite, favourite;

        public AllCatagoryHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.category_name);
            crawon = itemView.findViewById(R.id.crown);
            favourite = itemView.findViewById(R.id.favourite);
            not_favourite = itemView.findViewById(R.id.not_fav);


            first_letter = itemView.findViewById(R.id.first_letter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    catagoryToQuote.getrequiredQuote(getAbsoluteAdapterPosition());
                }
            });

        }
    }
}
