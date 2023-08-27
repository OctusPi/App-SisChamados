package br.com.dticampossales.appsischamados;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_key), MODE_PRIVATE);

        Intent nextActivity;

        if (sharedPref.getBoolean(getString(R.string.is_authenticated), false))
            nextActivity = new Intent(this, ChamadosActivity.class);
        else
            nextActivity = new Intent(this, LoginActivity.class);

        startActivity(nextActivity);
        finish();
    }
}