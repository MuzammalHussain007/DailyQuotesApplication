package com.blinkedgewillsun.dailyquotesapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.blinkedgewillsun.dailyquotesapplication.Modal.CatagoryModal;
import com.blinkedgewillsun.dailyquotesapplication.R;

import java.util.List;

public class CatagoryAdapter extends RecyclerView.Adapter<CatagoryAdapter.CatagoryHolder> {
    private List<CatagoryModal> catagoryModalList ;
    private Context context ;

    public CatagoryAdapter(List<CatagoryModal> catagoryModalList, Context context) {
        this.catagoryModalList = catagoryModalList;
        this.context = context;
    }

    @NonNull
    @Override
    public CatagoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_catagory_layout,parent,false);
        return new CatagoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatagoryHolder holder, int position) {
        holder.textView.setText(catagoryModalList.get(holder.getAbsoluteAdapterPosition()).getName());

    }

    @Override
    public int getItemCount() {
        return catagoryModalList.size();
    }

    public class CatagoryHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageView;
        private TextView textView ;

        public CatagoryHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_catagory_image);
            textView = itemView.findViewById(R.id.custom_catagory_text);
        }
    }
}
