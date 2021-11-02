package com.blinkedgewillsun.dailyquotesapplication.Adapter.HomeScreen;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.blinkedgewillsun.dailyquotesapplication.Interface.CatagoryToQuote;
import com.blinkedgewillsun.dailyquotesapplication.Interface.MoveToFragmentCategory;
import com.blinkedgewillsun.dailyquotesapplication.MainScreen.LoginActivity;
import com.blinkedgewillsun.dailyquotesapplication.OtherClass.UserUtils;
import com.blinkedgewillsun.dailyquotesapplication.R;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.AddCateGoryFavourite.CategoryFavourite;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.QuoteCategoryLikeShowResponse.CategoryLike;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedCatagory extends RecyclerView.Adapter<SavedCatagory.SavedCategoryHolder> {
    private Context context;
    private List<CategoryLike> saved_categoryList;
    private CatagoryToQuote catagoryToQuote;
    private UserUtils userUtils;
    private MoveToFragmentCategory moveToFragmentCategory ;

    public SavedCatagory(Context context, List<CategoryLike> saved_categoryList, CatagoryToQuote catagoryToQuote,MoveToFragmentCategory moveToFragmentCategory) {
        this.context = context;
        this.saved_categoryList = saved_categoryList;
        this.catagoryToQuote = catagoryToQuote;
        userUtils = new UserUtils(context);
        this.moveToFragmentCategory = moveToFragmentCategory;
    }

    @NonNull
    @Override
    public SavedCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_saved_category, parent, false);
        return new SavedCategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedCategoryHolder holder, int position) {
        String name = saved_categoryList.get(holder.getAbsoluteAdapterPosition()).getName().substring(0, 1);
        holder.singal_name.setText(name);
        holder.full_name.setText(saved_categoryList.get(holder.getAbsoluteAdapterPosition()).getName());
        if (saved_categoryList.get(holder.getAbsoluteAdapterPosition()).getType().equals("Paid")) {
            holder.crown.setVisibility(View.VISIBLE);
        }
        String value = saved_categoryList.get(holder.getAbsoluteAdapterPosition()).getValue();
        if (value.equals("true")) {
            holder.favourite.setVisibility(View.VISIBLE);
            holder.not_favourite.setVisibility(View.GONE);
        } else if (value.equals("false")) {
            holder.favourite.setVisibility(View.GONE);
            holder.not_favourite.setVisibility(View.VISIBLE);
        }
        if (userUtils.getUserStatus().equals("true"))
        {

            ClickListener(holder);
        }else
        {
            context.startActivity(new Intent(context, LoginActivity.class));
            ((AppCompatActivity)context).finish();
        }


    }

    private void ClickListener(SavedCategoryHolder holder) {
        holder.not_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.not_favourite.setVisibility(View.GONE);
                holder.favourite.setVisibility(View.VISIBLE);
                 hitCateGoryLikeDislike(userUtils.getUserId(), holder);
                saved_categoryList.remove(holder.getAbsoluteAdapterPosition());
                notifyDataSetChanged();

            }
        });

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.not_favourite.setVisibility(View.VISIBLE);
                holder.favourite.setVisibility(View.GONE);
                hitCateGoryLikeDislike(userUtils.getUserId(), holder);
                saved_categoryList.remove(holder.getAbsoluteAdapterPosition());
                notifyDataSetChanged();

            }
        });

    }

    private void hitCateGoryLikeDislike(String userid, SavedCategoryHolder holder) {
        RetrofitClient.getInstance().getapi().addCategoryFavourite(userid, saved_categoryList.get(holder.getAbsoluteAdapterPosition()).getCatId(), "false").enqueue(new Callback<CategoryFavourite>() {
            @Override
            public void onResponse(Call<CategoryFavourite> call, Response<CategoryFavourite> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

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
        return saved_categoryList.size();
    }

    public class SavedCategoryHolder extends RecyclerView.ViewHolder {
        private final TextView singal_name;
        private final TextView full_name;
        private final ImageView favourite;
        private final ImageView not_favourite;
        private ImageView crown;


        public SavedCategoryHolder(@NonNull View itemView) {
            super(itemView);
            singal_name = itemView.findViewById(R.id.first_letter);
            full_name = itemView.findViewById(R.id.category_name);
            not_favourite = itemView.findViewById(R.id.not_fav);
            favourite = itemView.findViewById(R.id.favourite);
            crown = itemView.findViewById(R.id.crown);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    catagoryToQuote.getrequiredQuote(getAbsoluteAdapterPosition());
                }
            });


        }
    }
}
