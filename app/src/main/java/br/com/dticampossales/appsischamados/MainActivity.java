package br.com.dticampossales.appsischamados;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;

import Utils.HttpClientUtil;
import Utils.JsonUtil;

public class MainActivity extends AppCompatActivity {

    private boolean isAuth = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        veirifyIsAuth();

    }

    private void veirifyIsAuth() {

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE);
        String hashLogin = sharedPref.getString(getString(R.string.is_hash_login), "");
        String urlJSON   = String.format(getResources().getString(R.string.api_login), hashLogin);

        CompletableFuture<JSONObject> future = HttpClientUtil.asyncJson(urlJSON);
        future.thenAccept(json -> {
            try {
                if(json.getInt("id") != 0){
                    Intent act = new Intent(this, ChamadosActivity.class);
                    startActivity(act);
                    finish();
                }else{
                    Intent act = new Intent(this, LoginActivity.class);
                    startActivity(act);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });

    }
}