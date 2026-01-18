package com.example.matbakhy.data.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static Network instance = null;
    public MealServices services;

    private Network(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.themealdb.com/api/json/v1/1/").addConverterFactory(GsonConverterFactory.create()).build();
        services = retrofit.create(MealServices.class);
    }
    public static Network getInstance(){
        if(instance == null){
            instance = new Network();
        }
        return instance;
    }
}
