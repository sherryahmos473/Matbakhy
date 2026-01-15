package com.example.matbakhy;

import android.content.Context;
import android.util.Log;
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

    // TAG for logging
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

    public FirebaseServices(Context context) {
        this.context = context;
        initializeFirebase();
    }

    private void initializeFirebase() {
        try {
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            Log.d(TAG, "Firebase initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Firebase initialization failed: " + e.getMessage());
        }
    }
    public void register(String email, String password, String name, RegistrationCallback callback) {
        if (email == null || email.trim().isEmpty()) {
            callback.onFailure("Email is required");
            return;
        }

        if (password == null || password.isEmpty()) {
            callback.onFailure("Password is required");
            return;
        }

        if (name == null || name.trim().isEmpty()) {
            callback.onFailure("Name is required");
            return;
        }

        if (!isValidEmail(email)) {
            callback.onFailure("Please enter a valid email address");
            return;
        }

        if (password.length() < 6) {
            callback.onFailure("Password must be at least 6 characters");
            return;
        }

        // Check Firebase initialization
        if (mAuth == null) {
            callback.onFailure("Authentication service not available");
            return;
        }

        Log.d(TAG, "Attempting to register user: " + email);

        // Create user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email.trim(), password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Update user profile with name
                            updateUserProfile(firebaseUser, name, email, callback);
                        } else {
                            callback.onFailure("User creation failed");
                        }
                    } else {
                        String error = getFirebaseErrorMessage(task.getException());
                        callback.onFailure("Registration failed: " + error);
                    }
                });
    }

    private void updateUserProfile(FirebaseUser firebaseUser, String name, String email, RegistrationCallback callback) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Store user data in Firestore
                        storeUserInFirestore(firebaseUser.getUid(), email, name, callback);
                    } else {
                        String error = getFirebaseErrorMessage(task.getException());
                        callback.onFailure("Profile update failed: " + error);
                    }
                });
    }

    public void login(String email, String password, LoginCallback callback) {
        // Input validation
        if (email == null || email.trim().isEmpty()) {
            callback.onFailure("Email is required");
            return;
        }

        if (password == null || password.isEmpty()) {
            callback.onFailure("Password is required");
            return;
        }

        if (!isValidEmail(email)) {
            callback.onFailure("Please enter a valid email address");
            return;
        }

        if (mAuth == null) {
            callback.onFailure("Authentication service not available");
            return;
        }

        Log.d(TAG, "Attempting to login user: " + email);

        mAuth.signInWithEmailAndPassword(email.trim(), password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Fetch complete user data from Firestore
                            fetchUserFromFirestore(firebaseUser.getUid(), callback);
                        } else {
                            callback.onFailure("Login failed - user not found");
                        }
                    } else {
                        String error = getFirebaseErrorMessage(task.getException());
                        callback.onFailure("Login failed: " + error);
                    }
                });
    }


    public void sendPasswordResetEmail(String email, PasswordResetCallback callback) {
        // Input validation
        if (email == null || email.trim().isEmpty()) {
            callback.onFailure("Email is required");
            return;
        }

        if (!isValidEmail(email)) {
            callback.onFailure("Please enter a valid email address");
            return;
        }

        if (mAuth == null) {
            callback.onFailure("Authentication service not available");
            return;
        }

        Log.d(TAG, "Sending password reset email to: " + email);

        mAuth.sendPasswordResetEmail(email.trim())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Password reset email sent successfully");
                        callback.onSuccess(email);
                    } else {
                        String error = getFirebaseErrorMessage(task.getException());
                        callback.onFailure("Failed to send reset email: " + error);
                    }
                });
    }

    public void logout() {
        if (mAuth != null) {
            mAuth.signOut();
            Log.d(TAG, "User logged out");
        }
    }

    public boolean isUserLoggedIn() {
        return mAuth != null && mAuth.getCurrentUser() != null;
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
            callback.onFailure("No user logged in");
        }
    }

    public void updateUserProfile(String userId, String name, String phone, String address, DataCallback callback) {
        if (db == null) {
            callback.onFailure("Database service not available");
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
                        callback.onSuccess("Profile updated successfully");
                    } else {
                        String error = getFirebaseErrorMessage(task.getException());
                        callback.onFailure("Failed to update profile: " + error);
                    }
                });
    }

    private void storeUserInFirestore(String userId, String email, String name, RegistrationCallback callback) {
        if (db == null) {
            // Create basic user object without Firestore data
            User user = new User(userId, email, name);
            callback.onSuccess(user);
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
                        callback.onSuccess(user);
                    } else {
                        String error = getFirebaseErrorMessage(task.getException());
                        Log.e(TAG, "Failed to store user data: " + error);
                        User user = new User(userId, email, name);
                        callback.onSuccess(user);
                    }
                });
    }

    private void fetchUserFromFirestore(String userId, LoginCallback callback) {
        if (db == null) {
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            if (firebaseUser != null) {
                User user = new User(
                        firebaseUser.getUid(),
                        firebaseUser.getEmail(),
                        firebaseUser.getDisplayName()
                );
                callback.onSuccess(user);
            } else {
                callback.onFailure("Failed to fetch user data");
            }
            return;
        }

        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Create User object from Firestore data
                            User user = document.toObject(User.class);
                            if (user != null) {
                                callback.onSuccess(user);
                            } else {
                                createUserFromFirebaseAuth(callback);
                            }
                        } else {
                            createUserFromFirebaseAuth(callback);
                        }
                    } else {
                        createUserFromFirebaseAuth(callback);
                    }
                });
    }

    private void createUserFromFirebaseAuth(LoginCallback callback) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            User user = new User(
                    firebaseUser.getUid(),
                    firebaseUser.getEmail(),
                    firebaseUser.getDisplayName()
            );
            callback.onSuccess(user);
        } else {
            callback.onFailure("User data not found");
        }
    }

    private String getFirebaseErrorMessage(Exception exception) {
        if (exception == null) {
            return "Unknown error";
        }

        String error = exception.getMessage();
        if (error == null) {
            return "Unknown error";
        }

        error = error.toLowerCase();

        // Authentication errors
        if (error.contains("invalid-email")) {
            return "Invalid email address";
        } else if (error.contains("user-not-found")) {
            return "No account found with this email";
        } else if (error.contains("wrong-password")) {
            return "Incorrect password";
        } else if (error.contains("email-already-in-use")) {
            return "Email already registered";
        } else if (error.contains("weak-password")) {
            return "Password is too weak";
        } else if (error.contains("too-many-requests")) {
            return "Too many attempts. Please try again later";
        } else if (error.contains("network")) {
            return "Network error. Check your connection";
        } else if (error.contains("user-disabled")) {
            return "This account has been disabled";
        } else if (error.contains("requires-recent-login")) {
            return "Please login again to perform this action";
        }

        // Return original error if no match
        return exception.getMessage();
    }

    public static boolean isValidEmail(String email) {
        return email != null &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
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
}