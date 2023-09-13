package br.com.dticampossales.appsischamados.validation.Atendimento;

import android.util.Patterns;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class AtendimentoTextInputValidator {
    public static boolean validate(TextInputLayout inputLayout, TextInputEditText editText) {
        if (Objects.requireNonNull(editText.getText()).toString().trim().isEmpty()) {
            inputLayout.setError("Campo obrigatório!");
            inputLayout.requestFocus();
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
            return true;
        }
    }
}
