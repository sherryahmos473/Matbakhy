package com.example.matbakhy.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.matbakhy.data.datasources.local.MealsLocalDataSource;
import com.example.matbakhy.data.datasources.local.SharedPrefServices;
import com.example.matbakhy.data.datasources.remote.FirebaseBackupService;
import com.example.matbakhy.data.datasources.remote.FirebaseServices;
import com.example.matbakhy.data.datasources.remote.Network;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.data.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class AuthRepository {
    private static final String TAG = "AuthRepository";

    private final FirebaseServices firebaseServices;
    private final FirebaseBackupService firebaseBackupService;
    private final SharedPrefServices sharedPref;
    private final MealRepository mealRepository;
    private final MealsLocalDataSource mealsLocalDataSource;


    public AuthRepository(Context context) {
        this.firebaseServices = Network.getInstance(context).firebaseServices;
        this.mealRepository = new MealRepository(context);
        this.sharedPref = SharedPrefServices.getInstance(context);
        this.firebaseBackupService = new FirebaseBackupService();
        this.mealsLocalDataSource = new MealsLocalDataSource(context);
    }

    public Completable loginAsGuest() {
        return
            sharedPref.loginAsGuestRx();
    }

    public Single<Boolean> isGuest() {
        return Single.fromCallable(sharedPref::isGuest)
                .subscribeOn(Schedulers.io());
    }

    public Single<User> register(String email, String password, String name) {
        return firebaseServices.register(email, password, name)
                .subscribeOn(Schedulers.io());
    }

    public Single<User> login(String email, String password) {
        return firebaseServices.login(email, password);
    }

    public Single<Intent> getGoogleSignInIntent() {
        return firebaseServices.getGoogleSignInIntent();
    }

    public Single<User> handleGoogleSignInResult(Intent data) {
        return firebaseServices.loginWithGoogle(data);
    }

    public Completable sendPasswordResetEmail(String email) {
        return firebaseServices.sendPasswordResetEmail(email);
    }

    public Single<Boolean> isUserLoggedIn() {
        return firebaseServices.isUserLoggedIn();
    }

    public Single<String> getCurrentUserEmail() {
        return firebaseServices.getCurrentUserEmail();
    }

    public Single<String> getCurrentUserName() {
        return firebaseServices.getCurrentUserName();
    }

    public Single<Boolean> isNetworkAvailable() {
        return firebaseServices.isNetworkAvailable();
    }

    public Completable logoutWithBackup() {
        return mealRepository.getAllMealsFromLocal()
                .flatMapCompletable(meals ->
                        Completable.create(emitter -> {
                                    firebaseBackupService.backupAllMeals(meals);
                                    emitter.onComplete();
                                })
                                .onErrorComplete()
                                .andThen(mealRepository.clearAllLocalMeals())
                                .andThen(firebaseServices.logout())
                );
    }
    public Single<User> loginWithRestore(String email, String password) {
        return firebaseServices.login(email, password)
                .doOnSuccess(user ->
                        restoreUserData()
                                .subscribe(
                                        () -> Log.d("RESTORE", "Restore completed"),
                                        error -> Log.e("RESTORE", "Restore failed", error)
                                )
                );
    }

    public Single<User> handleGoogleSignInWithRestore(Intent data) {
        return firebaseServices.loginWithGoogle(data)
                .doOnSuccess(user ->
                        restoreUserData()
                                .subscribe(
                                        () -> Log.d("RESTORE", "Restore completed"),
                                        error -> Log.e("RESTORE", "Restore failed", error)
                                )
                );
    }
    private Completable restoreUserData() {

        return firebaseBackupService.restoreMealsFromFirebase()
                .subscribeOn(Schedulers.io())
                .flatMapCompletable(meals -> {
                    if (meals == null || meals.isEmpty()) {
                        Log.d("RESTORE", "No backup found");
                        return Completable.complete();
                    }
                    return saveMealsLocally(meals);
                });
    }
    private Completable saveMealsLocally(List<Meal> meals) {
        return Completable.fromAction(() -> {
            for (Meal meal : meals) {
                Log.d(TAG, "saveMealsLocally: " + meal.getName());
                mealsLocalDataSource.insertMeal(meal).subscribeOn(Schedulers.io()).blockingAwait();
            }
        });
    }
}