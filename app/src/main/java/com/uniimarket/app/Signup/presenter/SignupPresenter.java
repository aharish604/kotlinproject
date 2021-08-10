package com.uniimarket.app.Signup.presenter;

import org.jetbrains.annotations.NotNull;

public class SignupPresenter {

    public SignupPresenter(@NotNull String firstName, @NotNull String lastName, @NotNull String uniiMail, @NotNull String password) {

    }

    public interface SignupListener {
        void signUpResponse(String status, String message);
    }
}
