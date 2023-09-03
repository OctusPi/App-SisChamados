package br.com.dticampossales.appsischamados;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import Utils.HttpClientUtil;
import Utils.JsonUtil;
import Utils.RawJsonReader;
import Utils.Security;
import br.com.dticampossales.appsischamados.adapters.Chamados.ChamadosListAdapter;
import br.com.dticampossales.appsischamados.controllers.ChamadosController;

public class ChamadosActivity extends AppCompatActivity {
    private ArrayList<JSONObject> dataSource;
    private ChamadosListAdapter chamadosListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        ChamadosController chamadosController = new ChamadosController(this);

        dataSource = chamadosController.getChamadosList();
        chamadosListAdapter = new ChamadosListAdapter(dataSource);

        Context context = getApplicationContext();


        String search  = "";
        String urlJSON = String.format(getResources().getString(R.string.api_chamados), hashLogin, search); //BUILD URL REQUEST

        //NOVA FORMA DE USAR O JSON DA API
        CompletableFuture<JSONObject> future = HttpClientUtil.asyncJson(urlJSON);
        future.thenAccept(json -> {

            // O JSON DEVE SER UTILIZADO DENTRO DA CLAUSULA THEN

        }).exceptionally(ex -> {
            // AQUI DEVE SER FEITO O TRATAMENTO DE ERROS CASO A REQUISICAO FALHE
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(), R.string.app_fail, Toast.LENGTH_SHORT).show();
            return null;
        });

        populateRecyclerView(findViewById(R.id.chamados_list), chamadosListAdapter);

        FloatingActionButton floatingActionButton = findViewById(R.id.chamados_floating);
        floatingActionButton.setOnClickListener(view -> toggleFilterLayout());

        Spinner sectorSpinner = findViewById(R.id.filter_sector);
        Spinner equipmentSpinner = findViewById(R.id.filter_equipment);
        Spinner statusSpinner = findViewById(R.id.filter_status);
    }

    private void populateRecyclerView(RecyclerView recyclerView, ChamadosListAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void toggleFilterLayout() {
        ConstraintLayout filterLayout = findViewById(R.id.filter_layout);
        if (filterLayout.getVisibility() != View.VISIBLE) {
            filterLayout.setVisibility(View.VISIBLE);
        } else {
            filterLayout.setVisibility(View.GONE);
        }
    }

    private void makeSpinnerItems(Spinner spinner, ArrayList<JSONObject> optionsList) {
        ArrayList<CharSequence> optionsString = new ArrayList<>();

        optionsString.add(getString(R.string.filter_default));

        for (JSONObject jsonObject : optionsList) {
            optionsString.add(JsonUtil.getJsonVal(jsonObject, getString(R.string.filter_name)));
        }

        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, optionsString);

        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(arrayAdapter);
    }
}