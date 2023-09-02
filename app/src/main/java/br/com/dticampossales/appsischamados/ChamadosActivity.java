package br.com.dticampossales.appsischamados;

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

        ChamadosController chamadosController = new ChamadosController(this, null);

        dataSource = chamadosController.getChamadosList();
        chamadosListAdapter = new ChamadosListAdapter(dataSource);

        ArrayList<JSONObject> sectorFilter = RawJsonReader.makeDataSource(this, R.raw.filter_sector_example);
        ArrayList<JSONObject> equipmentFilter = RawJsonReader.makeDataSource(this, R.raw.filter_equipment_example);
        ArrayList<JSONObject> statusFilter = RawJsonReader.makeDataSource(this, R.raw.filter_status_example);

        populateRecyclerView(findViewById(R.id.chamados_list), chamadosListAdapter);

        FloatingActionButton floatingActionButton = findViewById(R.id.chamados_floating);
        floatingActionButton.setOnClickListener(view -> toggleFilterLayout());

        Spinner sectorSpinner = findViewById(R.id.filter_sector);
        Spinner equipmentSpinner = findViewById(R.id.filter_equipment);
        Spinner statusSpinner = findViewById(R.id.filter_status);

        makeSpinnerItems(sectorSpinner, sectorFilter);
        makeSpinnerItems(equipmentSpinner, equipmentFilter);
        makeSpinnerItems(statusSpinner, statusFilter);

        Button filterBtn = findViewById(R.id.filter_button);

        filterBtn.setOnClickListener(view -> {

            chamadosListAdapter.applyFilter(filter(getString(R.string.chamado_sector), sectorSpinner.getSelectedItem().toString(), dataSource));
            chamadosListAdapter.applyFilter(filter(getString(R.string.chamado_equipment), equipmentSpinner.getSelectedItem().toString(), chamadosListAdapter.getDataSet()));
            chamadosListAdapter.applyFilter(filter(getString(R.string.chamado_status), statusSpinner.getSelectedItem().toString(),chamadosListAdapter.getDataSet()));

            toggleFilterLayout();
        });
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

        for (int i = 0; i < optionsList.size(); i++) {
            optionsString.add(JsonUtil.getJsonVal(optionsList.get(i), getString(R.string.filter_name)));
        }

        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, optionsString);

        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(arrayAdapter);
    }

    private ArrayList<JSONObject> filter(String key, String value, ArrayList<JSONObject> dataSet) {
        ArrayList<JSONObject> filteredList = new ArrayList<>();
        if (!value.equals(getString(R.string.filter_default))) {
            for (int i = 0; i < dataSet.size(); i++) {
                if (JsonUtil.getJsonVal(dataSet.get(i), key).equals(value)) {
                    filteredList.add(dataSet.get(i));
                }
            }
            return filteredList;
        }
        return dataSet;
    }
}