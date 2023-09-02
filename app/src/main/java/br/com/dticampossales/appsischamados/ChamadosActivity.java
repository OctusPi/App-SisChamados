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

public class ChamadosActivity extends AppCompatActivity {
    private ArrayList<JSONObject> dataSource;
    private ChamadosListAdapter chamadosListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        /*
         * EM VIRTUDE DA NECESSIDADE DE REQUISICOES ASYNCRONAS INFELIZMENTE TIVE QUE MODIFICAR A FORMA
         * COMO A CLASS RESPONSÁVEL POR REQUERER O JSON NA API, IMPLEMENTE O CÓDIGO DENTRO DSA CLASS HTTPCLIENTUTIL
         * DESSA FORMA É NECESSÁRIO TRABALHAR COM PROMISSES ASSIM COMO FAZ O JAVA SCRIPT SEGUE UM EXEMPLO REFATORA O CODIGO DA ACTIVITY
         * LISTAR CHAMADOS PARA SE ADEQUAR
         */

        /*
        EXEMPLO DE IMPLEMENTACAO
        */
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE);
        String hashLogin = sharedPref.getString(getString(R.string.is_hash_login), "");

        String search  = ""; //DADOS VINDOS DOS CAMPOS DE FILTRO
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


//        dataSource = getChamadosList();
//        chamadosListAdapter = new ChamadosListAdapter(dataSource);
//
//        ArrayList<JSONObject> sectorFilter = RawJsonReader.makeDataSource(this, R.raw.filter_sector_example);
//        ArrayList<JSONObject> equipmentFilter = RawJsonReader.makeDataSource(this, R.raw.filter_equipment_example);
//        ArrayList<JSONObject> statusFilter = RawJsonReader.makeDataSource(this, R.raw.filter_status_example);
//
//        populateRecyclerView(findViewById(R.id.chamados_list), chamadosListAdapter);
//
//        FloatingActionButton floatingActionButton = findViewById(R.id.chamados_floating);
//        floatingActionButton.setOnClickListener(view -> toggleFilterLayout());
//
//        Spinner sectorSpinner = findViewById(R.id.filter_sector);
//        Spinner equipmentSpinner = findViewById(R.id.filter_equipment);
//        Spinner statusSpinner = findViewById(R.id.filter_status);
//
//        makeSpinnerItems(sectorSpinner, sectorFilter);
//        makeSpinnerItems(equipmentSpinner, equipmentFilter);
//        makeSpinnerItems(statusSpinner, statusFilter);
//
//        Button filterBtn = findViewById(R.id.filter_button);
//
//        filterBtn.setOnClickListener(view -> {
//
//            chamadosListAdapter.applyFilter(filter(getString(R.string.chamado_sector), sectorSpinner.getSelectedItem().toString(), dataSource));
//            chamadosListAdapter.applyFilter(filter(getString(R.string.chamado_equipment), equipmentSpinner.getSelectedItem().toString(), chamadosListAdapter.getDataSet()));
//            chamadosListAdapter.applyFilter(filter(getString(R.string.chamado_status), statusSpinner.getSelectedItem().toString(),chamadosListAdapter.getDataSet()));
//
//            toggleFilterLayout();
//        });
//
//        for (JSONObject chamado : dataSource) {
//            Log.println(Log.ASSERT, "msg", chamado.toString() + "\n");
//        }
    }

//    private void populateRecyclerView(RecyclerView recyclerView, ChamadosListAdapter adapter) {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void toggleFilterLayout() {
//        ConstraintLayout filterLayout = findViewById(R.id.filter_layout);
//        if (filterLayout.getVisibility() != View.VISIBLE) {
//            filterLayout.setVisibility(View.VISIBLE);
//        } else {
//            filterLayout.setVisibility(View.GONE);
//        }
//    }
//
//    private ArrayList<JSONObject> filter(String key, String value, ArrayList<JSONObject> dataSet) {
//        ArrayList<JSONObject> filteredList = new ArrayList<>();
//        if (!value.equals(getString(R.string.filter_default))) {
//            for (int i = 0; i < dataSet.size(); i++) {
//                if (JsonUtil.getJsonVal(dataSet.get(i), key).equals(value)) {
//                    filteredList.add(dataSet.get(i));
//                }
//            }
//            return filteredList;
//        }
//        return dataSet;
//    }
//
//    private void makeSpinnerItems(Spinner spinner, ArrayList<JSONObject> optionsList) {
//        ArrayList<CharSequence> optionsString = new ArrayList<>();
//
//        optionsString.add(getString(R.string.filter_default));
//
//        for (int i = 0; i < optionsList.size(); i++) {
//            optionsString.add(JsonUtil.getJsonVal(optionsList.get(i), getString(R.string.filter_name)));
//        }
//
//        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<>(
//                getApplicationContext(), R.layout.spinner_item, optionsString);
//
//        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
//        spinner.setAdapter(arrayAdapter);
//    }
//
//    private ArrayList<JSONObject> getChamadosList() {
//        Context context   = getApplicationContext();
//        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE);
//        String hashLogin = sharedPref.getString(getString(R.string.is_hash_login), "");
//
//
//        @SuppressLint("StringFormatMatches")
//        String urlRequest = String.format(getResources().getString(R.string.api_chamados), hashLogin, "");
//        ArrayList<JSONObject> chamadosList = new ArrayList<>();
//
//        try {
//            JSONObject jsonObject = HttpClientUtil.requestJson(urlRequest);
//            chamadosList = JsonUtil.jsonList(new JSONArray(JsonUtil.getJsonVal(jsonObject, getString(R.string.chamados_list_key))));
//        } catch (JSONException e) {
//            Toast.makeText(context, R.string.app_fail, Toast.LENGTH_SHORT).show();
//        }
//
//
//        return chamadosList;
//    }
}