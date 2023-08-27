package br.com.dticampossales.appsischamados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import br.com.dticampossales.appsischamados.validation.Login.LoginEmailValidator;
import br.com.dticampossales.appsischamados.validation.Login.LoginPasswordValidator;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputLayout emailLayout = findViewById(R.id.form_email);
        TextInputEditText emailText = findViewById(R.id.email);
        LoginEmailValidator emailValidator = new LoginEmailValidator(emailLayout, emailText);

        TextInputLayout passwordLayout = findViewById(R.id.form_password);
        TextInputEditText passwordText = findViewById(R.id.password);
        LoginPasswordValidator passwordValidator = new LoginPasswordValidator(passwordLayout, passwordText);

        Button btn = findViewById(R.id.form_submit);

        btn.setOnClickListener(view -> {
            boolean isEmailValid = emailValidator.validate();
            boolean isPasswordValid = passwordValidator.validate();

            Context context = getApplicationContext();
            if (isEmailValid && isPasswordValid) {
                SharedPreferences sharedPref = context.getSharedPreferences(
                        getString(R.string.preference_key), MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.is_authenticated), true).apply();

                Toast.makeText(this,"Sucesso!", Toast.LENGTH_LONG).show();

                Intent chamadosActivity = new Intent(this, ChamadosActivity.class);
                startActivity(chamadosActivity);
                finish();
            }
        });
    }
}