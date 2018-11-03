package com.example.ahmad.popularmovrub;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class AhmedFavVMod extends AndroidViewModel {

    private AhmedFavRep ahmedFavRep;
    private LiveData<List<AhmedMovRes>> allFavorites;

    public AhmedFavVMod(@NonNull Application application) {
        super(application);
        ahmedFavRep = new AhmedFavRep(application);
        allFavorites = ahmedFavRep.getAllFavorites();
    }

    public void insert(AhmedMovRes ahmedMovRes) {
        ahmedFavRep.insert(ahmedMovRes);
    }

    public void deleteAllFavorites() {
        ahmedFavRep.deleteAllFavorites();
    }

    public LiveData<List<AhmedMovRes>> getAllFavorites() {
        return allFavorites;
    }

}
