package com.example.ahmad.popularmovrub;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = AhmedMovRes.class, version = 1, exportSchema = false)
public abstract class AhmedFavDb extends RoomDatabase {

    private static AhmedFavDb instance;

    public static synchronized AhmedFavDb getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AhmedFavDb.class, "favorites_database.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract AhmedFav favoritesDao();
}
