package com.example.matbakhy;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseServices {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Context context;
    private RegistrationCallback callback;

    public interface RegistrationCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public FirebaseServices(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void register(String email, String password, String name, RegistrationCallback callback) {
        this.callback = callback;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        com.google.firebase.auth.UserProfileChangeRequest profileUpdates =
                                new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();

                        user.updateProfile(profileUpdates).addOnCompleteListener(profileTask -> {
                            if (profileTask.isSuccessful()) {
                                storeUserInFirestore(user.getUid(), email, name);
                            } else {
                                callback.onFailure("Profile update failed: " + profileTask.getException().getMessage());
                            }
                        });
                    } else {
                        callback.onFailure("Registration failed: " + task.getException().getMessage());
                    }
                });
    }

    private void storeUserInFirestore(String userId, String email, String name) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("name", name);
        user.put("createdAt", new Date());
        user.put("userId", userId);

        db.collection("users").document(userId)
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure("Failed to store user data: " + task.getException().getMessage());
                    }
                });
    }

    public void sendPasswordResetEmail(String email, PasswordResetCallback callback) {
        if (email == null || email.trim().isEmpty()) {
            callback.onFailure("Please enter your email address");
            return;
        }

        if (!isValidEmail(email)) {
            callback.onFailure("Please enter a valid email address");
            return;
        }

        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
            if (mAuth == null) {
                callback.onFailure("Authentication service not available");
                return;
            }
        }

        mAuth.sendPasswordResetEmail(email.trim())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        String errorMessage = "Failed to send reset email";

                        if (task.getException() != null) {
                            String error = task.getException().getMessage().toLowerCase();

                            if (error.contains("user-not-found")) {
                                errorMessage = "No account found with this email address";
                            } else if (error.contains("invalid-email")) {
                                errorMessage = "Invalid email address format";
                            } else if (error.contains("too-many-requests")) {
                                errorMessage = "Too many attempts. Please try again later";
                            } else if (error.contains("network")) {
                                errorMessage = "Network error. Please check your internet connection";
                            } else {
                                errorMessage = "Error: " + task.getException().getMessage();
                            }
                        }

                        callback.onFailure(errorMessage);
                    }
                });
    }

    public interface PasswordResetCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 6) return false;
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[a-z].*")) return false;
        if (!password.matches(".*\\d.*")) return false;
        return true;
    }
}