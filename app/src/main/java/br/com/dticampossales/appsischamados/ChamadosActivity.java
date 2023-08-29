package br.com.dticampossales.appsischamados;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

import Utils.JsonUtil;
import br.com.dticampossales.appsischamados.adapters.ChamadosAdapter;

public class ChamadosActivity extends AppCompatActivity {

    private ChamadosAdapter chamadosAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        try {
            Context context = getApplicationContext();
            InputStream inputStream = context.getResources().openRawResource(R.raw.data_test);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray jsonArray = new JSONArray(jsonString.toString());
            ArrayList<JSONObject> listJSON = JsonUtil.jsonList(jsonArray);

            /*
             * IMPLEMENTA O ADAPTER USANDO ESSE **** listJSON **** PARA REALIZAR OS TESTES
             */

            DataBindingUtil.setContentView(this, R.layout.activity_chamados);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}