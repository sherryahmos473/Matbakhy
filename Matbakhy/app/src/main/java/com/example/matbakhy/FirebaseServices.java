package com.example.matbakhy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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

public class FirebaseServices {
    private static FirebaseServices instance;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Context context;
    private SharedPrefManager sharedPrefManager;
    private GoogleSignInClient googleSignInClient;

    private static final String TAG = "FirebaseServices";

    // Callback Interfaces
    public interface AuthCallback {
        void onSuccess(User user);
        void onFailure(String errorMessage);
    }

    public interface SimpleCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    // Private constructor - use getInstance()
    private FirebaseServices(Context context) {
        this.context = context.getApplicationContext();
        this.sharedPrefManager = SharedPrefManager.getInstance(this.context);
        initializeServices();
    }

    // Singleton pattern
    public static synchronized FirebaseServices getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseServices(context);
        }
        return instance;
    }

    // Initialize Firebase and Google Sign-In
    private void initializeServices() {
        try {
            Log.d(TAG, "Initializing Firebase services...");

            // Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            // Check if Firebase is properly initialized
            if (mAuth == null) {
                Log.e(TAG, "FirebaseAuth is null!");
                return;
            }

            Log.d(TAG, "Firebase initialized successfully");

            // Google Sign-In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(context, gso);
            Log.d(TAG, "Google Sign-In initialized successfully");

            // Log current user if any
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

    // ==================== FORGOT PASSWORD ====================
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
            initializeServices();
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

    // ==================== GOOGLE SIGN-IN ====================
    public Intent getGoogleSignInIntent() {
        if (googleSignInClient == null) {
            initializeServices();
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
        if (mAuth == null) {
            initializeServices();
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

    // ==================== EMAIL/PASSWORD AUTH ====================
    public void register(String email, String password, String name, AuthCallback callback) {
        if (!validateInput(email, password, name)) {
            callback.onFailure("Invalid input");
            return;
        }

        if (!isNetworkAvailable()) {
            callback.onFailure("No internet connection");
            return;
        }

        if (mAuth == null) {
            initializeServices();
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

        if (mAuth == null) {
            initializeServices();
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

    // ==================== USER MANAGEMENT ====================
    private void createOrFetchUser(FirebaseUser firebaseUser, String authProvider, AuthCallback callback) {
        String userId = firebaseUser.getUid();

        if (db == null) {
            // Create simple user without Firestore
            User user = new User(userId, firebaseUser.getEmail(),
                    firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "User");
            saveUserToSharedPref(user);
            callback.onSuccess(user);
            return;
        }

        // Check if user exists in Firestore
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                        // User exists, fetch it
                        fetchUserFromFirestore(userId, callback);
                    } else {
                        // Create new user
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
                    saveUserToSharedPref(user);
                    callback.onSuccess(user);
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
                                saveUserToSharedPref(user);
                                callback.onSuccess(user);
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

    // ==================== SHARED PREFERENCES ====================
    private void saveUserToSharedPref(User user) {
        if (user != null && user.getUid() != null && user.getEmail() != null) {
            sharedPrefManager.saveUserLogin(
                    user.getEmail(),
                    user.getName() != null ? user.getName() : "User",
                    user.getUid()
            );
            Log.d(TAG, "User saved to SharedPreferences: " + user.getEmail());
        }
    }

    // ==================== UTILITY METHODS ====================
    private boolean validateInput(String email, String password, String name) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            return false;
        }
        return isValidEmail(email) && password.length() >= 6;
    }

    public static boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
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

    // ==================== PUBLIC METHODS ====================
    public void logout() {
        if (googleSignInClient != null) {
            googleSignInClient.signOut();
        }
        if (mAuth != null) {
            mAuth.signOut();
        }
        sharedPrefManager.clearUserData();
        Log.d(TAG, "User logged out");
    }

    public boolean isUserLoggedIn() {
        boolean firebaseLoggedIn = mAuth != null && mAuth.getCurrentUser() != null;
        boolean sharedPrefLoggedIn = sharedPrefManager.isLoggedIn();
        Log.d(TAG, "isUserLoggedIn - Firebase: " + firebaseLoggedIn + ", SharedPref: " + sharedPrefLoggedIn);
        return firebaseLoggedIn && sharedPrefLoggedIn;
    }

    public String getCurrentUserEmail() {
        return sharedPrefManager.getUserEmail();
    }

    public String getCurrentUserName() {
        return sharedPrefManager.getUserName();
    }
}