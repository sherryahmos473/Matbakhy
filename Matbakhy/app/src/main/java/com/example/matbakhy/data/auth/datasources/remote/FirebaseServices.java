// FirebaseAuthDataSourceImpl.java
package com.example.matbakhy.data.auth.datasources.remote;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.matbakhy.R;
import com.example.matbakhy.data.auth.callbacks.AuthCallback;
import com.example.matbakhy.data.auth.callbacks.SimpleCallback;
import com.example.matbakhy.data.auth.datasources.local.SharedPref;
import com.example.matbakhy.data.auth.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseServices implements com.example.matbakhy.data.auth.datasources.remote.FirebaseAuth {
    private static final String TAG = "FirebaseAuthDataSource";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Context context;
    private GoogleSignInClient googleSignInClient;
    private SharedPref sharedPrefDataSource;


    public void initialize(Context context) {
        this.context = context.getApplicationContext();
        sharedPrefDataSource = AuthNetwork.getInstance(context).sharedPref;

        try {
            Log.d(TAG, "Initializing Firebase services...");

            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            if (mAuth == null) {
                Log.e(TAG, "FirebaseAuth is null!");
                return;
            }

            Log.d(TAG, "Firebase initialized successfully");

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(context, gso);
            Log.d(TAG, "Google Sign-In initialized successfully");

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                Log.d(TAG, "Current Firebase user: " + currentUser.getEmail());
            } else {
                Log.d(TAG, "No current Firebase user");
            }

        } catch (Exception e) {
            Log.e(TAG, "Firebase initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendPasswordResetEmail(String email, SimpleCallback callback) {
        if (email == null || email.trim().isEmpty()) {
            callback.onFailure("Email is required");
            return;
        }

        if (!isValidEmail(email)) {
            callback.onFailure("Please enter a valid email address");
            return;
        }

        if (!isNetworkAvailable()) {
            callback.onFailure("No internet connection");
            return;
        }

        if (mAuth == null) {
            initialize(context);
            if (mAuth == null) {
                callback.onFailure("Authentication service not available");
                return;
            }
        }

        Log.d(TAG, "Sending password reset email to: " + email);

        mAuth.sendPasswordResetEmail(email.trim())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Password reset email sent successfully");
                        callback.onSuccess("Password reset email sent to " + email);
                    } else {
                        String error = getErrorMessage(task.getException());
                        callback.onFailure("Failed to send reset email: " + error);
                    }
                });
    }


    public Intent getGoogleSignInIntent() {
        if (googleSignInClient == null && context != null) {
            initialize(context);
        }
        return googleSignInClient != null ? googleSignInClient.getSignInIntent() : null;
    }


    public void handleGoogleSignInResult(Intent data, AuthCallback callback) {
        if (data == null) {
            callback.onFailure("Google sign in failed - no data returned");
            return;
        }

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null && account.getIdToken() != null) {
                firebaseAuthWithGoogle(account.getIdToken(), callback);
            } else {
                callback.onFailure("Google sign in failed - invalid account");
            }
        } catch (ApiException e) {
            Log.e(TAG, "Google sign in failed: " + e.getStatusCode() + ", " + e.getMessage());
            callback.onFailure("Google sign in failed: " + e.getMessage());
        }
    }

    private void firebaseAuthWithGoogle(String idToken, AuthCallback callback) {
        if (mAuth == null && context != null) {
            initialize(context);
            if (mAuth == null) {
                callback.onFailure("Authentication service not available");
                return;
            }
        }

        try {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                Log.d(TAG, "Google auth successful for: " + firebaseUser.getEmail());
                                createOrFetchUser(firebaseUser, "google", callback);
                            } else {
                                callback.onFailure("User not found after Google sign in");
                            }
                        } else {
                            String error = getErrorMessage(task.getException());
                            callback.onFailure("Google authentication failed: " + error);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error in firebaseAuthWithGoogle: " + e.getMessage());
            callback.onFailure("Google authentication error");
        }
    }


    public void register(String email, String password, String name, AuthCallback callback) {
        if (!validateInput(email, password, name)) {
            callback.onFailure("Invalid input");
            return;
        }

        if (!isNetworkAvailable()) {
            callback.onFailure("No internet connection");
            return;
        }

        if (mAuth == null && context != null) {
            initialize(context);
            if (mAuth == null) {
                callback.onFailure("Auth service not available");
                return;
            }
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            createOrFetchUser(firebaseUser, "email", callback);
                        } else {
                            callback.onFailure("User creation failed");
                        }
                    } else {
                        callback.onFailure(getErrorMessage(task.getException()));
                    }
                });
    }

    public void login(String email, String password, AuthCallback callback) {
        if (email.isEmpty() || password.isEmpty()) {
            callback.onFailure("Please enter email and password");
            return;
        }

        if (!isNetworkAvailable()) {
            callback.onFailure("No internet connection");
            return;
        }

        if (mAuth == null && context != null) {
            initialize(context);
            if (mAuth == null) {
                callback.onFailure("Auth service not available");
                return;
            }
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            createOrFetchUser(firebaseUser, "email", callback);
                        } else {
                            callback.onFailure("Login failed");
                        }
                    } else {
                        callback.onFailure(getErrorMessage(task.getException()));
                    }
                });
    }

    private void createOrFetchUser(FirebaseUser firebaseUser, String authProvider, AuthCallback callback) {
        String userId = firebaseUser.getUid();

        if (db == null) {
            User user = new User(userId, firebaseUser.getEmail(),
                    firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "User");

            sharedPrefDataSource.saveUserLogin(
                    user.getEmail(),
                    user.getName() != null ? user.getName() : "User",
                    user.getUid(),
                    new SimpleCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Log.d(TAG, message);
                            callback.onSuccess(user);
                        }

                        @Override
                        public void onFailure(String error) {
                            callback.onFailure("Failed to save user: " + error);
                        }
                    }
            );
            return;
        }

        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                        fetchUserFromFirestore(userId, callback);
                    } else {
                        createUserInFirestore(firebaseUser, authProvider, callback);
                    }
                });
    }

    private void createUserInFirestore(FirebaseUser firebaseUser, String authProvider, AuthCallback callback) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", firebaseUser.getUid());
        userData.put("email", firebaseUser.getEmail());
        userData.put("name", firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "User");
        userData.put("authProvider", authProvider);
        userData.put("createdAt", new Date());
        userData.put("updatedAt", new Date());

        db.collection("users").document(firebaseUser.getUid())
                .set(userData)
                .addOnCompleteListener(task -> {
                    User user = new User(firebaseUser.getUid(), firebaseUser.getEmail(),
                            firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "User");

                    sharedPrefDataSource.saveUserLogin(
                            user.getEmail(),
                            user.getName() != null ? user.getName() : "User",
                            user.getUid(),
                            new SimpleCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    Log.d(TAG, message);
                                    callback.onSuccess(user);
                                }

                                @Override
                                public void onFailure(String error) {
                                    callback.onFailure("Failed to save user: " + error);
                                }
                            }
                    );
                });
    }

    private void fetchUserFromFirestore(String userId, AuthCallback callback) {
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);
                            if (user != null) {
                                sharedPrefDataSource.saveUserLogin(
                                        user.getEmail(),
                                        user.getName() != null ? user.getName() : "User",
                                        user.getUid(),
                                        new SimpleCallback() {
                                            @Override
                                            public void onSuccess(String message) {
                                                Log.d(TAG, message);
                                                callback.onSuccess(user);
                                            }

                                            @Override
                                            public void onFailure(String error) {
                                                callback.onFailure("Failed to save user: " + error);
                                            }
                                        }
                                );
                            } else {
                                callback.onFailure("Failed to read user data");
                            }
                        } else {
                            callback.onFailure("User not found in Firestore");
                        }
                    } else {
                        callback.onFailure("Failed to fetch user from Firestore");
                    }
                });
    }

    public void logout() {
        if (googleSignInClient != null) {
            googleSignInClient.signOut();
        }
        if (mAuth != null) {
            mAuth.signOut();
        }
        sharedPrefDataSource.clearUserData(new SimpleCallback() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG, message);
            }

            @Override
            public void onFailure(String error) {
                Log.e(TAG, "Failed to clear user data: " + error);
            }
        });
        Log.d(TAG, "User logged out");
    }

    public boolean isUserLoggedIn() {
        boolean firebaseLoggedIn = mAuth != null && mAuth.getCurrentUser() != null;
        boolean sharedPrefLoggedIn = sharedPrefDataSource.isLoggedIn();
        Log.d(TAG, "isUserLoggedIn - Firebase: " + firebaseLoggedIn + ", SharedPref: " + sharedPrefLoggedIn);
        return firebaseLoggedIn && sharedPrefLoggedIn;
    }

    public String getCurrentUserEmail() {
        return sharedPrefDataSource.getUserEmail();
    }

    public String getCurrentUserName() {
        return sharedPrefDataSource.getUserName();
    }

    public boolean isNetworkAvailable() {
        if (context == null) return false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    private boolean validateInput(String email, String password, String name) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            return false;
        }
        return isValidEmail(email) && password.length() >= 6;
    }

    private String getErrorMessage(Exception exception) {
        if (exception == null) return "Unknown error";

        String error = exception.getMessage();
        if (error == null) return "Unknown error";

        if (error.contains("invalid-email")) return "Invalid email";
        if (error.contains("user-not-found")) return "User not found";
        if (error.contains("wrong-password")) return "Wrong password";
        if (error.contains("email-already-in-use")) return "Email already registered";
        if (error.contains("weak-password")) return "Password too weak (min 6 characters)";
        if (error.contains("network")) return "Network error";
        if (error.contains("invalid-credential")) return "Invalid email or password";

        return "Authentication failed: " + error;
    }
}