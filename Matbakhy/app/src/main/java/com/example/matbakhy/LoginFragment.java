package com.example.matbakhy;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginFragment extends Fragment {
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txtRegister, txtForgotPassword;
    private ProgressDialog progressDialog;
    private FirebaseServices firebaseServices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        firebaseServices = new FirebaseServices(requireContext());

        edtEmail = view.findViewById(R.id.emailEditText);
        edtPassword = view.findViewById(R.id.passwordEditText);
        btnLogin = view.findViewById(R.id.signInButton);
        txtRegister = view.findViewById(R.id.signUpTextView);
        txtForgotPassword = view.findViewById(R.id.forgotPasswordTextView);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);

        btnLogin.setOnClickListener(v -> validateAndLogin());
        txtRegister.setOnClickListener(v -> navigateToRegister());
        txtForgotPassword.setOnClickListener(v -> navigateToForgotPassword());

        return view;
    }

    private void validateAndLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();


        if (email.isEmpty()) {
            showError(edtEmail, "Email is required");
            return;
        }

        if (!FirebaseServices.isValidEmail(email)) {
            showError(edtEmail, "Please enter a valid email");
            return;
        }

        if (password.isEmpty()) {
            showError(edtPassword, "Password is required");
            return;
        }

        if (!FirebaseServices.isValidPassword(password)) {
            showError(edtPassword, "Password must be at least 6 characters with uppercase, lowercase, and number");
            return;
        }

        clearErrors();
        progressDialog.show();
        firebaseServices.login(email, password, new FirebaseServices.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                progressDialog.dismiss();
                new MyToast(getContext(), "Login successful!");
                // navigateToHome(); // Navigate to home screen
            }

            @Override
            public void onFailure(String errorMessage) {
                progressDialog.dismiss();
                Log.d("login", "onFailure: " + errorMessage);
                new MyToast(getContext(), errorMessage);
            }
        });
    }

    private void showError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
    }

    private void clearErrors() {
        edtEmail.setError(null);
        edtPassword.setError(null);
    }
    private void navigateToRegister() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment);
    }

    private void navigateToForgotPassword() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
    }
    private void setupEnterKeyListener() {
        edtPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                validateAndLogin();
                return true;
            }
            return false;
        });
    }
}