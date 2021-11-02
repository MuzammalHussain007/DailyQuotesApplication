package com.blinkedgewillsun.dailyquotesapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blinkedgewillsun.dailyquotesapplication.Fragments.PackageDetailFragment;
import com.blinkedgewillsun.dailyquotesapplication.Interface.MoveToFragment;
import com.blinkedgewillsun.dailyquotesapplication.R;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.PackagesResponse.Package__1;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageAdapterHolder> {
    private Context context;
    private List<Package__1> package__1List;
    private static int lastCheckedPos = 0;


    public PackageAdapter(Context context, List<Package__1> package__1List) {
        this.context = context;
        this.package__1List = package__1List;
    }

    @NonNull
    @Override
    public PackageAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_subscribe_plan_layout, parent, false);
        return new PackageAdapterHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull PackageAdapterHolder holder, int position) {
        holder.type.setText(package__1List.get(holder.getAbsoluteAdapterPosition()).getCatName());
        if(holder.getAbsoluteAdapterPosition() == lastCheckedPos) {
            holder.cardView.setStrokeColor(context.getColor(R.color.DeepSkyBlue));
        } else {
            holder.cardView.setStrokeColor(Color.WHITE);
        }
        holder.price.setText("$"+package__1List.get(holder.getAbsoluteAdapterPosition()).getPrice()+".00");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                PackageDetailFragment packageDetailFragment = new PackageDetailFragment();
                Bundle bundle = new Bundle();

                int prevPos = lastCheckedPos;
                lastCheckedPos = holder.getAbsoluteAdapterPosition();
                notifyItemChanged(prevPos);
                notifyItemChanged(lastCheckedPos);
                bundle.putString("title", package__1List.get(holder.getAbsoluteAdapterPosition()).getCatName());
                bundle.putString("price", package__1List.get(holder.getAbsoluteAdapterPosition()).getPrice());
                bundle.putString("duration", package__1List.get(holder.getAbsoluteAdapterPosition()).getDuration());
                bundle.putString("description", package__1List.get(holder.getAbsoluteAdapterPosition()).getDescription());
                packageDetailFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.change_frame, packageDetailFragment, "packageDetailFragment").commit();
             }
        });


    }

    @Override
    public int getItemCount() {
        return package__1List.size();
    }

    public class PackageAdapterHolder extends RecyclerView.ViewHolder {
        private TextView type , price;
        private MaterialCardView cardView;

        public PackageAdapterHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.packageName);
            cardView = itemView.findViewById(R.id.card_click);
            price = itemView.findViewById(R.id.price);
        }
    }
}
