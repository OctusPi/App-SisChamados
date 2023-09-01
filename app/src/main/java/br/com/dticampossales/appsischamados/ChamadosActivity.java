package br.com.dticampossales.appsischamados;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import Utils.JsonUtil;
import Utils.RawJsonReader;
import br.com.dticampossales.appsischamados.adapters.Chamados.ChamadosListAdapter;

public class ChamadosActivity extends AppCompatActivity {
    private ArrayList<JSONObject> dataSource;
    private ChamadosListAdapter chamadosListAdapter;

    private ArrayList<JSONObject> sectorFilter;
    private ArrayList<JSONObject> equipmentFilter;
    private ArrayList<JSONObject> statusFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        dataSource = RawJsonReader.makeDataSource(this, R.raw.data_test);
        chamadosListAdapter = new ChamadosListAdapter(dataSource);

        sectorFilter = RawJsonReader.makeDataSource(this, R.raw.filter_sector_example);
        equipmentFilter = RawJsonReader.makeDataSource(this, R.raw.filter_equipment_example);
        statusFilter = RawJsonReader.makeDataSource(this, R.raw.filter_status_example);

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
            Log.println(Log.ASSERT, "msg", sectorSpinner.getSelectedItem().toString());
            filter("sector", sectorSpinner.getSelectedItem().toString(), dataSource);
        });
    }

    private void populateRecyclerView(RecyclerView recyclerView, ChamadosListAdapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void toggleFilterLayout() {
        ConstraintLayout filterLayout = findViewById(R.id.filter_layout);
        if (filterLayout.getVisibility() == View.VISIBLE) {
            filterLayout.setVisibility(View.GONE);
        } else {
            filterLayout.setVisibility(View.VISIBLE);
        }
    }

    private void filter(String key, String value, ArrayList<JSONObject> dataSet) {
        ArrayList<JSONObject> filteredList = new ArrayList<>();
        if (!value.equals(getString(R.string.filter_default))) {
            for (int i = 0; i < dataSet.size(); i++) {
                if (JsonUtil.getJsonVal(dataSet.get(i), key).equals(value)) {
                    filteredList.add(dataSet.get(i));
                    Log.println(Log.ASSERT, "msg", JsonUtil.getJsonVal(dataSet.get(i), key));
                }
            }
            chamadosListAdapter.applyFilter(filteredList);
        }
    }

    private void makeSpinnerItems(Spinner spinner, ArrayList<JSONObject> optionsList) {
        ArrayList<CharSequence> optionsString = new ArrayList<>();

        optionsString.add(getString(R.string.filter_default));

        for (int i = 0; i < optionsList.size(); i++) {
            optionsString.add(JsonUtil.getJsonVal(optionsList.get(i), "name"));
        }

        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, optionsString);

        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(arrayAdapter);
    }
}