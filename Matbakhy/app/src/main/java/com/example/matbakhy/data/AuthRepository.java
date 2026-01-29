package com.example.matbakhy.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.matbakhy.data.callbacks.AuthCallback;
import com.example.matbakhy.data.callbacks.LogoutCallback;
import com.example.matbakhy.data.callbacks.RestoreCallback;
import com.example.matbakhy.data.callbacks.SimpleCallback;
import com.example.matbakhy.data.datasources.local.SharedPrefServices;
import com.example.matbakhy.data.datasources.remote.FirebaseBackupService;
import com.example.matbakhy.data.datasources.remote.FirebaseServices;
import com.example.matbakhy.data.datasources.remote.Network;
import com.example.matbakhy.data.model.Meal;
import com.example.matbakhy.data.model.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AuthRepository {
    private static final String TAG = "AuthRepository";

    private final FirebaseServices firebaseServices;
    private final FirebaseBackupService firebaseBackupService;
    private final SharedPrefServices sharedPref;
    private final MealRepository mealRepository;
    private final Context context;

    public AuthRepository(Context context) {
        this.context = context.getApplicationContext();
        this.firebaseServices = Network.getInstance(this.context).firebaseServices;
        this.mealRepository = new MealRepository(this.context);
        this.sharedPref = SharedPrefServices.getInstance(this.context);
        this.firebaseBackupService = new FirebaseBackupService();
    }

    public Completable loginAsGuest() {
        return Completable.create(emitter -> {
            sharedPref.loginAsGuest(new SimpleCallback() {
                @Override
                public void onSuccess(String message) {
                    emitter.onComplete();
                }

                @Override
                public void onFailure(String error) {
                    emitter.onError(new Exception(error));
                }
            });
        }).subscribeOn(Schedulers.io());
    }

    public Single<Boolean> isGuest() {
        return Single.fromCallable(() -> sharedPref.isGuest())
                .subscribeOn(Schedulers.io());
    }

    public Single<User> register(String email, String password, String name) {
        return firebaseServices.register(email, password, name)
                .subscribeOn(Schedulers.io());
    }

    public Single<User> login(String email, String password) {
        return firebaseServices.login(email, password)
                .subscribeOn(Schedulers.io());
    }

    public Single<Intent> getGoogleSignInIntent() {
        return firebaseServices.getGoogleSignInIntent()
                .subscribeOn(Schedulers.io());
    }

    public Single<User> handleGoogleSignInResult(Intent data) {
        return firebaseServices.loginWithGoogle(data)
                .subscribeOn(Schedulers.io());
    }

    public Completable sendPasswordResetEmail(String email) {
        return firebaseServices.sendPasswordResetEmail(email)
                .subscribeOn(Schedulers.io());
    }

    public Completable logout() {
        Log.d(TAG, "logout: ");
        return firebaseServices.logout()
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> isUserLoggedIn() {
        return firebaseServices.isUserLoggedIn()
                .subscribeOn(Schedulers.io());
    }

    public Single<String> getCurrentUserEmail() {
        return firebaseServices.getCurrentUserEmail()
                .subscribeOn(Schedulers.io());
    }

    public Single<String> getCurrentUserName() {
        return firebaseServices.getCurrentUserName()
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> isNetworkAvailable() {
        return firebaseServices.isNetworkAvailable()
                .subscribeOn(Schedulers.io());
    }

    public Completable logoutWithBackup() {
            return Completable.create(emitter -> {
                mealRepository.getAllMealsFromLocal()
                        .subscribe(meals -> {
                            firebaseBackupService.backupAllMeals(meals);
                            mealRepository.clearAllLocalMeals()
                                    .subscribe(() -> {
                                        logout().subscribe(emitter::onComplete, emitter::onError);
                                    }, emitter::onError);
                        }, emitter::onError);
            }).subscribeOn(Schedulers.io());

    }
    public Single<User> loginWithRestore(String email, String password) {
        return firebaseServices.login(email, password)
                .flatMap(user ->
                        restoreUserData()
                                .map(result -> user)
                                .onErrorReturn(throwable -> user)
                )
                .subscribeOn(Schedulers.io());
    }

    public Single<User> handleGoogleSignInWithRestore(Intent data) {
        return firebaseServices.loginWithGoogle(data)
                .flatMap(user ->
                        restoreUserData()
                                .map(result -> user)
                                .onErrorReturn(throwable -> user)
                )
                .subscribeOn(Schedulers.io());
    }

    private Single<RestoreResult> restoreUserData() {
        return Single.create(emitter -> {
            firebaseBackupService.restoreMealsFromFirebase(new RestoreCallback() {
                @Override
                public void onSuccess(int restoredCount, List<Meal> meals, String message) {
                    if (meals != null && !meals.isEmpty()) {
                        saveMealsLocally(meals)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe(
                                        () -> emitter.onSuccess(new RestoreResult(true, restoredCount, message)),
                                        error -> emitter.onSuccess(new RestoreResult(false, restoredCount, "Save failed: " + error.getMessage()))
                                );
                    } else {
                        emitter.onSuccess(new RestoreResult(true, 0, message));
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    emitter.onSuccess(new RestoreResult(false, 0, errorMessage));
                }
            });
        });
    }

    private Completable saveMealsLocally(List<Meal> meals) {
        return Completable.create(emitter -> {
            for (Meal meal : meals) {
                mealRepository.insertMeal(meal)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                () -> {},
                                error -> Log.e(TAG, "Error saving meal: " + meal.getId(), error)
                        );
            }
            emitter.onComplete();
        });
    }

    public void register(String email, String password, String name, AuthCallback callback) {
        register(email, password, name)
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onFailure(error.getMessage())
                );
    }

    public void login(String email, String password, AuthCallback callback) {
        login(email, password)
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onFailure(error.getMessage())
                );
    }

    public void handleGoogleSignInResult(Intent data, AuthCallback callback) {
        handleGoogleSignInResult(data)
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onFailure(error.getMessage())
                );
    }

    public void sendPasswordResetEmail(String email, SimpleCallback callback) {
        sendPasswordResetEmail(email)
                .subscribe(
                        () -> callback.onSuccess("Password reset email sent"),
                        error -> callback.onFailure(error.getMessage())
                );
    }

    public void loginAsGuest(SimpleCallback callback) {
        loginAsGuest()
                .subscribe(
                        () -> callback.onSuccess("User logged in as a guest"),
                        error -> callback.onFailure(error.getMessage())
                );
    }

    public void logoutWithBackup(LogoutCallback callback) {
        logoutWithBackup()
                .subscribe(
                        () -> callback.onLogoutComplete(true, "Logged out successfully"),
                        error -> callback.onLogoutComplete(false, error.getMessage())
                );
    }

    public void loginWithRestore(String email, String password, AuthCallback callback) {
        loginWithRestore(email, password)
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onFailure(error.getMessage())
                );
    }

    private static class RestoreResult {
        private final boolean success;
        private final int restoredCount;
        private final String message;

        public RestoreResult(boolean success, int restoredCount, String message) {
            this.success = success;
            this.restoredCount = restoredCount;
            this.message = message;
        }
    }
}