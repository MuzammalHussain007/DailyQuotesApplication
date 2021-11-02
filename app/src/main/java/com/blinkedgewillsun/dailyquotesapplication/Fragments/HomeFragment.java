package com.blinkedgewillsun.dailyquotesapplication.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blinkedgewillsun.dailyquotesapplication.Adapter.HomeScreen.SavedCatagory;
import com.blinkedgewillsun.dailyquotesapplication.Adapter.HomeScreen.SavedQuoteAdapter;
import com.blinkedgewillsun.dailyquotesapplication.Interface.CatagoryToQuote;
import com.blinkedgewillsun.dailyquotesapplication.Interface.MoveToFragment;
import com.blinkedgewillsun.dailyquotesapplication.Interface.MoveToFragmentCategory;
import com.blinkedgewillsun.dailyquotesapplication.Modal.SavedQuotes;
import com.blinkedgewillsun.dailyquotesapplication.Modal.Saved_Category;
import com.blinkedgewillsun.dailyquotesapplication.OtherClass.UserUtils;
import com.blinkedgewillsun.dailyquotesapplication.R;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.BackGroundResponse.BackGround;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.FontResponse.Font;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.QuoteCategoryLikeShowResponse.CategoryLike;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.QuoteCategoryLikeShowResponse.QuoteCategoryLikeShow;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.QuoteOFDayLikeResponse.Quote;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.QuotesofDayResponse.QuotesofDay;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.RetrofitClient;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.ShowQuoteFavouriteResponse.FavouriteQuotes;
import com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.ShowQuoteFavouriteResponse.Like;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements CatagoryToQuote, MoveToFragment, MoveToFragmentCategory {
    private TextView main_quote, catagory_see_all, quotes_see_all, category_error;
    private RecyclerView saved_catagory, save_quotes;
    private List<SavedQuotes> savedQuotes;
    private List<com.blinkedgewillsun.dailyquotesapplication.RetrofitCall.QuotesofDayResponse.Quote> quoteList;
    private List<Saved_Category> saved_categoryList;
    private List<CategoryLike> categoryLikeList;
    private int i = 0;
    private TextView errorQuote ;
    private RelativeLayout relativeLayout;
    private ImageView font, paint;
    private ProgressDialog progressDialog;
    private ImageView imageview, copy, share, download, favoutrite, unfavourite;
    private int[] back = {
            R.drawable.img13, R.drawable.img15,
    };
    private TextView errorMessageFavourite;
    int index = 0;
    String quote_id;
    private List<Like> favouriteQuotesList;
    private UserUtils userUtils ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        innit(view);
        progressDialog.show();
        clickListener();
        setQuotesOFDay();
        showFavouriteQuotes();
        showFavouriteCataogry();
        return view;
    }
    private void showFavouriteCataogry() {
        progressDialog.show();
        RetrofitClient.getInstance().getapi().showCategoryFavourite(userUtils.getUserId()).enqueue(new Callback<QuoteCategoryLikeShow>() {
            @Override
            public void onResponse(Call<QuoteCategoryLikeShow> call, Response<QuoteCategoryLikeShow> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            categoryLikeList.addAll(response.body().getCategoryLikes());
                            goToAdapterCategoryLike();
                        } else {
                            category_error.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<QuoteCategoryLikeShow> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });


    }
    private void goToAdapterCategoryLike() {
        if (categoryLikeList.isEmpty()) {
            category_error.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        } else {
            saved_catagory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            saved_catagory.setAdapter(new SavedCatagory(getContext(), categoryLikeList, this,this::moveValueCategory));
        }
    }

    private void showFavouriteQuotes() {

        RetrofitClient.getInstance().getapi().showFavourite(userUtils.getUserId()).enqueue(new Callback<FavouriteQuotes>() {
            @Override
            public void onResponse(Call<FavouriteQuotes> call, Response<FavouriteQuotes> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            favouriteQuotesList.addAll(response.body().getLikes());
                            saveQuotes();
                        } else {
                            errorMessageFavourite.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FavouriteQuotes> call, Throwable t) {
                errorMessageFavourite.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setQuotesOFDay() {
        RetrofitClient.getInstance().getapi().SingalQuotesOfDay().enqueue(new Callback<QuotesofDay>() {
            @Override
            public void onResponse(Call<QuotesofDay> call, Response<QuotesofDay> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            progressDialog.dismiss();
                            quoteList = response.body().getQuote();
                            main_quote.setText(quoteList.get(0).getQuoteOfTheDay());
                            quote_id = quoteList.get(0).getId();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<QuotesofDay> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                category_error.setVisibility(View.VISIBLE);
                errorMessageFavourite.setVisibility(View.VISIBLE);
                errorQuote.setVisibility(View.VISIBLE);
                main_quote.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");
    }

    private void saveQuotes() {


        if (favouriteQuotesList.isEmpty()) {
            errorMessageFavourite.setVisibility(View.VISIBLE);
        } else {
            save_quotes.setLayoutManager(new LinearLayoutManager(getContext()));
            save_quotes.setAdapter(new SavedQuoteAdapter(getContext(), favouriteQuotesList,this::moveValue));
        }
    }

    @SuppressLint("ResourceAsColor")
    private Bitmap addStampToImage(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    private void clickListener() {
          download.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Dexter.withContext(getContext()).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                          .withListener(new MultiplePermissionsListener() {
                              @Override
                              public void onPermissionsChecked(MultiplePermissionsReport report) {
                                  if (report.areAllPermissionsGranted()) {
                                      if (startDownload(addStampToImage(relativeLayout)) == null) {
                                          Toast.makeText(getContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                                      } else {
                                          Toast.makeText(getContext(), "Saved To Gallery", Toast.LENGTH_SHORT).show();
                                      }
                                  } else {
                                      Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                                  }
                              }

                              @Override
                              public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                  token.continuePermissionRequest();
                              }
                          }).check();
              }
          });
        catagory_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_transaction, new CatagoryFragment(), "catagoryFragment").addToBackStack("catagoryFragment").commit();
            }
        });
        quotes_see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_transaction, new FavouriteFragment(), "favFragment").addToBackStack("favFragment").commit();
            }
        });


        copy.setOnClickListener(v -> {

            String text = main_quote.getText().toString();
            ClipboardManager clipboardMgr = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied text", text);
            clipboardMgr.setPrimaryClip(clip);
            Toast.makeText(getContext(), "copied", Toast.LENGTH_SHORT).show();

        });
        share.setOnClickListener(v -> {
            String shareBody = main_quote.getText().toString();
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share Quote"));

        });
        font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFontFamily();

            }
        });
        paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackGround();

            }
        });

    }



    private Uri startDownload(Bitmap addStampToImage) {
        OutputStream outputStream = null;
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver contentResolver = getContext().getContentResolver();
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
                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                        bitmap1, "image_" + System.currentTimeMillis(), null);
                uri = Uri.parse(path);
            } else {
                String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                        addStampToImage, "image_" + System.currentTimeMillis(), null);
                uri = Uri.parse(path);
            }
            Log.d("filteredUri__", uri + " = camera");


        }
        return uri;
    }

    private void setBackGround() {
        if (index < back.length) {
            imageview.setImageResource(back[index]);
            insertBackGround(userUtils.getUserId(), quote_id,String.valueOf(back[index]));
            index++;
        } else {
            index = 0;
            imageview.setImageResource(back[index]);
         //   insertBackGround(userUtils.getUserId(), quote_id,String.valueOf(back[index]));
            index++;
        }
    }


    private void setFontFamily() {
        int[] fonts = {R.font.caveat_bold, R.font.caveat_regular, R.font.karltown, R.font.fortunate,
                R.font.russel, R.font.magic, R.font.raimbow, R.font.montserrat_bold, R.font.beautymountain, R.font.beautypeople};
        if (i >= fonts.length) {
            i = 0;
        }
          String font = String.valueOf(fonts[i]);
        Typeface typeface = ResourcesCompat.getFont(getContext().getApplicationContext(), fonts[i]);
        main_quote.setTypeface(typeface);
        i++;
     //   insertFont(userUtils.getUserId(), quote_id,font);
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
                            Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Font> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BackGround> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void innit(View view) {
        copy = view.findViewById(R.id.copy);
        share = view.findViewById(R.id.share);
        download = view.findViewById(R.id.download);
        main_quote = view.findViewById(R.id.quote_txt);
        imageview = view.findViewById(R.id.imageView);
        saved_categoryList = new ArrayList<>();
        savedQuotes = new ArrayList<>();
        save_quotes = view.findViewById(R.id.saved_quote_recyclar);
        saved_catagory = view.findViewById(R.id.saved_category);
        catagory_see_all = view.findViewById(R.id.catagory_see_all);
        quotes_see_all = view.findViewById(R.id.faviorite_see_all);
        progressDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please Wait");
        quoteList = new ArrayList<>();
        favouriteQuotesList = new ArrayList<>();
        errorMessageFavourite = view.findViewById(R.id.errorMessage);
        categoryLikeList = new ArrayList<>();
        category_error = view.findViewById(R.id.category_error);
        font = view.findViewById(R.id.change_font);
        paint = view.findViewById(R.id.change_back_ground);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        userUtils = new UserUtils(getContext());
        errorQuote = view.findViewById(R.id.errorQuote);

    }

    @Override
    public void getrequiredQuote(int pos) {

        if (!categoryLikeList.get(pos).getType().equals("Paid")) {
            ShowCatQuote fragment = new ShowCatQuote();
            Bundle bundle = new Bundle();
            bundle.putString("catid", "" + categoryLikeList.get(pos).getCatId());
            bundle.putString("catname", categoryLikeList.get(pos).getName());
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.master_transaction, fragment, "catagoryfragment").addToBackStack("catagoryfragment").commit();

        } else {
            getFragmentManager().beginTransaction().replace(R.id.master_transaction, new SubscribeFragment(), "catagoryfragment").addToBackStack("catagoryfragment").commit();

        }

    }

    @Override
    public void moveValue(int value) {
         if (value==1)
        {
            errorMessageFavourite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void moveValueCategory(int value) {
        if (value==1)
        {
            category_error.setVisibility(View.VISIBLE);
        }

    }
}