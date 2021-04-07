package com.bignerdranch.android.points_on_map.api;

import com.bignerdranch.android.points_on_map.data.MapResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL = "https://2fjd9l3x1l.api.quickmocker.com/";

    @GET("kyiv/places")
    Call<MapResponse> getMapResult();
}
