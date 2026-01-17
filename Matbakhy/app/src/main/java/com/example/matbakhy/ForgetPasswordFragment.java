package com.example.matbakhy;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

public class ForgetPasswordFragment extends Fragment {
    private EditText edtEmail;
    private TextInputLayout inputLayoutEmail;
    private Button btnSendReset;
    private TextView txtRememberPassword, txtResendEmail, txtEmailSentTo;
    private LinearLayout layoutSuccess;
    private ProgressDialog progressDialog;
    private FirebaseServices firebaseServices;
    private String userEmail = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);

        firebaseServices = FirebaseServices.getInstance(requireContext());

        edtEmail = view.findViewById(R.id.emailEditText);
        inputLayoutEmail = view.findViewById(R.id.emailInputLayout);
        btnSendReset = view.findViewById(R.id.btnSendReset);
        txtRememberPassword = view.findViewById(R.id.txtRememberPassword);
        txtResendEmail = view.findViewById(R.id.txtResendEmail);
        txtEmailSentTo = view.findViewById(R.id.txtEmailSentTo);
        layoutSuccess = view.findViewById(R.id.layoutSuccess);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Sending reset email...");
        progressDialog.setCancelable(false);

        btnSendReset.setOnClickListener(v -> sendResetEmail());
        txtRememberPassword.setOnClickListener(v -> navigateToLogin());
        txtResendEmail.setOnClickListener(v -> resendResetEmail());

        view.findViewById(R.id.btnBack).setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        return view;
    }

    private void sendResetEmail() {
        userEmail = edtEmail.getText().toString().trim();

        if (userEmail.isEmpty()) {
            inputLayoutEmail.setError("Email is required");
            return;
        }

        if (!FirebaseServices.isValidEmail(userEmail)) {
            inputLayoutEmail.setError("Please enter a valid email");
            return;
        }

        inputLayoutEmail.setError(null);

        progressDialog.show();

        // Send reset email
        firebaseServices.sendPasswordResetEmail(userEmail, new FirebaseServices.SimpleCallback() {
            @Override
            public void onSuccess(String email) {
                progressDialog.dismiss();
                showSuccessScreen();
            }

            @Override
            public void onFailure(String errorMessage) {
                progressDialog.dismiss();
                new MyToast(getContext(), errorMessage);
            }
        });
    }

    private void resendResetEmail() {
        if (userEmail.isEmpty()) {
            new MyToast(getContext(), "Email not found");
            return;
        }

        progressDialog.show();
        firebaseServices.sendPasswordResetEmail(userEmail, new FirebaseServices.SimpleCallback() {
            @Override
            public void onSuccess(String email) {
                progressDialog.dismiss();
                new MyToast(getContext(), "Reset email resent successfully");
            }

            @Override
            public void onFailure(String errorMessage) {
                progressDialog.dismiss();
                new MyToast(getContext(), "Failed to resend: " + errorMessage);
            }
        });
    }

    private void showSuccessScreen() {
        // Hide input form
        edtEmail.setVisibility(View.GONE);
        inputLayoutEmail.setVisibility(View.GONE);
        btnSendReset.setVisibility(View.GONE);
        txtRememberPassword.setVisibility(View.GONE);

        // Show success message
        layoutSuccess.setVisibility(View.VISIBLE);
        txtEmailSentTo.setText(userEmail);
    }
    private void navigateToLogin() {
        Navigation.findNavController(requireView()).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
    }
}