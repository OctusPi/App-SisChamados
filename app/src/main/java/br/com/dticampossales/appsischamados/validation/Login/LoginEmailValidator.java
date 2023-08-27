package br.com.dticampossales.appsischamados.validation.Login;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.util.Patterns;

import java.util.Objects;

public class LoginEmailValidator {
    private final TextInputLayout emailLayout;
    private final TextInputEditText emailText;

    public LoginEmailValidator(TextInputLayout emailLayout, TextInputEditText emailText) {
        this.emailLayout = emailLayout;
        this.emailText = emailText;
    }
    public boolean validate() {
        if (Objects.requireNonNull(emailText.getText()).toString().trim().isEmpty()) {
            emailLayout.setError("Campo obrigatório!");
            emailLayout.requestFocus();
            return false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(emailText.getText()).matches()) {
                emailLayout.setError("Email inválido! ex.: abc@def.com");
                emailLayout.requestFocus();
                return false;
            } else {
                emailLayout.setErrorEnabled(false);
                return true;
            }
        }
    }
}
