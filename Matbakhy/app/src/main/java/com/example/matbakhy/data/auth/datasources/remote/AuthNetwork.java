package com.example.matbakhy.data.auth.datasources.remote;

public class AuthNetwork {
    private static AuthNetwork instance = null;
    public FirebaseServices services;

    private AuthNetwork(){

    }
    public static AuthNetwork getInstance(){
        if(instance == null){
            instance = new AuthNetwork();
        }
        return instance;
    }
}
