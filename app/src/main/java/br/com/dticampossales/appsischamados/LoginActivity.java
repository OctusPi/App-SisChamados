package br.com.dticampossales.appsischamados;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import Utils.JsonUtil;
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

                String email     = Objects.requireNonNull(emailText.getText()).toString();
                String passwd    = Objects.requireNonNull(passwordText.getText()).toString();

                try {
                    String hashLogin = Security.hashLogin(email, passwd);
                    String urlJSON   = String.format(getResources().getString(R.string.api_login), hashLogin);

                    JSONObject jsonObject = JsonUtil.requestJson(urlJSON);
                    boolean isAuth = jsonObject.getInt("id") != 0;
                    execLogin(isAuth, hashLogin);

                } catch (NoSuchAlgorithmException | JSONException | ExecutionException | InterruptedException e) {
                    progressBar.setVisibility(View.GONE);
                    alertText.setText(getString(R.string.app_fail));
                    e.printStackTrace();
                }
            }
        });
    }

    private void execLogin(boolean isAuth, String hashLogin){
        if(isAuth){
            //save hash in shared preferences to auto login in next time
            Security.setHashLogin(getApplicationContext(), hashLogin);

            // call acitivity list chamados
            Intent chamadosActivity = new Intent(getApplicationContext(), ChamadosActivity.class);
            startActivity(chamadosActivity);
            finish();
        }else{
            progressBar.setVisibility(View.GONE);
            alertText.setText(getString(R.string.app_fail_login));
        }
    }
}