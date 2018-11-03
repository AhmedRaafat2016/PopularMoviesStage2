package com.example.ahmad.popularmovrub;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AhmedFav {

    @Insert
    void insert(AhmedMovRes movieResults);

    @Query("DELETE FROM favorites_table")
    void deleteAllFavorites();

    @Query("SELECT * FROM favorites_table ORDER BY id DESC")
    LiveData<List<AhmedMovRes>> getAllFavorites();

    @Query("DELETE FROM favorites_table WHERE id = :id")
    void deleteThisMovie(int id);

    @Query("SELECT COUNT(id) FROM favorites_table WHERE id = :id")
    Integer ifExists(int id);

}