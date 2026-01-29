package com.example.matbakhy.data.datasources.remote;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static Network instance = null;
    public MealServices services;
    public FirebaseServices firebaseServices;

    private Network(Context context){
        firebaseServices = new FirebaseServices(context);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        services = retrofit.create(MealServices.class);
    }
    public static Network getInstance(Context context){
        if(instance == null){
            instance = new Network(context);
        }
        return instance;
    }
}
