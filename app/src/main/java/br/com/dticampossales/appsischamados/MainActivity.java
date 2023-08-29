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
        setContentView(R.layout.activity_main);
        routeActivity();
    }

    @Override
    public void onResume(){
        super.onResume();
        routeActivity();
    }

    private void routeActivity(){

        Intent nextActivity = isAuthenticade()
                ? new Intent(this, ChamadosActivity.class)
                : new Intent(this, LoginActivity.class);

        startActivity(nextActivity);
        finish();
    }

    private boolean isAuthenticade(){

        /*
        ######################
        Temporary method, for security reasons here it will be checked if there is a hash saved in
        Shared Preferences and if this hash corresponds to that of a valid user
        ######################
        */

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE);
        return sharedPref.getBoolean(getString(R.string.is_authenticated), false);
    }
}