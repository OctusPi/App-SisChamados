package br.com.dticampossales.appsischamados;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn = findViewById(R.id.form_submit);

        btn.setOnClickListener(view -> {
            Toast.makeText(this, "ASDASDASD", Toast.LENGTH_LONG).show();
        });

        String a = String.valueOf(R.string.login);
    }
}