package com.example.matbakhy.presentation.Auth.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.matbakhy.R;
import com.example.matbakhy.helper.ErrorSnackBar;
import com.example.matbakhy.helper.MySnackBar;
import com.example.matbakhy.presentation.Auth.presenter.AuthUtils;
import com.example.matbakhy.presentation.Auth.presenter.RegisterPresenter;
import com.example.matbakhy.presentation.Auth.presenter.RegisterPresenterImpl;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class RegisterFragment extends Fragment implements RegisterView {
    private static final String TAG = "RegisterFragment";

    private TextInputEditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister, btnGoogle;
    private TextView txtLogin, guestTextView;
    private ProgressDialog progressDialog;
    private RegisterPresenter presenter;
    TextInputLayout nameInputLayout, emailInputLayout, passwordInputLayout, confirmPasswordInputLayout;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);

        presenter = new RegisterPresenterImpl(getContext());
        presenter.attachView(this);

        initializeViews(view);
        setupListeners();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewCreated();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onGoogleSignInResult(requestCode, resultCode, data);
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
    public void showLoading(String message) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage(message);
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
    public void showNameError(String message) {
        Log.d(TAG, "showNameError: ");
        nameInputLayout.setError(message);
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
    public void showConfirmPasswordError(String message) {
        confirmPasswordInputLayout.setError(message);
    }

    @Override
    public void clearErrors() {
        if (edtName != null) edtName.setError(null);
        if (edtEmail != null) edtEmail.setError(null);
        if (edtPassword != null) edtPassword.setError(null);
        if (edtConfirmPassword != null) edtConfirmPassword.setError(null);
    }

    @Override
    public void showToast(String message) {
        new MySnackBar(view, message);
    }

    @Override
    public void showError(String message) {
        new ErrorSnackBar(view,message);
    }

    @Override
    public void navigateToHome(String email) {
        AuthUtils.navigateToHome(requireActivity(), email);
    }

    @Override
    public void navigateToLogin() {
        Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);
    }

    @Override
    public void setEmail(String email) {
        if (edtEmail != null) {
            edtEmail.setText(email);
        }
    }

    @Override
    public void setName(String name) {
        if (edtName != null) {
            edtName.setText(name);
        }
    }

    @Override
    public String getName() {
        Log.d(TAG, "getName: "+ edtName.getText().toString().trim());
        return edtName != null ? edtName.getText().toString().trim() : "";
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
    public String getConfirmPassword() {
        return edtConfirmPassword != null ? edtConfirmPassword.getText().toString() : "";
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
        startActivityForResult(intent, 9001);
    }

    @Override
    public void clearEmailField() {
        if (edtEmail != null) {
            edtEmail.setText("");
        }
    }

    @Override
    public void onGuestStatus(boolean isGuest) {
        if(isGuest){
            navigateToHome("none");
        }
    }

    @Override
    public void clearAllFields() {
        if (edtName != null) edtName.setText("");
        if (edtEmail != null) edtEmail.setText("");
        if (edtPassword != null) edtPassword.setText("");
        if (edtConfirmPassword != null) edtConfirmPassword.setText("");
    }


    private void initializeViews(View view) {
        edtName = view.findViewById(R.id.nameEditText);
        edtEmail = view.findViewById(R.id.emailEditText);
        edtPassword = view.findViewById(R.id.passwordEditText);
        edtConfirmPassword = view.findViewById(R.id.confirmPasswordEditText);
        btnRegister = view.findViewById(R.id.signUpButton);
        btnGoogle = view.findViewById(R.id.googleButton);
        txtLogin = view.findViewById(R.id.signInTextView);
        guestTextView = view.findViewById(R.id.guestTextView);
        nameInputLayout = view.findViewById(R.id.nameInputLayout);
        emailInputLayout = view.findViewById(R.id.emailInputLayout);
        passwordInputLayout = view.findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout = view.findViewById(R.id.confirmPasswordInputLayout);



        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> presenter.onRegisterClicked());
        btnGoogle.setOnClickListener(v -> presenter.onGoogleSignInClickedWithRestore());
        txtLogin.setOnClickListener(v -> presenter.onLoginClicked());
        guestTextView.setOnClickListener(v -> presenter.loginGuest());

    }
}