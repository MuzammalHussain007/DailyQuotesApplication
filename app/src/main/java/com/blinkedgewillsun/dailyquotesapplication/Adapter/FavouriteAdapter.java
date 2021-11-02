package com.blinkedgewillsun.dailyquotesapplication.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blinkedgewillsun.dailyquotesapplication.MainScreen.LoginActivity;
import com.blinkedgewillsun.dailyquotesapplication.OtherClass.UserUtils;
import com.blinkedgewillsun.dailyquotesapplication.R;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.BackGroundResponse.BackGround;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.FavouriteResponse.Favourite;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.FontResponse.Font;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.RetrofitClient;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.ShowQuoteFavouriteResponse.Like;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteHolder> {
    private List<Like> favouriteList;
    private Context context;
    private int i = 0;
    private UserUtils userUtils ;
    private int[] back = {
            R.drawable.img13, R.drawable.img15,
    };
    private int index = 0;

    public FavouriteAdapter(List<Like> favouriteList, Context context) {
        this.favouriteList = favouriteList;
        this.context = context;
        userUtils = new UserUtils(context);
    }

    @NonNull
    @Override
    public FavouriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_favourite_layout, parent, false);
        return new FavouriteHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull FavouriteHolder holder, int position) {
        holder.quote_txt.setText(favouriteList.get(holder.getAbsoluteAdapterPosition()).getText());
        setFont(holder);
        setBackground(holder);
        String userLike = favouriteList.get(holder.getAbsoluteAdapterPosition()).getValue();
        if (userLike.equals("true"))
        {
            holder.favourite.setVisibility(View.VISIBLE);
            holder.not_favourite.setVisibility(View.GONE);
        }else if (userLike.equals("false"))
        {
            holder.favourite.setVisibility(View.GONE);
            holder.not_favourite.setVisibility(View.VISIBLE);
        }
        if (userUtils.getUserStatus().equals("true"))
        {
            clickListener(holder);
        }
        else
        {
            context.startActivity(new Intent(context, LoginActivity.class));
            ((AppCompatActivity)context).finish();
        }

    }

    private void setFont(FavouriteHolder holder) {
        //TODO Font Set krni hy abi
//        Typeface typeface = ResourcesCompat.getFont(context.getApplicationContext(), Integer.parseInt(favouriteList.get(holder.getAbsoluteAdapterPosition()).getFont()));
//        holder.quote_txt.setTypeface(typeface);

    }

    private void setBackground(FavouriteHolder holder) {
        //TODO backGround Image Set krni hy
//        holder.imageView.setImageResource(Integer.parseInt(favouriteList.get(holder.getAbsoluteAdapterPosition()).getImg()));
    }

    private Uri startDownload(Bitmap addStampToImage) {
        OutputStream outputStream = null;
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver contentResolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "image_" + System.currentTimeMillis() / 1000 + ".jpg");
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/DailyQuotes");
            uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);


            try {
                outputStream = contentResolver.openOutputStream(uri);
                Matrix matrix = new Matrix();
                matrix.postRotate(360);
                Bitmap bitmap1 = Bitmap.createBitmap(addStampToImage, 0, 0, addStampToImage.getWidth(), addStampToImage.getHeight(), matrix, true);
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            if (addStampToImage.getWidth() > addStampToImage.getHeight()) {
                String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/DailyQuotes").toString();
                File file = new File(imagesDir, "image_" + System.currentTimeMillis() + ".jpg");
                try {
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(360); // This code rotate image ...
                Bitmap bitmap1 = Bitmap.createBitmap(addStampToImage, 0, 0, addStampToImage.getWidth(), addStampToImage.getHeight(), matrix, true);
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        bitmap1, "image_" + System.currentTimeMillis(), null);
                uri = Uri.parse(path);
            } else {
                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        addStampToImage, "image_" + System.currentTimeMillis(), null);
                uri = Uri.parse(path);
            }
            Log.d("filteredUri__", uri + " = camera");


        }
        return uri;
    }
    private Bitmap addStampToImage(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void clickListener(FavouriteHolder holder) {
        holder.downlaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(context).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    if (startDownload(addStampToImage(holder.relativeLayout)) == null) {
                                        Toast.makeText(context, "Try Again!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Saved To Gallery", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        holder.not_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    holder.not_favourite.setVisibility(View.GONE);
                    holder.favourite.setVisibility(View.VISIBLE);
                    hitfav_unFav_Api(userUtils.getUserId(), favouriteList.get(holder.getAbsoluteAdapterPosition()).getCatId(),favouriteList.get(holder.getAbsoluteAdapterPosition()).getQuoteId(),"false");

            }
        });
        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.not_favourite.setVisibility(View.VISIBLE);
                holder.favourite.setVisibility(View.GONE);
                hitfav_unFav_Api(userUtils.getUserId(), favouriteList.get(holder.getAbsoluteAdapterPosition()).getCatId(),favouriteList.get(holder.getAbsoluteAdapterPosition()).getQuoteId(),"true");
                favouriteList.remove(holder.getAbsoluteAdapterPosition());
                notifyDataSetChanged();
            }
        });
        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = holder.quote_txt.getText().toString();
                ClipboardManager clipboardMgr = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied text", text);
                clipboardMgr.setPrimaryClip(clip);
                Toast.makeText(context, "copied", Toast.LENGTH_SHORT).show();
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = holder.quote_txt.getText().toString();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
               context.startActivity(Intent.createChooser(sharingIntent, "Share Quote"));

            }
        });
        holder.font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFontFamily(holder);

            }
        });
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGround(holder);

            }
        });
    }
    private void setFontFamily(FavouriteHolder holder) {
        int[] fonts = {R.font.caveat_bold, R.font.caveat_regular, R.font.karltown, R.font.fortunate,
                R.font.russel, R.font.magic, R.font.raimbow, R.font.montserrat_bold, R.font.beautymountain, R.font.beautypeople};
        if (i >= fonts.length) {
            i = 0;
        }
        String font = String.valueOf(fonts[i]);
        Typeface typeface = ResourcesCompat.getFont(context.getApplicationContext(), fonts[i]);
        holder.quote_txt.setTypeface(typeface);
        i++;
       // insertFont(userUtils.getUserId(), favouriteList.get(holder.getAbsoluteAdapterPosition()).getQuoteId(),font);
    }
    private void setBackGround(FavouriteHolder holder) {
        if (index < back.length) {
            holder.imageView.setImageResource(back[index]);
          //  insertBackGround(userUtils.getUserId(), favouriteList.get(holder.getAbsoluteAdapterPosition()).getQuoteId(),String.valueOf(back[index]));
            index++;
        } else {
            index = 0;
            holder.imageView.setImageResource(back[index]);
            insertBackGround(userUtils.getUserId(), favouriteList.get(holder.getAbsoluteAdapterPosition()).getQuoteId(),String.valueOf(back[index]));
            index++;
        }
    }
    private void insertFont(String userID,String quoteID,String fontName)
    {
        RetrofitClient.getInstance().getapi().setFont(userID,quoteID, fontName).enqueue(new Callback<Font>() {
            @Override
            public void onResponse(Call<Font> call, Response<Font> response) {
                if(response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        if (response.body().getStatus())
                        {

                        }
                        else
                        {
                            Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Font> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void insertBackGround(String userID,String quoteID,String backgroundName)
    {
        RetrofitClient.getInstance().getapi().setBackground(userID,quoteID,backgroundName).enqueue(new Callback<BackGround>() {
            @Override
            public void onResponse(Call<BackGround> call, Response<BackGround> response) {
                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        if (response.body().getStatus())
                        {

                        }else
                        {
                            Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BackGround> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void hitfav_unFav_Api(String userid, String catId, String quoteId, String value) {
        RetrofitClient.getInstance().getapi().AddToFavourite(userid,catId,quoteId,value).enqueue(new Callback<Favourite>() {
            @Override
            public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        if (response.body().getStatus())
                        {

                        }else
                        {
                            Toast.makeText(context, ""+response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<Favourite> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }


    public class FavouriteHolder extends RecyclerView.ViewHolder {
        private ImageView downlaod, copy, share, favourite , not_favourite , font ,background,imageView;
        private TextView quote_txt;
        private boolean fav_unfav = false;
        private RelativeLayout relativeLayout ;

        public FavouriteHolder(@NonNull View itemView) {
            super(itemView);
            downlaod = itemView.findViewById(R.id.download);
            copy = itemView.findViewById(R.id.copy);
            share = itemView.findViewById(R.id.share);
            favourite = itemView.findViewById(R.id.fav);
            quote_txt = itemView.findViewById(R.id.quote_txt);
            not_favourite  = itemView.findViewById(R.id.not_favourite);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            font = itemView.findViewById(R.id.change_font);
            background = itemView.findViewById(R.id.change_back_ground);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
