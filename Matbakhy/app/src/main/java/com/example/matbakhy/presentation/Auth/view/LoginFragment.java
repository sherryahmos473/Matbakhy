package com.example.matbakhy.presentation.Auth.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.matbakhy.R;
import com.example.matbakhy.helper.ErrorSnackBar;
import com.example.matbakhy.helper.MySnackBar;
import com.example.matbakhy.presentation.Auth.presenter.AuthUtils;
import com.example.matbakhy.presentation.Auth.presenter.LoginPresenter;
import com.example.matbakhy.presentation.Auth.presenter.LoginPresenterImpl;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment implements LoginView {
    private static final String TAG = "LoginFragment";
    private static final int RC_SIGN_IN = 9001;

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnGoogle;
    private TextView txtRegister, txtForgotPassword, guestTextView;
    TextInputLayout passwordInputLayout, emailInputLayout;
    private ProgressDialog progressDialog;
    private LoginPresenter presenter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        presenter = new LoginPresenterImpl(getContext());
        presenter.attachView(this);

        initializeViews(view);
        setupListeners();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.checkIfUserLoggedIn();
        presenter.onViewCreated();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
        presenter.detachView();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onGoogleSignInResult(requestCode, resultCode, data);
    }
    @Override
    public void showLoading(String message) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showEmailError(String message) {
        emailInputLayout.setError(message);
    }

    @Override
    public void showPasswordError(String message) {
        passwordInputLayout.setError(message);
    }

    @Override
    public void clearErrors() {
        if (edtEmail != null) edtEmail.setError(null);
        if (edtPassword != null) edtPassword.setError(null);
    }

    @Override
    public void showToast(String message) {
        new MySnackBar(view, message);
    }

    @Override
    public void showError(String message) {
        new ErrorSnackBar(view, message);
    }

    @Override
    public void navigateToHome(String email) {
        AuthUtils.navigateToHome(requireActivity(), email);
    }

    @Override
    public void navigateToRegister() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment);
    }

    @Override
    public void navigateToForgotPassword() {
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
    }

    @Override
    public void setEmail(String email) {
        if (edtEmail != null) {
            edtEmail.setText(email);
        }
    }

    @Override
    public String getEmail() {
        return edtEmail != null ? edtEmail.getText().toString().trim() : "";
    }

    @Override
    public String getPassword() {
        return edtPassword != null ? edtPassword.getText().toString() : "";
    }

    @Override
    public void clearPasswordField() {
        if (edtPassword != null) {
            edtPassword.setText("");
        }
    }

    @Override
    public void focusPasswordField() {
        if (edtPassword != null) {
            edtPassword.requestFocus();
        }
    }

    @Override
    public void startGoogleSignInIntent(Intent intent, int rcSignIn) {
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void clearEmailField() {
        if (edtEmail != null) {
            edtEmail.setText("");
        }
    }


    private void initializeViews(View view) {
        edtEmail = view.findViewById(R.id.emailEditText);
        edtPassword = view.findViewById(R.id.passwordEditText);
        btnLogin = view.findViewById(R.id.signInButton);
        txtRegister = view.findViewById(R.id.signUpTextView);
        txtForgotPassword = view.findViewById(R.id.forgotPasswordTextView);
        guestTextView = view.findViewById(R.id.guestTextView);
        btnGoogle = view.findViewById(R.id.googleButton);
        emailInputLayout = view.findViewById(R.id.emailInputLayout);
        passwordInputLayout = view.findViewById(R.id.passwordInputLayout);


        progressDialog = AuthUtils.createProgressDialog(requireContext(), "Logging in...");
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> presenter.onLoginClicked());
        txtRegister.setOnClickListener(v -> presenter.onRegisterClicked());
        txtForgotPassword.setOnClickListener(v -> presenter.onForgotPasswordClicked());
        btnGoogle.setOnClickListener(v -> presenter.onGoogleSignInClickedWithRestore());
        guestTextView.setOnClickListener(v -> presenter.loginGuest());
    }
}