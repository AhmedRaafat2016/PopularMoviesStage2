package com.example.ahmad.popularmovrub;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "favorites_table")
public class AhmedMovRes implements Parcelable {

    @Ignore
    public static final Creator<AhmedMovRes> CREATOR = new Creator<AhmedMovRes>() {
        @Override
        public AhmedMovRes createFromParcel(Parcel in) {
            return new AhmedMovRes(in);
        }

        @Override
        public AhmedMovRes[] newArray(int size) {
            return new AhmedMovRes[size];
        }
    };
    @PrimaryKey
    @NonNull
    private int id;
    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    private String voteAverage;
    private String title;
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String posterPath;
    @ColumnInfo(name = "backdrop_path")

    @SerializedName("backdrop_path")
    private String backdropPath;
    private String overview;
    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private String releaseDate;


    public AhmedMovRes(int id, String voteAverage, String title, String posterPath, String backdropPath, String overview, String releaseDate) {

        this.id = id;
        this.voteAverage = voteAverage;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    @Ignore
    protected AhmedMovRes(Parcel in) {
        id = in.readInt();
        voteAverage = in.readString();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(voteAverage);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }
}
