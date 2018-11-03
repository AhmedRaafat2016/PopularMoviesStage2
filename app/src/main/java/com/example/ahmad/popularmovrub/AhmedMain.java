package com.example.ahmad.popularmovrub;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AhmedMain extends AppCompatActivity {

    //TODO: Enter API Key Below
    public static final String API_KEY = "0febe36237bb25c7d7cce61fc7cc0e95";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    private List<AhmedMovRes> results;
    private RecyclerView movieRecyclerView;
    private AhmedMovAdap mAhmedMovAdap;
    private ProgressBar progressBar;
    private ImageView noConnection;
    private TextView errorText;
    private Button retryButton;
    private AhmedJsonApiM ahmedJsonApiM;

    private int movieFlag = 1;
    private AhmedFavVMod ahmedFavVMod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ahmedFavVMod = ViewModelProviders.of(this).get(AhmedFavVMod.class);

        noConnection = findViewById(R.id.no_network_iv);
        noConnection.setVisibility(View.GONE);
        errorText = findViewById(R.id.error_text);
        errorText.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        movieRecyclerView = findViewById(R.id.main_recycler_view);
        retryButton = findViewById(R.id.reset_button);
        retryButton.setVisibility(View.GONE);
        retryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                noConnection.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                retryButton.setVisibility(View.GONE);
                errorText.setVisibility(View.GONE);
                errorText.setText("");
                checkConnection();
            }
        });
        checkConnection();
    }

    private void checkConnection() {
        if (isConnected()) {
            checkApi();
        } else {
            errorText.setText("");
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(R.string.no_connection);
            retryButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            noConnection.setVisibility(View.VISIBLE);
        }
    }

    private boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    private void checkApi() {

        if (API_KEY.length() > 1) {
            buildBaseUrl();

        } else {
            errorText.setText("");
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(R.string.no_api_key);

            retryButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            noConnection.setVisibility(View.GONE);

        }
    }

    private void buildBaseUrl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ahmedJsonApiM = retrofit.create(AhmedJsonApiM.class);
        initViews();
    }


    private void callMovies() {

        Call<AhmedMovResp> call = ahmedJsonApiM.getPopular(API_KEY);

        if (movieFlag == 1) {
            call = ahmedJsonApiM.getPopular(API_KEY);
        }
        if (movieFlag == 2) {
            call = ahmedJsonApiM.getTopRated(API_KEY);
        }

        call.enqueue(new Callback<AhmedMovResp>() {
            @Override
            public void onResponse(@NonNull Call<AhmedMovResp> call, @NonNull Response<AhmedMovResp> response) {

                AhmedMovResp jsonAhmedMovResp = response.body();
                assert jsonAhmedMovResp != null;
                results = new ArrayList<>(Arrays.asList(jsonAhmedMovResp.getResults()));

                mAhmedMovAdap = new AhmedMovAdap(AhmedMain.this, results);
                movieRecyclerView.setAdapter(mAhmedMovAdap);
            }

            @Override
            public void onFailure(Call<AhmedMovResp> call, Throwable t) {
                errorText.setText("");
                errorText.setVisibility(View.VISIBLE);
                retryButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                errorText.setText("Error" + t.getMessage());
            }
        });
        progressBar.setVisibility(View.GONE);
    }


    private void initViews() {
        movieRecyclerView = findViewById(R.id.main_recycler_view);
        movieRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        movieRecyclerView.setLayoutManager(layoutManager);

        // spaces out grid evenly
        movieRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position
                int spanCount = 2;
                int spacing = 10;//spacing between views in grid

                if (position >= 0) {
                    int column = position % spanCount; // item column

                    outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing; // item bottom
                } else {
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.top = 0;
                    outRect.bottom = 0;
                }
            }
        });

        checkFlag();

    }

    private void checkFlag() {
        if (movieFlag == 3) {
            callFavs();
        } else {
            callMovies();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popular:
                movieFlag = 1;
                checkFlag();
                return true;
            case R.id.sort_by_top_rated:
                movieFlag = 2;
                checkFlag();
                return true;
            case R.id.sort_by_favorites:
                movieFlag = 3;
                checkFlag();
                return true;
            case R.id.delete_favs:
                checkFavEmpty();
                checkFlag();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callFavs() {

        ahmedFavVMod.getAllFavorites().observe(this, new Observer<List<AhmedMovRes>>() {
            @Override
            public void onChanged(@Nullable List<AhmedMovRes> movieResults) {
                mAhmedMovAdap.setFavs(movieResults);
                movieRecyclerView.setAdapter(mAhmedMovAdap);

            }
        });
    }

    private void checkFavEmpty() {
        try {
            ahmedFavVMod.deleteAllFavorites();
            Toast.makeText(this, "All Favorites Deleted", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e + " Trying to Delete Favorites", Toast.LENGTH_SHORT).show();
        }
    }
}

