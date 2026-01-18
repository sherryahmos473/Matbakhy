package com.example.matbakhy.presentation.Auth.presenter;

import com.example.matbakhy.data.auth.AuthRepository;
import com.example.matbakhy.data.auth.callbacks.AuthCallback;
import com.example.matbakhy.model.User;
import com.example.matbakhy.presentation.Auth.view.RegisterView;

public class RegisterPresenterImpl extends BaseAuthPresenterImpl implements RegisterPresenter {
    private RegisterView registerView;

    public RegisterPresenterImpl(AuthRepository authRepository) {
        super(authRepository);
    }

    @Override
    public void attachView(RegisterView view) {
        super.attachView(view);
        this.registerView = (RegisterView) view;
    }

    @Override
    public void detachView() {
        super.detachView();
        this.registerView = null;
    }

    @Override
    public void onRegisterClicked() {
        if (registerView == null) return;

        String name = registerView.getName();
        String email = registerView.getEmail();
        String password = registerView.getPassword();
        String confirmPassword = registerView.getConfirmPassword();

        if (!validateInputs(name, email, password, confirmPassword)) {
            return;
        }

        performRegistration(name, email, password);
    }

    @Override
    public void onLoginClicked() {
        if (registerView != null) {
            registerView.navigateToLogin();
        }
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        if (registerView != null) {
            registerView.clearAllFields();
        }
    }

    // Private helper methods
    private boolean validateInputs(String name, String email, String password, String confirmPassword) {
        boolean isValid = true;

        if (registerView == null) return false;

        if (name.isEmpty()) {
            registerView.showNameError("Name is required");
            isValid = false;
        }

        // Use parent class validation for email and password
        if (!validateEmailAndPassword(email, password)) {
            isValid = false;
        }

        if (confirmPassword.isEmpty()) {
            registerView.showConfirmPasswordError("Please confirm your password");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            registerView.showConfirmPasswordError("Passwords do not match");
            isValid = false;
        }

        if (!isValid) {
            registerView.clearErrors();
        }

        return isValid;
    }

    private void performRegistration(String name, String email, String password) {
        if (registerView == null) return;

        registerView.showLoading("Creating account...");

        authRepository.register(email, password, name, new AuthCallback() {
            @Override
            public void onSuccess(User user) {
                if (registerView != null) {
                    registerView.hideLoading();
                    registerView.showToast("Registration successful!");
                    registerView.navigateToHome(email);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (registerView != null) {
                    registerView.hideLoading();
                    registerView.showToast(errorMessage);
                }
            }
        });
    }
}