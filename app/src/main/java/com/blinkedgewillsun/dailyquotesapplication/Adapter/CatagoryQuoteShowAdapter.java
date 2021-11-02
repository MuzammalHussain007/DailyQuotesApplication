package com.blinkedgewillsun.dailyquotesapplication.Adapter;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.CatagoryToQuoteResponse.Quote__1;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.FavouriteResponse.Favourite;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.RetrofitClient;
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

public class CatagoryQuoteShowAdapter extends RecyclerView.Adapter<CatagoryQuoteShowAdapter.CatagoryQuoteHolder> {
    private Context context;
    private List<Quote__1> quote__1List;
    private String id ;
    private int i=0;
    private UserUtils userUtils ;
    private int[] back = {
            R.drawable.img13, R.drawable.img15,
    };
    private int index ;

    public CatagoryQuoteShowAdapter(Context context, List<Quote__1> quote__1List, String id) {
        this.context = context;
        this.quote__1List = quote__1List;
        this.id = id;
        userUtils = new UserUtils(context);
    }

    @NonNull
    @Override
    public CatagoryQuoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_favourite_layout, parent, false);
        return new CatagoryQuoteHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CatagoryQuoteHolder holder, int position) {
        textShow(holder);
        setFonts(holder);
        setBackGround(holder);
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

    private void setBackGround(CatagoryQuoteHolder holder) {
        //TODO image set krni hy
//        holder.imageView.setImageResource(Integer.parseInt(quote__1List.get(holder.getAbsoluteAdapterPosition()).getImg()));
    }

    private void setFonts(CatagoryQuoteHolder holder) {
        //TODO font set krna hy
//        Typeface typeface = ResourcesCompat.getFont(context.getApplicationContext(), Integer.parseInt(quote__1List.get(holder.getAbsoluteAdapterPosition()).getFont()));
//        holder.quote_text.setTypeface(typeface);

    }
    private void clickListener(CatagoryQuoteHolder holder) {
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = holder.quote_text.getText().toString();
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
                int[] fonts = {R.font.caveat_bold, R.font.caveat_regular, R.font.karltown, R.font.fortunate,
                        R.font.russel, R.font.magic, R.font.raimbow, R.font.montserrat_bold, R.font.beautymountain, R.font.beautypeople};
                if (i >= fonts.length) {
                    i = 0;
                }
                Typeface typeface = ResourcesCompat.getFont(context.getApplicationContext(), fonts[i]);
                holder.quote_text.setTypeface(typeface);
                i++;

            }

        });
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < back.length) {
                    holder.imageView.setImageResource(back[index]);
                    index++;
                } else {
                    index = 0;
                    holder.imageView.setImageResource(back[index]);
                    index++;
                }

            }
        });
        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = holder.quote_text.getText().toString();
                ClipboardManager clipboardMgr = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied text", text);
                clipboardMgr.setPrimaryClip(clip);
                Toast.makeText(context, "copied", Toast.LENGTH_SHORT).show();
            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
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

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.unfavourite.setVisibility(View.VISIBLE);
                holder.favourite.setVisibility(View.GONE);
                hitfav_unfav_call_api(userUtils.getUserId(), id ,quote__1List.get(holder.getAbsoluteAdapterPosition()).getQuoteId(),"true");
            }
        });

        holder.unfavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.unfavourite.setVisibility(View.GONE);
                holder.favourite.setVisibility(View.VISIBLE);
                hitfav_unfav_call_api(userUtils.getUserId(), id ,quote__1List.get(holder.getAbsoluteAdapterPosition()).getQuoteId(),"false");

            }
        });
    }
    private Bitmap addStampToImage(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
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

    private void hitfav_unfav_call_api(String userid, String id, String quoteId, String favValue) {
        RetrofitClient.getInstance().getapi().AddToFavourite(userid,id,quoteId,favValue).enqueue(new Callback<Favourite>() {
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

    private void textShow(CatagoryQuoteHolder holder) {
        holder.quote_text.setText(quote__1List.get(holder.getAbsoluteAdapterPosition()).getText());
        String userLike = quote__1List.get(holder.getAbsoluteAdapterPosition()).getUserLiked();
        if (userLike.equals("true"))
        {
            holder.favourite.setVisibility(View.VISIBLE);
            holder.unfavourite.setVisibility(View.GONE);
        }else if (userLike.equals("false"))
        {
            holder.favourite.setVisibility(View.GONE);
            holder.unfavourite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return quote__1List.size();
    }

    public class CatagoryQuoteHolder extends RecyclerView.ViewHolder
    {
        private TextView quote_text;
        private RelativeLayout relativeLayout ;
        private ImageView share,copy , download , favourite , unfavourite , font , background,imageView;

        public CatagoryQuoteHolder(@NonNull View itemView) {
            super(itemView);
            quote_text = itemView.findViewById(R.id.quote_txt);
            share= itemView.findViewById(R.id.share);
            copy =itemView.findViewById(R.id.copy);
            download = itemView.findViewById(R.id.download);
            favourite = itemView.findViewById(R.id.fav);
            unfavourite = itemView.findViewById(R.id.not_favourite);
            font = itemView.findViewById(R.id.change_font);
            background = itemView.findViewById(R.id.change_back_ground);
            imageView = itemView.findViewById(R.id.imageView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);


        }
    }
}
