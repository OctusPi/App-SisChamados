package br.com.dticampossales.appsischamados.validation.Login;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginPasswordValidator {
    private final TextInputLayout passwordLayout;
    private final TextInputEditText passwordText;

    public LoginPasswordValidator(TextInputLayout passwordLayout, TextInputEditText passwordText) {
        this.passwordLayout = passwordLayout;
        this.passwordText = passwordText;
    }

    public boolean validate() {
        if (Objects.requireNonNull(passwordText.getText()).toString().trim().isEmpty()) {
            passwordLayout.setError("Campo obrigatório!");
            passwordLayout.requestFocus();
            return false;
        } else {
            passwordLayout.setErrorEnabled(false);
            return true;
        }
    }
}
