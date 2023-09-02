package br.com.dticampossales.appsischamados;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import Utils.HttpClientUtil;
import Utils.JsonUtil;
import Utils.RWFiles;
import Utils.Security;
import br.com.dticampossales.appsischamados.validation.Login.LoginEmailValidator;
import br.com.dticampossales.appsischamados.validation.Login.LoginPasswordValidator;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailText;
    private TextInputEditText passwordText;
    private TextView alertText;
    private ProgressBar progressBar;
    private  Button btnLogin;
    private LoginPasswordValidator passwordValidator;
    private LoginEmailValidator emailValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputLayout emailLayout = findViewById(R.id.form_email);
        TextInputLayout passwordLayout = findViewById(R.id.form_password);

        emailText      = findViewById(R.id.email);
        passwordText   = findViewById(R.id.password);
        btnLogin       = findViewById(R.id.form_submit);
        alertText      = findViewById(R.id.alert_text);
        progressBar    = findViewById(R.id.progress_bar);

        passwordValidator = new LoginPasswordValidator(passwordLayout, passwordText);
        emailValidator    = new LoginEmailValidator(emailLayout, emailText);

        emailText.setText("octuspi@gmail.com");
        passwordText.setText("Kira@7616");

        loginAPI();
    }

    private void loginAPI(){
        btnLogin.setOnClickListener(view -> {

            if(emailValidator.validate() && passwordValidator.validate()){
                progressBar.setVisibility(View.VISIBLE);
                alertText.setText("");

                Context context  = getApplicationContext();
                String email     = Objects.requireNonNull(emailText.getText()).toString();
                String passwd    = Objects.requireNonNull(passwordText.getText()).toString();

                try {
                    String hashLogin = Security.hashLogin(email, passwd);
                    String urlJSON   = String.format(getResources().getString(R.string.api_login), hashLogin);
                    CompletableFuture<JSONObject> future = HttpClientUtil.asyncJson(urlJSON);
                    future.thenAccept(json -> {
                        try {
                            if(json.getInt("id") != 0){
                                //save hash in shared preferences to auto login in next time
                                SharedPreferences sharedPref    = context.getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(getString(R.string.is_hash_login), hashLogin).apply();

                                // call acitivity list chamados
                                Intent chamadosActivity = new Intent(context, ChamadosActivity.class);
                                startActivity(chamadosActivity);
                                finish();
                            }else{
                                progressBar.setVisibility(View.GONE);
                                alertText.setText(getResources().getString(R.string.app_fail_login));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }).exceptionally(ex -> {
                        progressBar.setVisibility(View.GONE);
                        alertText.setText(getResources().getString(R.string.app_fail));
                        ex.printStackTrace();
                        return null;
                    });

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    alertText.setText(getResources().getString(R.string.app_fail));
                }
            }
        });
    }
}