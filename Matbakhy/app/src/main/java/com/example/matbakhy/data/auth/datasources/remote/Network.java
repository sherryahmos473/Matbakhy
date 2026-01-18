package com.example.matbakhy.data.auth.datasources.remote;

public class Network {
    private static Network instance = null;
    public FirebaseServices services;

    private Network(){

    }
    public static Network getInstance(){
        if(instance == null){
            instance = new Network();
        }
        return instance;
    }
}
