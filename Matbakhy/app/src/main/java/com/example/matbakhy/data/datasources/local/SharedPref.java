package com.example.matbakhy.data.datasources.local;

import com.example.matbakhy.data.model.User;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface SharedPref {

    Completable saveUserLoginRx(String email, String name, String userId);

    Single<Boolean> isLoggedInRx();

    Single<String> getUserEmailRx();

    Single<String> getUserNameRx();

    Single<String> getUserIdRx();

    Completable loginAsGuestRx();

    Completable clearUserDataRx();

    Single<Boolean> isGuestRx();

    Completable saveUserDataRx(String email, String name, String userId, boolean isGuest);

    Single<User> getUserDataRx();
}