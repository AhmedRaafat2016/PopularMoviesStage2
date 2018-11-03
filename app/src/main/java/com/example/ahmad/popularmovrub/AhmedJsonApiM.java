package com.example.ahmad.popularmovrub;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AhmedJsonApiM {

    // GET  posts
    @GET("popular")
    Call<AhmedMovResp> getPopular(@Query("api_key") String apiKey);

    @GET("top_rated")
    Call<AhmedMovResp> getTopRated(@Query("api_key") String apiKey);

    @GET("{id}/videos")
    Call<AhmedTrailResp> getTrailers(
            @Path("id") int postId,
            @Query("api_key") String apiKey
    );

    @GET("{id}/reviews")
    Call<AhmedRevResp> getReviews(
            @Path("id") int postId,
            @Query("api_key") String apiKey
    );

}
