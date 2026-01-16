package com.example.matbakhy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseServices {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Context context;
    private SharedPrefManager sharedPrefManager;

    private static final String TAG = "FirebaseServices";

    // ==================== INTERFACES ====================
    public interface RegistrationCallback {
        void onSuccess(User user);
        void onFailure(String errorMessage);
    }

    public interface LoginCallback {
        void onSuccess(User user);
        void onFailure(String errorMessage);
    }

    public interface PasswordResetCallback {
        void onSuccess(String email);
        void onFailure(String errorMessage);
    }

    public interface DataCallback {
        void onSuccess(Object data);
        void onFailure(String errorMessage);
    }

    // ==================== CONSTRUCTOR ====================
    public FirebaseServices(Context context) {
        this.context = context;
        this.sharedPrefManager = SharedPrefManager.getInstance(context);
        initializeFirebase();
    }

    // ==================== INITIALIZATION ====================
    private void initializeFirebase() {
        try {
            Log.d(TAG, "Initializing Firebase...");

            // Ensure Firebase is initialized
            if (FirebaseApp.getApps(context).isEmpty()) {
                Log.e(TAG, "FirebaseApp not initialized!");
                FirebaseApp.initializeApp(context);
            }

            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            if (mAuth == null) {
                Log.e(TAG, "FirebaseAuth initialization failed!");
            } else {
                Log.d(TAG, "FirebaseAuth initialized successfully");
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    Log.d(TAG, "Current user from Firebase: " + currentUser.getEmail());
                    if (!sharedPrefManager.isLoggedIn()) {
                        Log.d(TAG, "Firebase has user but SharedPref doesn't, syncing...");
                        fetchUserFromFirestore(currentUser.getUid(), new LoginCallback() {
                            @Override
                            public void onSuccess(User user) {
                                saveUserToSharedPref(user);
                                Log.d(TAG, "User synced to SharedPref: " + user.getEmail());
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Log.e(TAG, "Failed to sync user to SharedPref: " + errorMessage);
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "No current user in Firebase");
                }
            }

            if (db == null) {
                Log.e(TAG, "FirebaseFirestore initialization failed!");
            } else {
                Log.d(TAG, "FirebaseFirestore initialized successfully");
            }

        } catch (Exception e) {
            Log.e(TAG, "Firebase initialization failed: " + e.getMessage(), e);
        }
    }

    public void register(String email, String password, String name, RegistrationCallback callback) {
        Log.d(TAG, "Registering user: " + email);

        // Validate inputs
        if (!validateRegistrationInputs(email, password, name, callback)) {
            return;
        }

        if (!isNetworkAvailable()) {
            runOnUiThread(() -> callback.onFailure("No internet connection"));
            return;
        }

        if (mAuth == null) {
            initializeFirebase();
            if (mAuth == null) {
                runOnUiThread(() -> callback.onFailure("Authentication service not available"));
                return;
            }
        }
        mAuth.createUserWithEmailAndPassword(email.trim(), password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            updateUserProfile(firebaseUser, name, email, callback);
                        } else {
                            runOnUiThread(() -> callback.onFailure("User creation failed - no user returned"));
                        }
                    } else {
                        String error = getFirebaseErrorMessage(task.getException());
                        Log.e(TAG, "Registration failed: " + error);
                        runOnUiThread(() -> callback.onFailure("Registration failed: " + error));
                    }
                });
    }

    public void login(String email, String password, LoginCallback callback) {
        Log.d(TAG, "=== LOGIN METHOD STARTED ===");
        Log.d(TAG, "Email: " + email);
        Log.d(TAG, "Thread: " + Thread.currentThread().getName());

        // Validate inputs
        if (!validateLoginInputs(email, password, callback)) {
            return;
        }

        if (!isNetworkAvailable()) {
            runOnUiThread(() -> callback.onFailure("No internet connection. Please check your network."));
            return;
        }

        if (mAuth == null) {
            Log.w(TAG, "FirebaseAuth is null, reinitializing...");
            initializeFirebase();
            if (mAuth == null) {
                runOnUiThread(() -> callback.onFailure("Authentication service not available"));
                return;
            }
        }

        Log.d(TAG, "Calling signInWithEmailAndPassword...");

        mAuth.signInWithEmailAndPassword(email.trim(), password)
                .addOnCompleteListener(task -> {
                    Log.d(TAG, "Login task completed. Success: " + task.isSuccessful());

                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            Log.d(TAG, "User authenticated: " + firebaseUser.getEmail());
                            Log.d(TAG, "User UID: " + firebaseUser.getUid());

                            // Fetch user data from Firestore
                            fetchUserFromFirestore(firebaseUser.getUid(), callback);
                        } else {
                            Log.e(TAG, "FirebaseUser is null after successful login!");
                            runOnUiThread(() -> callback.onFailure("Login failed - user data not found"));
                        }
                    } else {
                        Exception exception = task.getException();
                        String error = getFirebaseErrorMessage(exception);
                        Log.e(TAG, "Login failed: " + error);
                        runOnUiThread(() -> callback.onFailure("Login failed: " + error));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Login onFailure: " + e.getMessage(), e);
                    runOnUiThread(() -> callback.onFailure("Login failed: " + e.getMessage()));
                });
    }

    public void sendPasswordResetEmail(String email, PasswordResetCallback callback) {
        if (email == null || email.trim().isEmpty()) {
            runOnUiThread(() -> callback.onFailure("Email is required"));
            return;
        }

        if (!isValidEmail(email)) {
            runOnUiThread(() -> callback.onFailure("Please enter a valid email address"));
            return;
        }

        if (!isNetworkAvailable()) {
            runOnUiThread(() -> callback.onFailure("No internet connection"));
            return;
        }

        if (mAuth == null) {
            runOnUiThread(() -> callback.onFailure("Authentication service not available"));
            return;
        }

        Log.d(TAG, "Sending password reset email to: " + email);

        mAuth.sendPasswordResetEmail(email.trim())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Password reset email sent successfully");
                        runOnUiThread(() -> callback.onSuccess(email));
                    } else {
                        String error = getFirebaseErrorMessage(task.getException());
                        runOnUiThread(() -> callback.onFailure("Failed to send reset email: " + error));
                    }
                });
    }

    // ==================== USER MANAGEMENT METHODS ====================
    public void logout() {
        Log.d(TAG, "Logging out user...");

        // Clear Firebase authentication
        if (mAuth != null) {
            mAuth.signOut();
            Log.d(TAG, "User logged out from Firebase");
        }

        // Clear SharedPreferences
        sharedPrefManager.clearUserData();
        Log.d(TAG, "User data cleared from SharedPreferences");
    }

    public boolean isUserLoggedIn() {
        // Check both Firebase authentication and SharedPreferences
        boolean firebaseLoggedIn = mAuth != null && mAuth.getCurrentUser() != null;
        boolean sharedPrefLoggedIn = sharedPrefManager.isLoggedIn();

        Log.d(TAG, "Login check - Firebase: " + firebaseLoggedIn + ", SharedPref: " + sharedPrefLoggedIn);

        // Return true only if both agree user is logged in
        return firebaseLoggedIn && sharedPrefLoggedIn;
    }

    public FirebaseUser getCurrentFirebaseUser() {
        if (mAuth != null) {
            return mAuth.getCurrentUser();
        }
        return null;
    }

    public void getCurrentUser(LoginCallback callback) {
        FirebaseUser firebaseUser = getCurrentFirebaseUser();
        if (firebaseUser != null) {
            fetchUserFromFirestore(firebaseUser.getUid(), callback);
        } else {
            runOnUiThread(() -> callback.onFailure("No user logged in"));
        }
    }

    public String getCurrentUserEmail() {
        // Try to get email from SharedPreferences first (faster)
        String email = sharedPrefManager.getUserEmail();
        if (!email.isEmpty()) {
            return email;
        }

        // Fall back to Firebase if SharedPreferences is empty
        FirebaseUser user = getCurrentFirebaseUser();
        return user != null ? user.getEmail() : "";
    }

    public String getCurrentUserName() {
        return sharedPrefManager.getUserName();
    }

    public String getCurrentUserId() {
        return sharedPrefManager.getUserId();
    }

    public void updateUserProfile(String userId, String name, String phone, String address, DataCallback callback) {
        if (db == null) {
            runOnUiThread(() -> callback.onFailure("Database service not available"));
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        if (name != null && !name.trim().isEmpty()) {
            updates.put("name", name.trim());
        }
        if (phone != null && !phone.trim().isEmpty()) {
            updates.put("phone", phone.trim());
        }
        if (address != null && !address.trim().isEmpty()) {
            updates.put("address", address.trim());
        }
        updates.put("updatedAt", FieldValue.serverTimestamp());

        db.collection("users").document(userId)
                .update(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Update SharedPreferences if name changed
                        if (name != null && !name.trim().isEmpty()) {
                            sharedPrefManager.saveUserLogin(
                                    sharedPrefManager.getUserEmail(),
                                    name.trim(),
                                    userId
                            );
                        }
                        runOnUiThread(() -> callback.onSuccess("Profile updated successfully"));
                    } else {
                        String error = getFirebaseErrorMessage(task.getException());
                        runOnUiThread(() -> callback.onFailure("Failed to update profile: " + error));
                    }
                });
    }

    private void saveUserToSharedPref(User user) {
        if (user != null && user.getUid() != null && user.getEmail() != null) {
            sharedPrefManager.saveUserLogin(
                    user.getEmail(),
                    user.getName() != null ? user.getName() : "User",
                    user.getUid()
            );
            Log.d(TAG, "User saved to SharedPreferences: " + user.getEmail());
        } else {
            Log.e(TAG, "Cannot save null user to SharedPreferences");
        }
    }

    private boolean validateRegistrationInputs(String email, String password, String name, RegistrationCallback callback) {
        if (email == null || email.trim().isEmpty()) {
            runOnUiThread(() -> callback.onFailure("Email is required"));
            return false;
        }

        if (password == null || password.isEmpty()) {
            runOnUiThread(() -> callback.onFailure("Password is required"));
            return false;
        }

        if (name == null || name.trim().isEmpty()) {
            runOnUiThread(() -> callback.onFailure("Name is required"));
            return false;
        }

        if (!isValidEmail(email)) {
            runOnUiThread(() -> callback.onFailure("Please enter a valid email address"));
            return false;
        }

        if (!isValidPassword(password)) {
            runOnUiThread(() -> callback.onFailure("Password must be at least 6 characters"));
            return false;
        }

        return true;
    }

    private boolean validateLoginInputs(String email, String password, LoginCallback callback) {
        if (email == null || email.trim().isEmpty()) {
            runOnUiThread(() -> callback.onFailure("Email is required"));
            return false;
        }

        if (password == null || password.isEmpty()) {
            runOnUiThread(() -> callback.onFailure("Password is required"));
            return false;
        }

        if (!isValidEmail(email)) {
            runOnUiThread(() -> callback.onFailure("Please enter a valid email address"));
            return false;
        }

        return true;
    }

    private void updateUserProfile(FirebaseUser firebaseUser, String name, String email, RegistrationCallback callback) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storeUserInFirestore(firebaseUser.getUid(), email, name, callback);
                    } else {
                        String error = getFirebaseErrorMessage(task.getException());
                        runOnUiThread(() -> callback.onFailure("Profile update failed: " + error));
                    }
                });
    }

    private void storeUserInFirestore(String userId, String email, String name, RegistrationCallback callback) {
        if (db == null) {
            Log.w(TAG, "Firestore not available, creating user without Firestore data");
            User user = new User(userId, email, name);
            saveUserToSharedPref(user);
            runOnUiThread(() -> callback.onSuccess(user));
            return;
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", userId);
        userData.put("email", email);
        userData.put("name", name);
        userData.put("createdAt", new Date());
        userData.put("updatedAt", new Date());

        db.collection("users").document(userId)
                .set(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User data stored in Firestore: " + userId);
                        User user = new User(userId, email, name);
                        saveUserToSharedPref(user);
                        runOnUiThread(() -> callback.onSuccess(user));
                    } else {
                        String error = getFirebaseErrorMessage(task.getException());
                        Log.e(TAG, "Failed to store user data in Firestore: " + error);
                        User user = new User(userId, email, name);
                        saveUserToSharedPref(user);
                        runOnUiThread(() -> callback.onSuccess(user));
                    }
                });
    }

    private void fetchUserFromFirestore(String userId, LoginCallback callback) {
        Log.d(TAG, "Fetching user from Firestore, UID: " + userId);

        if (db == null) {
            Log.w(TAG, "Firestore not available, using Firebase Auth data");
            createUserFromFirebaseAuth(callback);
            return;
        }
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "User document found in Firestore");
                            try {
                                User user = document.toObject(User.class);
                                if (user != null) {
                                    Log.d(TAG, "User object created from Firestore: " + user.getEmail());
                                    saveUserToSharedPref(user);
                                    runOnUiThread(() -> callback.onSuccess(user));
                                } else {
                                    Log.w(TAG, "User object is null from Firestore, using Auth data");
                                    createUserFromFirebaseAuth(callback);
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error deserializing user from Firestore: " + e.getMessage());
                                User user = manualMapDocumentToUser(document);
                                if (user != null) {
                                    saveUserToSharedPref(user);
                                    runOnUiThread(() -> callback.onSuccess(user));
                                } else {
                                    createUserFromFirebaseAuth(callback);
                                }
                            }
                        } else {
                            Log.w(TAG, "User document not found in Firestore, creating from Auth");
                            createUserFromFirebaseAuth(callback);
                        }
                    } else {
                        Log.w(TAG, "Firestore fetch failed, using Auth data");
                        createUserFromFirebaseAuth(callback);
                    }
                });
    }

    private User manualMapDocumentToUser(DocumentSnapshot document) {
        try {
            String uid = document.getString("uid");
            String email = document.getString("email");
            String name = document.getString("name");

            if (uid == null || email == null) {
                Log.e(TAG, "Required fields missing in document");
                return null;
            }

            User user = new User(uid, email, name != null ? name : "User");

            Log.d(TAG, "Manual mapping successful for: " + email);
            return user;

        } catch (Exception e) {
            Log.e(TAG, "Error in manualMapDocumentToUser: " + e.getMessage());
            return null;
        }
    }

    private void createUserFromFirebaseAuth(LoginCallback callback) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            User user = new User(
                    firebaseUser.getUid(),
                    firebaseUser.getEmail(),
                    firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "User"
            );
            Log.d(TAG, "Created user from FirebaseAuth: " + user.getEmail());
            saveUserToSharedPref(user);
            runOnUiThread(() -> callback.onSuccess(user));
        } else {
            Log.e(TAG, "Cannot create user from FirebaseAuth - current user is null");
            runOnUiThread(() -> callback.onFailure("User data not found"));
        }
    }

    private String getFirebaseErrorMessage(Exception exception) {
        if (exception == null) {
            return "Unknown error occurred";
        }

        String error = exception.getMessage();
        if (error == null) {
            return "Unknown error";
        }

        error = error.toLowerCase();

        if (error.contains("invalid-email")) {
            return "Invalid email address format";
        } else if (error.contains("user-not-found")) {
            return "No account found with this email";
        } else if (error.contains("wrong-password")) {
            return "Incorrect password";
        } else if (error.contains("email-already-in-use")) {
            return "Email already registered";
        } else if (error.contains("weak-password")) {
            return "Password is too weak (minimum 6 characters)";
        } else if (error.contains("too-many-requests")) {
            return "Too many attempts. Please try again later";
        } else if (error.contains("network")) {
            return "Network error. Please check your internet connection";
        } else if (error.contains("user-disabled")) {
            return "This account has been disabled";
        } else if (error.contains("requires-recent-login")) {
            return "Session expired. Please login again";
        } else if (error.contains("invalid-credential")) {
            return "Invalid email or password";
        }

        return exception.getMessage();
    }

    private void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public static boolean isValidEmail(String email) {
        return email != null &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public boolean isNetworkAvailable() {
        if (context == null) return false;
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        } catch (Exception e) {
            Log.e(TAG, "Network check failed: " + e.getMessage());
        }

        return false;
    }

    public boolean isFirebaseAvailable() {
        return mAuth != null && db != null;
    }

    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    public FirebaseFirestore getFirestore() {
        return db;
    }

    public SharedPrefManager getSharedPrefManager() {
        return sharedPrefManager;
    }
}