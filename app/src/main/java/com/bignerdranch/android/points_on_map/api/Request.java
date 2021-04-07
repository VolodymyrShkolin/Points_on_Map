package com.bignerdranch.android.points_on_map.api;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Request {

    public Api buildRetrofitConfig(){
        OkHttpClient.Builder httpClient = getBaseHttpConfig();
        Retrofit.Builder retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()));
        return retrofit.build().create(Api.class);
    }

    private OkHttpClient.Builder getBaseHttpConfig(){
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(log);
        return okHttpClient;
    }
}
