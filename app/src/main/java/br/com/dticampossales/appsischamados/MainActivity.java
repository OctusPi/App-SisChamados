package br.com.dticampossales.appsischamados;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import Utils.JsonRequest;
import Utils.JsonUtil;
import Utils.Security;

public class MainActivity extends AppCompatActivity {

    private TextView alertText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alertText = findViewById(R.id.alert_txt_main);

        isAuthenticate();
    }

    private void isAuthenticate() {

        Context context  = getApplicationContext();
        String hashLogin = Security.getHashLogin(context);
        String urlJSON   = String.format(getResources().getString(R.string.api_login), hashLogin);

        if(!hashLogin.equals("")){
            try {
                JSONObject jsonObject = JsonRequest.request(urlJSON);
                boolean isAuth = jsonObject.getInt("id") != 0;

                Intent intent = isAuth
                        ?  new Intent(context, ChamadosActivity.class)
                        :  new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();

            } catch (ExecutionException | InterruptedException | JSONException e) {
                alertText.setText(getString(R.string.app_fail));
                e.printStackTrace();
            }
        }else{
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}