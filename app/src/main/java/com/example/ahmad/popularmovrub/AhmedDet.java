package com.example.ahmad.popularmovrub;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AhmedDet extends AppCompatActivity {
    public static final String BASE_IMG_URL = "https://image.tmdb.org/t/p/w500";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private FloatingActionButton fab;
    private AhmedJsonApiM ahmedJsonApiM;
    private AhmedMovRes mAhmedMovRes;
    private int mMovieId;
    private String mTitle;
    private AhmedFavDb mDatabase;
    private AhmedFavVMod mAhmedFavVMod;
    private int mTrailerSize = 0;
    private TextView mTrailersFound;
    private ArrayList<AhmedTrailRes> mTrailerResults;
    private AhmedTrailAdap mAhmedTrailAdap;
    private RecyclerView mTrailerRecyclerView;
    private int mReviewSize = 0;
    private TextView mReviewsFound;
    private ArrayList<AhmedRevRes> mReviewResults;
    private AhmedRevAdap mAhmedRevAdap;
    private RecyclerView mReviewRecyclerView;
    private Boolean mCheckResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getIncomingIntent();
        setTitle(mTitle);
        mDatabase = AhmedFavDb.getInstance(getApplicationContext());
        mAhmedFavVMod = ViewModelProviders.of(this).get(AhmedFavVMod.class);
        fab = findViewById(R.id.fab_favorites);
        checkIfExists task = new checkIfExists();
        task.execute();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabButtonClicked();
            }
        });
    }

    private void getIncomingIntent() {

        Intent intent = getIntent();
        mAhmedMovRes = intent.getParcelableExtra("Movie");

        mMovieId = mAhmedMovRes.getId();
        String mVoteAverage = mAhmedMovRes.getVoteAverage();
        mTitle = mAhmedMovRes.getTitle();
        String mBackdropPath = mAhmedMovRes.getBackdropPath();
        String mOverview = mAhmedMovRes.getOverview();
        String mReleaseDate = mAhmedMovRes.getReleaseDate();

        TextView movieTitle = findViewById(R.id.movie_title);
        movieTitle.setText(mTitle);

        TextView movieOverview = findViewById(R.id.synopsis);
        movieOverview.setText(mOverview);

        TextView movieUserRating = findViewById(R.id.user_rating_data);
        movieUserRating.setText(mVoteAverage);

        TextView movieReleaseDate = findViewById(R.id.release_date_data);

        // Takes yyyy-MM-dd converts to MM/dd/yyyy
        String[] parts = mReleaseDate.split("-");
        String updatedReleaseDate = parts[1] + "/" + parts[2] + "/" + parts[0];
        movieReleaseDate.setText(updatedReleaseDate);

        ImageView image = findViewById(R.id.movie_backdrop);
        Glide.with(this)
                .load(BASE_IMG_URL + mBackdropPath)
                .into(image);


        buildBaseUrl();
        callTrailer();
        callReviews();

    }


    private void buildBaseUrl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ahmedJsonApiM = retrofit.create(AhmedJsonApiM.class);
        initTrailerRecyclerView();
        initReviewRecyclerView();
    }


    private void callReviews() {

        Call<AhmedRevResp> call = ahmedJsonApiM.getReviews(mMovieId, AhmedMain.API_KEY);

        call.enqueue(new Callback<AhmedRevResp>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<AhmedRevResp> call, @NonNull Response<AhmedRevResp> response) {

                AhmedRevResp ahmedRevResp = response.body();

                assert ahmedRevResp != null;
                mReviewResults = new ArrayList<>(Arrays.asList(ahmedRevResp.getReviewResults()));
                mAhmedRevAdap = new AhmedRevAdap(mReviewResults);
                mReviewRecyclerView.setAdapter(mAhmedRevAdap);

                mReviewSize = mReviewResults.size();
                mReviewsFound = findViewById(R.id.reviews_found);

                if (mReviewSize > 0) {
                    mReviewsFound.setText("Found " + mReviewSize + " Movie Reviews");
                } else {
                    mReviewsFound.setText(R.string.no_reviews_found);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AhmedRevResp> call, Throwable t) {
                mReviewsFound.setText(R.string.error_parsing_reviews);
            }
        });

    }

    private void callTrailer() {

        Call<AhmedTrailResp> call = ahmedJsonApiM.getTrailers(mMovieId, AhmedMain.API_KEY);

        call.enqueue(new Callback<AhmedTrailResp>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<AhmedTrailResp> call, @NonNull Response<AhmedTrailResp> response) {

                AhmedTrailResp ahmedTrailResp = response.body();

                assert ahmedTrailResp != null;
                mTrailerResults = new ArrayList<>(Arrays.asList(ahmedTrailResp.getResults()));
                mAhmedTrailAdap = new AhmedTrailAdap(mTrailerResults, getApplicationContext());
                mTrailerRecyclerView.setAdapter(mAhmedTrailAdap);

                mTrailerSize = mTrailerResults.size();
                mTrailersFound = findViewById(R.id.trailers_found);

                if (mTrailerSize > 0) {
                    mTrailersFound.setText("Found " + mTrailerSize + " Movie Trailers");
                } else {
                    mTrailersFound.setText(R.string.no_trailers_found);
                }

            }

            @Override
            public void onFailure(@NonNull Call<AhmedTrailResp> call, Throwable t) {
                mTrailersFound.setText(R.string.trailer_parse_error);
            }
        });

    }

    private void initTrailerRecyclerView() {

        mTrailerRecyclerView = findViewById(R.id.trailer_rv);
        mTrailerRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(layoutManager);

    }

    private void initReviewRecyclerView() {
        mReviewRecyclerView = findViewById(R.id.reviews_rv);
        mReviewRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mReviewRecyclerView.setLayoutManager(layoutManager);

    }

    public void fabButtonClicked() {

        if (mCheckResult) {

            AhmedAppExec.getsInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.favoritesDao().deleteThisMovie(mMovieId);

                }
            });
            fab.setImageResource(R.drawable.ic_star_empty);
            mCheckResult = false;
            Toast.makeText(this, "Deleted " + mTitle + " from Favorites", Toast.LENGTH_SHORT).show();
        } else {
            mAhmedFavVMod.insert(mAhmedMovRes);
            fab.setImageResource(R.drawable.ic_star_yellow);
            mCheckResult = true;
            Toast.makeText(this, "Added " + mTitle + " to Favorites", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("StaticFieldLeak")
    protected class checkIfExists extends AsyncTask<Integer, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... integers) {
            Boolean checkResult;
            Integer checkMovieId = mDatabase.favoritesDao().ifExists(mAhmedMovRes.getId());
            checkResult = checkMovieId > 0;
            return checkResult;
        }


        @Override
        protected void onPostExecute(Boolean checkResult) {
            mCheckResult = checkResult;
            if (mCheckResult) {
                fab.setImageResource(R.drawable.ic_star_yellow);
            } else {
                fab.setImageResource(R.drawable.ic_star_empty);
            }
        }
    }
}
