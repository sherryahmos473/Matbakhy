// AuthModule.java
package com.example.matbakhy.di;

import android.content.Context;

import com.example.matbakhy.data.auth.AuthRepository;
import com.example.matbakhy.data.auth.datasources.local.SharedPref;
import com.example.matbakhy.data.auth.datasources.local.SharedPrefServices;
import com.example.matbakhy.data.auth.datasources.remote.FirebaseServices;
import com.example.matbakhy.presentation.Auth.presenter.LoginPresenterImpl;

public class AuthModule {

    public static SharedPref provideSharedPref(Context context) {
        return new SharedPrefServices(context);
    }

    public static FirebaseServices provideFirebaseAuth(SharedPref sharedPref) {
        return new FirebaseServices(sharedPref);
    }

    public static AuthRepository provideAuthRepository(Context context) {
        SharedPref sharedPref = provideSharedPref(context);
        FirebaseServices firebaseAuth = provideFirebaseAuth(sharedPref);
        return new AuthRepository(context, firebaseAuth, sharedPref);
    }

    public static LoginPresenterImpl provideLoginPresenter(Context context) {
        AuthRepository authRepository = provideAuthRepository(context);
        return new LoginPresenterImpl(authRepository);
    }
}