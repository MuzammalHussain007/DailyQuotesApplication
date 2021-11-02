package com.blinkedgewillsun.dailyquotesapplication.Adapter.HomeScreen;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blinkedgewillsun.dailyquotesapplication.Interface.MoveToFragment;
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
import java.time.format.TextStyle;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedQuoteAdapter extends RecyclerView.Adapter<SavedQuoteAdapter.SavedQuoteAdapterHolder> {
    private Context context;
    private UserUtils userUtils ;
    private List<Like> savedQuotes;
    private int limit=3;
    private int[] back = {
            R.drawable.img13, R.drawable.img15,
    };
    private MoveToFragment moveToFragment ;
    private int index ;
    int i =0;

    public SavedQuoteAdapter(Context context, List<Like> savedQuotes,MoveToFragment moveToFragment) {
        this.context = context;
        this.savedQuotes = savedQuotes;
        userUtils = new UserUtils(context);
        this.moveToFragment = moveToFragment;
    }

    @NonNull
    @Override
    public SavedQuoteAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_favourite_layout, parent, false);
        return new SavedQuoteAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedQuoteAdapterHolder holder, int position) {
        mainText(holder);
        String check = userUtils.getUserStatus();
        if (check.equals("true"))
        {
            listerner(holder);
        }else
        {
            context.startActivity(new Intent(context, LoginActivity.class));
            ((AppCompatActivity) context).finish();
        }

        String userLike = savedQuotes.get(holder.getAbsoluteAdapterPosition()).getValue();
        if (userLike.equals("true"))
        {
            holder.favourite.setVisibility(View.VISIBLE);
            holder.not_favourite.setVisibility(View.GONE);
        }else if (userLike.equals("false"))
        {
            holder.favourite.setVisibility(View.GONE);
            holder.not_favourite.setVisibility(View.VISIBLE);
        }

    }
    private void listerner(SavedQuoteAdapterHolder holder) {
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
        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = holder.mainText.getText().toString();
                ClipboardManager clipboardMgr = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied text", text);
                clipboardMgr.setPrimaryClip(clip);
                Toast.makeText(context, "copied", Toast.LENGTH_SHORT).show();
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = holder.mainText.getText().toString();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share Quote"));

            }
        });

        holder.not_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.not_favourite.setVisibility(View.GONE);
                holder.favourite.setVisibility(View.VISIBLE);
                hitfav_unFav_Api(userUtils.getUserId() , savedQuotes.get(holder.getAbsoluteAdapterPosition()).getCatId(),savedQuotes.get(holder.getAbsoluteAdapterPosition()).getQuoteId(),"false");

            }
        });
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < back.length) {
                    holder.imageView.setImageResource(back[index]);
                    index++;
               //     insertBackGround(userUtils.getUserId(), savedQuotes.get(holder.getAbsoluteAdapterPosition()).getQuoteId(),String.valueOf(back[index]));
                } else {
                    index = 0;
                    holder.imageView.setImageResource(back[index]);
                    index++;
               //     insertBackGround(userUtils.getUserId(), savedQuotes.get(holder.getAbsoluteAdapterPosition()).getQuoteId(),String.valueOf(back[index]));
                }


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
                holder.mainText.setTypeface(typeface);
                i++;
            //    insertFont(userUtils.getUserId(), savedQuotes.get(holder.getAbsoluteAdapterPosition()).getQuoteId(),String.valueOf(fonts[i]));
            }
        });
        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.not_favourite.setVisibility(View.VISIBLE);
                holder.favourite.setVisibility(View.GONE);
                moveToFragment.moveValue(savedQuotes.size());
                hitfav_unFav_Api(userUtils.getUserId(), savedQuotes.get(holder.getAbsoluteAdapterPosition()).getCatId(),savedQuotes.get(holder.getAbsoluteAdapterPosition()).getQuoteId(),"true");
                savedQuotes.remove(holder.getAbsoluteAdapterPosition());
                notifyDataSetChanged();
            }
        });

    }
    private Bitmap addStampToImage(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
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

    private void mainText(SavedQuoteAdapterHolder holder) {
        holder.mainText.setText(savedQuotes.get(holder.getAbsoluteAdapterPosition()).getText());

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
        if (savedQuotes.size() > limit)
        {
            return limit;
        }else
        {

            return savedQuotes.size();
        }
    }

    public class SavedQuoteAdapterHolder extends RecyclerView.ViewHolder {
        private TextView mainText;
        private ImageView download,share , favourite , not_favourite , copy , background , font,imageView;
        private RelativeLayout relativeLayout;

        public SavedQuoteAdapterHolder(@NonNull View itemView) {
            super(itemView);
            mainText = itemView.findViewById(R.id.quote_txt);
            download = itemView.findViewById(R.id.download);
            share = itemView.findViewById(R.id.share);
            not_favourite =itemView.findViewById(R.id.not_favourite);
            favourite = itemView.findViewById(R.id.fav);
            copy = itemView.findViewById(R.id.copy);
            font = itemView.findViewById(R.id.change_font);
            background = itemView.findViewById(R.id.change_back_ground);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            imageView  = itemView.findViewById(R.id.imageView);
        }
    }

}
