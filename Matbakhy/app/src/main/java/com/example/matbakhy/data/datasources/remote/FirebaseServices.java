package com.example.matbakhy.data.datasources.remote;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.matbakhy.R;
import com.example.matbakhy.data.datasources.local.SharedPrefServices;
import com.example.matbakhy.data.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class FirebaseServices {
    private static final String TAG = "FirebaseServices";

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;
    private final GoogleSignInClient googleSignInClient;
    private final SharedPrefServices sharedPref;
    private final Context appContext;

    public FirebaseServices(Context context) {
        this.appContext = context.getApplicationContext();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
        this.sharedPref = SharedPrefServices.getInstance(appContext);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        this.googleSignInClient = GoogleSignIn.getClient(appContext, gso);
    }

    public Single<User> register(String email, String password, String name) {
        return Single.fromCallable(() -> {
                    validateInput(email, password, name);
                    checkNetworkAvailable();
                    return true;
                })
                .flatMap(valid ->
                        Single.<FirebaseUser>create(emitter -> {
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            if (user != null) {
                                                emitter.onSuccess(user);
                                            } else {
                                                emitter.onError(new Exception("User creation failed"));
                                            }
                                        } else {
                                            emitter.onError(task.getException());
                                        }
                                    });
                        })
                )
                .flatMap(firebaseUser ->
                        updateUserProfile(firebaseUser, name)
                                .flatMap(updatedUser -> createOrFetchUserRx(updatedUser, "email"))
                )
                .subscribeOn(Schedulers.io());
    }

    public Single<User> login(String email, String password) {
        return Single.fromCallable(() -> {
                    if (email.isEmpty() || password.isEmpty()) {
                        throw new Exception("Please enter email and password");
                    }
                    checkNetworkAvailable();
                    return true;
                })
                .flatMap(valid ->
                        Single.<FirebaseUser>create(emitter -> {
                            firebaseAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            if (user != null) {
                                                emitter.onSuccess(user);
                                            } else {
                                                emitter.onError(new Exception("Login failed"));
                                            }
                                        } else {
                                            emitter.onError(task.getException());
                                        }
                                    });
                        })
                )
                .flatMap(firebaseUser -> createOrFetchUserRx(firebaseUser, "email"))
                .subscribeOn(Schedulers.io());
    }

    public Single<Intent> getGoogleSignInIntent() {
        return Single.fromCallable(() -> googleSignInClient.getSignInIntent())
                .subscribeOn(Schedulers.io());
    }

    public Single<User> loginWithGoogle(Intent data) {
        return Single.fromCallable(() -> Tasks.await(GoogleSignIn.getSignedInAccountFromIntent(data)))
                .subscribeOn(Schedulers.io())
                .flatMap(account -> {
                    if (account == null || account.getIdToken() == null) {
                        return Single.error(new Exception("Invalid Google sign-in data"));
                    }
                    return firebaseAuthWithGoogleRx(account)
                            .flatMap(firebaseUser -> createOrFetchUserRx(firebaseUser, "google"));
                });
    }

    public Single<User> loginWithGoogleAccount(GoogleSignInAccount account) {
        return Single.fromCallable(() -> {
                    if (account == null || account.getIdToken() == null) {
                        throw new Exception("Invalid Google account");
                    }
                    return account;
                })
                .subscribeOn(Schedulers.io())
                .flatMap(this::firebaseAuthWithGoogleRx)
                .flatMap(firebaseUser -> createOrFetchUserRx(firebaseUser, "google"));
    }

    public Completable sendPasswordResetEmail(String email) {
        return Single.fromCallable(() -> {
                    if (!isValidEmail(email)) {
                        throw new Exception("Please enter a valid email address");
                    }
                    checkNetworkAvailable();
                    return email.trim();
                })
                .flatMapCompletable(validEmail ->
                        Completable.create(emitter -> {
                            firebaseAuth.sendPasswordResetEmail(validEmail)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            emitter.onComplete();
                                        } else {
                                            emitter.onError(task.getException());
                                        }
                                    });
                        })
                )
                .subscribeOn(Schedulers.io());
    }

    public Completable logout() {
        return Completable.fromAction(() -> {
                    googleSignInClient.signOut();
                    firebaseAuth.signOut();
                    sharedPref.clearUserDataSync();
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> isUserLoggedIn() {
        return Single.fromCallable(() -> {
                    boolean firebaseLoggedIn = firebaseAuth.getCurrentUser() != null;
                    boolean sharedPrefLoggedIn = sharedPref.isLoggedInSync();
                    return firebaseLoggedIn && sharedPrefLoggedIn;
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<String> getCurrentUserEmail() {
        return Single.fromCallable(() -> {
                    String email = sharedPref.getUserEmailSync();
                    if (email.isEmpty() && firebaseAuth.getCurrentUser() != null) {
                        email = firebaseAuth.getCurrentUser().getEmail();
                    }
                    return email != null ? email : "";
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<String> getCurrentUserName() {
        return Single.fromCallable(() -> {
                    String name = sharedPref.getUserNameSync();
                    if (name.isEmpty() && firebaseAuth.getCurrentUser() != null) {
                        name = firebaseAuth.getCurrentUser().getDisplayName();
                    }
                    return name != null ? name : "";
                })
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> isNetworkAvailable() {
        return Single.fromCallable(() -> {
                    ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    return activeNetwork != null && activeNetwork.isConnected();
                })
                .subscribeOn(Schedulers.io());
    }

    private Single<FirebaseUser> firebaseAuthWithGoogleRx(GoogleSignInAccount account) {
        return Single.create(emitter -> {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                emitter.onSuccess(user);
                            } else {
                                emitter.onError(new Exception("User not found after Google sign in"));
                            }
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    private Single<FirebaseUser> updateUserProfile(FirebaseUser firebaseUser, String name) {
        return Single.create(emitter -> {
            com.google.firebase.auth.UserProfileChangeRequest profileUpdates =
                    new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();

            firebaseUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onSuccess(firebaseUser);
                        } else {
                            emitter.onError(task.getException());
                        }
                    });
        });
    }

    private Single<User> createOrFetchUserRx(FirebaseUser firebaseUser, String authProvider) {
        String userId = firebaseUser.getUid();

        if (firestore == null) {
            return createBasicUser(firebaseUser);
        }

        return Single.<DocumentSnapshot>create(emitter -> {
                    firestore.collection("users").document(userId).get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    emitter.onSuccess(task.getResult());
                                } else {
                                    emitter.onError(task.getException());
                                }
                            });
                })
                .flatMap(documentSnapshot -> {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        return fetchUserFromFirestoreRx(documentSnapshot);
                    } else {
                        return createUserInFirestoreRx(firebaseUser, authProvider);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    private Single<User> createBasicUser(FirebaseUser firebaseUser) {
        User user = new User(
                firebaseUser.getUid(),
                firebaseUser.getEmail(),
                firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "User"
        );

        return saveUserToSharedPrefRx(user).andThen(Single.just(user));
    }

    private Single<User> createUserInFirestoreRx(FirebaseUser firebaseUser, String authProvider) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", firebaseUser.getUid());
        userData.put("email", firebaseUser.getEmail());
        userData.put("name", firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "User");
        userData.put("authProvider", authProvider);
        userData.put("createdAt", new Date());
        userData.put("updatedAt", new Date());

        return Single.<Void>create(emitter -> {
                    firestore.collection("users").document(firebaseUser.getUid())
                            .set(userData)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    emitter.onSuccess(null);
                                } else {
                                    emitter.onError(task.getException());
                                }
                            });
                })
                .flatMap(voidValue -> {
                    User user = new User(
                            firebaseUser.getUid(),
                            firebaseUser.getEmail(),
                            firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "User"
                    );
                    return saveUserToSharedPrefRx(user).andThen(Single.just(user));
                });
    }

    private Single<User> fetchUserFromFirestoreRx(DocumentSnapshot document) {
        User user = document.toObject(User.class);
        if (user == null) {
            return Single.error(new Exception("Failed to read user data from Firestore"));
        }

        return saveUserToSharedPrefRx(user).andThen(Single.just(user));
    }

    private Completable saveUserToSharedPrefRx(User user) {
        return Completable.fromAction(() -> {
            sharedPref.saveUserLoginSync(
                    user.getEmail(),
                    user.getName() != null ? user.getName() : "User",
                    user.getUid()
            );
        });
    }

    private void validateInput(String email, String password, String name) throws Exception {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            throw new Exception("All fields are required");
        }
        if (!isValidEmail(email)) {
            throw new Exception("Please enter a valid email address");
        }
        if (!isValidPassword(password)) {
            throw new Exception("Password must be at least 6 characters");
        }
    }

    private void checkNetworkAvailable() throws Exception {
        if (!isNetworkAvailableSync()) {
            throw new Exception("No internet connection");
        }
    }

    private boolean isNetworkAvailableSync() {
        try {
            ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    // Callback methods for backward compatibility
    public void register(String email, String password, String name, com.example.matbakhy.data.callbacks.AuthCallback callback) {
        register(email, password, name)
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onFailure(error.getMessage())
                );
    }

    public void login(String email, String password, com.example.matbakhy.data.callbacks.AuthCallback callback) {
        login(email, password)
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onFailure(error.getMessage())
                );
    }

    public void handleGoogleSignInResult(Intent data, com.example.matbakhy.data.callbacks.AuthCallback callback) {
        loginWithGoogle(data)
                .subscribe(
                        callback::onSuccess,
                        error -> callback.onFailure(error.getMessage())
                );
    }

    public void sendPasswordResetEmail(String email, com.example.matbakhy.data.callbacks.SimpleCallback callback) {
        sendPasswordResetEmail(email)
                .subscribe(
                        () -> callback.onSuccess("Password reset email sent"),
                        error -> callback.onFailure(error.getMessage())
                );
    }
}