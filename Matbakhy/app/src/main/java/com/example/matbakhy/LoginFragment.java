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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class LoginFragment extends Fragment {
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txtRegister, txtForgotPassword;
    private ProgressDialog progressDialog;
    private FirebaseServices firebaseServices;
    private View rootView;

    private static final String TAG = "LoginFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        rootView = view;

        firebaseServices = new FirebaseServices(requireContext());

        initializeViews(view);
        setupListeners();
        setupEnterKeyListener();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (firebaseServices.isUserLoggedIn()) {
            navigateToHomeActivity(firebaseServices.getCurrentFirebaseUser().getEmail());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (edtEmail != null) edtEmail.setText("");
        if (edtPassword != null) edtPassword.setText("");
        clearErrors();
    }

    private void initializeViews(View view) {
        edtEmail = view.findViewById(R.id.emailEditText);
        edtPassword = view.findViewById(R.id.passwordEditText);
        btnLogin = view.findViewById(R.id.signInButton);
        txtRegister = view.findViewById(R.id.signUpTextView);
        txtForgotPassword = view.findViewById(R.id.forgotPasswordTextView);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> validateAndLogin());
        txtRegister.setOnClickListener(v -> navigateToRegister());
        txtForgotPassword.setOnClickListener(v -> navigateToForgotPassword());
    }

    private void validateAndLogin() {
        if (!validateInputs()) {
            return;
        }

        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();

        clearErrors();
        progressDialog.show();

        Log.d(TAG, "Attempting login for: " + email);

        final String userEmail = email;

        firebaseServices.login(email, password, new FirebaseServices.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                progressDialog.dismiss();
                Log.d(TAG, "Login successful for: " + userEmail);
                showToast("Login successful!");
                navigateToHomeActivity(userEmail);
            }

            @Override
            public void onFailure(String errorMessage) {
                progressDialog.dismiss();
                Log.e(TAG, "Login failed: " + errorMessage);
                showToast(errorMessage);

                // Clear password field on failure
                if (edtPassword != null) {
                    edtPassword.setText("");
                    edtPassword.requestFocus();
                }
            }
        });
    }

    private boolean validateInputs() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();

        boolean isValid = true;

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
        } else if (!FirebaseServices.isValidPassword(password)) {
            showError(edtPassword, "Password must be at least 6 characters");
            isValid = false;
        }

        return isValid;
    }

    private void navigateToHomeActivity(String email) {
        try {
            Log.d(TAG, "Navigating to HomeActivity with email: " + email);

            if (getActivity() == null || getActivity().isFinishing()) {
                Log.e(TAG, "Activity is null or finishing");
                return;
            }

            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.putExtra("userEmail", email);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

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

    private void navigateToRegister() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment);

    }

    private void navigateToForgotPassword() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
    }

    private void showError(EditText editText, String message) {
        if (editText != null) {
            editText.setError(message);
            editText.requestFocus();
        }
    }

    private void clearErrors() {
        if (edtEmail != null) edtEmail.setError(null);
        if (edtPassword != null) edtPassword.setError(null);
    }

    private void setupEnterKeyListener() {
        if (edtPassword != null) {
            edtPassword.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                    validateAndLogin();
                    return true;
                }
                return false;
            });
        }

        if (edtEmail != null) {
            edtEmail.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_NEXT) {
                    if (edtPassword != null) {
                        edtPassword.requestFocus();
                        return true;
                    }
                }
                return false;
            });
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