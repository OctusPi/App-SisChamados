package br.com.dticampossales.appsischamados;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import Utils.JsonUtil;
import Utils.Security;
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
            boolean isEmailValid    = emailValidator.validate();
            boolean isPasswordValid = passwordValidator.validate();

            tempLoginTest();

            /*########################
             * Call a true method login, wait for finalize api backend in server
             *
                if(isEmailValid && isPasswordValid) {
                    try {
                        loginApi(Objects.requireNonNull(emailText.getText()).toString(), Objects.requireNonNull(passwordText.getText()).toString());
                    } catch (NoSuchAlgorithmException e) {
                        Toast.makeText(this, R.string.app_fail, Toast.LENGTH_SHORT).show();
                    }
                }
             *########################*/

        });
    }

    private void tempLoginTest(){
        Context context   = getApplicationContext();
        SharedPreferences sharedPref    = context.getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.is_authenticated), true).apply();


        Intent chamadosActivity = new Intent(context, ChamadosActivity.class);
        startActivity(chamadosActivity);
        finish();
    }

    @SuppressLint("StringFormatInvalid")
    private void loginApi(String email, String password) throws NoSuchAlgorithmException {
        Context context   = getApplicationContext();
        String hashLogin  = Security.hashLogin(email, password);
        String urlRequest = String.format(getResources().getString(R.string.api_login), hashLogin);

        try {
            JSONObject jsonObject = JsonUtil.requestJson(context, urlRequest);
            if(jsonObject.getInt("id") != 0){

                Toast.makeText(context, R.string.app_success, Toast.LENGTH_LONG).show();

                //save hash in shared preferences to auto login in next time
                SharedPreferences sharedPref    = context.getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.is_hash_login), hashLogin).apply();

                // call acitivity list chamados
                Intent chamadosActivity = new Intent(context, ChamadosActivity.class);
                startActivity(chamadosActivity);
                finish();

            }else{
                Toast.makeText(this, R.string.app_fail_login, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException | JSONException e) {
            Toast.makeText(this, R.string.app_fail, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}