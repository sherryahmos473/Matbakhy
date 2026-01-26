package com.example.matbakhy.presentation.Auth.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.matbakhy.R;
import com.example.matbakhy.helper.ErrorSnackBar;
import com.example.matbakhy.helper.MySnackBar;
import com.example.matbakhy.presentation.Auth.presenter.ForgotPasswordPresenter;
import com.example.matbakhy.presentation.Auth.presenter.ForgotPasswordPresenterImpl;
import com.google.android.material.textfield.TextInputLayout;

public class ForgetPasswordFragment extends Fragment implements ForgotPasswordView {
    private EditText edtEmail;
    private TextInputLayout inputLayoutEmail;
    private Button btnSendReset, signin;
    private ImageView btnBack;
    private TextView txtRememberPassword, txtResendEmail, txtEmailSentTo;
    private LinearLayout layoutSuccess;
    private ProgressDialog progressDialog;
    private ForgotPasswordPresenter presenter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_forget_password, container, false);

        presenter = new ForgotPasswordPresenterImpl(getContext());
        presenter.attachView(this);

        initializeViews(view);
        setupListeners();

        return view;
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
    public void showLoading() {
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
        if (inputLayoutEmail != null) {
            inputLayoutEmail.setError(message);
        }
    }

    @Override
    public void clearEmailError() {
        if (inputLayoutEmail != null) {
            inputLayoutEmail.setError(null);
        }
    }

    @Override
    public void showToast(String message) {
        if (getContext() != null) {
            new MySnackBar(view, message);
        }
    }

    @Override
    public void showError(String message) {
        new ErrorSnackBar(view, message);
    }

    @Override
    public void showSuccessScreen(String email) {
        if (getView() == null) return;

        hideInputForm();

        if (layoutSuccess != null) {
            layoutSuccess.setVisibility(View.VISIBLE);
        }

        if (txtEmailSentTo != null) {
            txtEmailSentTo.setText(email);
        }
    }

    @Override
    public void hideInputForm() {
        if (edtEmail != null) edtEmail.setVisibility(View.GONE);
        if (inputLayoutEmail != null) inputLayoutEmail.setVisibility(View.GONE);
        if (btnSendReset != null) btnSendReset.setVisibility(View.GONE);
        if (txtRememberPassword != null) txtRememberPassword.setVisibility(View.GONE);
        if (btnBack != null) btnBack.setVisibility(View.GONE);
    }

    @Override
    public void showInputForm() {
        if (edtEmail != null) edtEmail.setVisibility(View.VISIBLE);
        if (inputLayoutEmail != null) inputLayoutEmail.setVisibility(View.VISIBLE);
        if (btnSendReset != null) btnSendReset.setVisibility(View.VISIBLE);
        if (txtRememberPassword != null) txtRememberPassword.setVisibility(View.VISIBLE);
        if (btnBack != null) btnBack.setVisibility(View.VISIBLE);
        if (layoutSuccess != null) layoutSuccess.setVisibility(View.GONE);
    }

    @Override
    public void navigateToLogin() {
        if (getView() != null) {
            Navigation.findNavController(getView()).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
        }
    }

    @Override
    public void navigateBack() {
        if (getView() != null) {
            Navigation.findNavController(getView()).popBackStack();
        }
    }

    @Override
    public String getEmail() {
        return edtEmail != null ? edtEmail.getText().toString().trim() : "";
    }

    @Override
    public void setEmail(String email) {
        if (edtEmail != null) {
            edtEmail.setText(email);
        }
    }

    private void initializeViews(View view) {
        edtEmail = view.findViewById(R.id.emailEditText);
        inputLayoutEmail = view.findViewById(R.id.emailInputLayout);
        btnSendReset = view.findViewById(R.id.btnSendReset);
        btnBack = view.findViewById(R.id.btnBack);
        txtRememberPassword = view.findViewById(R.id.signInTextView);
        signin = view.findViewById(R.id.signInButton);
        txtResendEmail = view.findViewById(R.id.txtResendEmail);
        txtEmailSentTo = view.findViewById(R.id.txtEmailSentTo);
        layoutSuccess = view.findViewById(R.id.layoutSuccess);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Sending reset email...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
    }

    private void setupListeners() {
        if (btnSendReset != null) {
            btnSendReset.setOnClickListener(v -> presenter.onSendResetClicked());
        }

        if (txtRememberPassword != null) {
            txtRememberPassword.setOnClickListener(v -> presenter.onRememberPasswordClicked());
        }
        if (signin != null) {
            signin.setOnClickListener(v -> presenter.onRememberPasswordClicked());
        }

        if (txtResendEmail != null) {
            txtResendEmail.setOnClickListener(v -> presenter.onResendEmailClicked());
        }

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> presenter.onBackClicked());
        }
    }
}