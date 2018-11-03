package com.example.ahmad.popularmovrub;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class AhmedFavRep {

    private AhmedFav ahmedFav;

    private LiveData<List<AhmedMovRes>> allFavorites;


    public AhmedFavRep(Application application) {
        AhmedFavDb database = AhmedFavDb.getInstance(application);
        ahmedFav = database.favoritesDao();
        allFavorites = ahmedFav.getAllFavorites();

    }

    public void insert(AhmedMovRes fav) {
        new InsertFavoritesAsyncTask(ahmedFav).execute(fav);

    }


    public void deleteAllFavorites() {
        new DeleteAllFavoritesAsyncTask(ahmedFav).execute();
    }

    public LiveData<List<AhmedMovRes>> getAllFavorites() {
        return allFavorites;
    }


    private static class InsertFavoritesAsyncTask extends AsyncTask<AhmedMovRes, Void, Void> {
        private AhmedFav ahmedFav;

        private InsertFavoritesAsyncTask(AhmedFav ahmedFav) {
            this.ahmedFav = ahmedFav;
        }

        @Override
        protected Void doInBackground(AhmedMovRes... movieResults) {
            ahmedFav.insert(movieResults[0]);
            return null;
        }
    }


    private static class DeleteAllFavoritesAsyncTask extends AsyncTask<Void, Void, Void> {
        private AhmedFav ahmedFav;

        private DeleteAllFavoritesAsyncTask(AhmedFav ahmedFav) {
            this.ahmedFav = ahmedFav;
        }

        @Override
        protected Void doInBackground(Void... Voids) {
            ahmedFav.deleteAllFavorites();
            return null;
        }
    }
}
