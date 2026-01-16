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

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class RegisterFragment extends Fragment {
    private EditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private TextView txtLogin, txtForgotPassword;
    private ProgressDialog progressDialog;
    private FirebaseServices firebaseServices;
    private  View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);

        firebaseServices = new FirebaseServices(requireContext());

        edtName = view.findViewById(R.id.nameEditText);
        edtEmail = view.findViewById(R.id.emailEditText);
        edtPassword = view.findViewById(R.id.passwordEditText);
        edtConfirmPassword = view.findViewById(R.id.confirmPasswordEditText);
        btnRegister = view.findViewById(R.id.signUpButton);
        txtLogin = view.findViewById(R.id.signInTextView);
        txtForgotPassword = view.findViewById(R.id.forgotPasswordTextView);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);

        btnRegister.setOnClickListener(v -> validateAndRegister());
        txtLogin.setOnClickListener(v -> navigateToLogin());

        return view;
    }

    private void validateAndRegister() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        if (name.isEmpty()) {
            showError(edtName, "Name is required");
            return;
        }

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

        if (confirmPassword.isEmpty()) {
            showError(edtConfirmPassword, "Please confirm your password");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError(edtConfirmPassword, "Passwords do not match");
            return;
        }
        clearErrors();
        progressDialog.show();
        firebaseServices.register(email, password, name, new FirebaseServices.RegistrationCallback() {
            @Override
            public void onSuccess(User user) {
                progressDialog.dismiss();
                new MyToast(getContext(), "Registration successful!");
                navigateToHomeActivity(email);
            }

            @Override
            public void onFailure(String errorMessage) {
                progressDialog.dismiss();
                Log.d("Register", "onFailure: " + errorMessage);
                new MyToast(getContext(), errorMessage);
            }
        });
    }

    private void showError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
    }

    private void clearErrors() {
        edtName.setError(null);
        edtEmail.setError(null);
        edtPassword.setError(null);
        edtConfirmPassword.setError(null);
    }
    private void navigateToHomeActivity(String email) {
        try {

            if (getActivity() == null || getActivity().isFinishing()) {
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
            new MyToast(getContext(),"Navigation error. Please restart app.");
        }
    }

    private void navigateToLogin() {
        Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);
    }
}