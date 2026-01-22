package com.example.matbakhy.data.auth.datasources.remote;

import android.content.Context;

import com.example.matbakhy.data.auth.datasources.local.SharedPref;
import com.example.matbakhy.data.auth.datasources.local.SharedPrefServices;

public class AuthNetwork {
    private static AuthNetwork instance = null;
    public FirebaseServices services;
    public SharedPref sharedPref;

    private AuthNetwork(Context context){
        sharedPref = new SharedPrefServices(context);
        services = new FirebaseServices();
    }
    public static AuthNetwork getInstance(Context context){
        if(instance == null){
            instance = new AuthNetwork(context);
        }
        return instance;
    }
}
