package com.example.matbakhy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegisterFragment extends Fragment {
    private EditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister, btnGoogle;
    private TextView txtLogin;
    private ProgressDialog progressDialog;
    private FirebaseServices firebaseServices;
    private View view;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "RegisterFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);

        firebaseServices = FirebaseServices.getInstance(requireContext());

        initializeViews();
        setupListeners();

        return view;
    }

    private void initializeViews() {
        edtName = view.findViewById(R.id.nameEditText);
        edtEmail = view.findViewById(R.id.emailEditText);
        edtPassword = view.findViewById(R.id.passwordEditText);
        edtConfirmPassword = view.findViewById(R.id.confirmPasswordEditText);
        btnRegister = view.findViewById(R.id.signUpButton);
        btnGoogle = view.findViewById(R.id.googleButton);
        txtLogin = view.findViewById(R.id.signInTextView);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> validateAndRegister());
        btnGoogle.setOnClickListener(v -> signUpWithGoogle());
        txtLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void validateAndRegister() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        if (!validateEmailPasswordInputs(name, email, password, confirmPassword)) {
            return;
        }

        clearErrors();
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        firebaseServices.register(email, password, name, new FirebaseServices.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                progressDialog.dismiss();
                showToast("Registration successful!");
                navigateToHomeActivity(user.getEmail());
            }

            @Override
            public void onFailure(String errorMessage) {
                progressDialog.dismiss();
                Log.e(TAG, "Registration failed: " + errorMessage);
                showToast(errorMessage);
            }
        });
    }

    private boolean validateEmailPasswordInputs(String name, String email, String password, String confirmPassword) {
        boolean isValid = true;

        if (name.isEmpty()) {
            showError(edtName, "Name is required");
            isValid = false;
        }

        if (email.isEmpty()) {
            showError(edtEmail, "Email is required");
            isValid = false;
        } else if (!FirebaseServices.isValidEmail(email)) {
            showError(edtEmail, "Please enter a valid email");
            isValid = false;
        }

        if (password.isEmpty()) {
            showError(edtPassword, "Password is required");
            isValid = false;
        } else if (password.length() > 6) {
            showError(edtPassword, "Password must be at least 6 characters");
            isValid = false;
        }

        if (confirmPassword.isEmpty()) {
            showError(edtConfirmPassword, "Please confirm your password");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            showError(edtConfirmPassword, "Passwords do not match");
            isValid = false;
        }

        return isValid;
    }

    private void signUpWithGoogle() {
        Log.d(TAG, "Starting Google Sign-Up...");

        if (!firebaseServices.isNetworkAvailable()) {
            showToast("No internet connection. Please check your network.");
            return;
        }

        Intent signInIntent = firebaseServices.getGoogleSignInIntent();
        if (signInIntent != null) {
            progressDialog.setMessage("Signing in with Google...");
            progressDialog.show();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            showToast("Google Sign-In not available. Please try again.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            progressDialog.dismiss();

            if (data != null) {
                handleGoogleSignInResult(data);
            } else {
                Log.e(TAG, "Google Sign-In returned null data");
                showToast("Google Sign-In failed. Please try again.");
            }
        }
    }

    private void handleGoogleSignInResult(Intent data) {
        firebaseServices.handleGoogleSignInResult(data, new FirebaseServices.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                Log.d(TAG, "Google Sign-Up successful for: " + user.getEmail());
                showToast("Welcome " + (user.getName() != null ? user.getName() : "User") + "!");
                navigateToHomeActivity(user.getEmail());
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Google Sign-Up failed: " + errorMessage);
                showToast(errorMessage);
            }
        });
    }

    private void showError(EditText editText, String message) {
        if (editText != null) {
            editText.setError(message);
            editText.requestFocus();
        }
    }

    private void clearErrors() {
        if (edtName != null) edtName.setError(null);
        if (edtEmail != null) edtEmail.setError(null);
        if (edtPassword != null) edtPassword.setError(null);
        if (edtConfirmPassword != null) edtConfirmPassword.setError(null);
    }

    private void navigateToHomeActivity(String email) {
        try {
            if (getActivity() == null || getActivity().isFinishing()) {
                Log.e(TAG, "Activity is null or finishing");
                return;
            }

            Log.d(TAG, "Navigating to HomeActivity with email: " + email);

            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.putExtra("userEmail", email);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Add slight delay to ensure UI thread is ready
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }, 300);

        } catch (Exception e) {
            Log.e(TAG, "Navigation failed: " + e.getMessage(), e);
            showToast("Navigation error. Please restart app.");
        }
    }

    private void navigateToLogin() {
        try {
            // Using getParentFragmentManager() to navigate
            LoginFragment loginFragment = new LoginFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, loginFragment)
                    .addToBackStack("register")
                    .commit();
        } catch (Exception e) {
            Log.e(TAG, "Navigation to login failed: " + e.getMessage());
            showToast("Cannot open login screen");
        }
    }

    private void showToast(String message) {
        if (getContext() != null) {
            new MyToast(getContext(), message);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}