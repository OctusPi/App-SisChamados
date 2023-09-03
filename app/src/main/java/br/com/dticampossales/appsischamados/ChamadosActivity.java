package br.com.dticampossales.appsischamados;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;

import Utils.JsonUtil;
import br.com.dticampossales.appsischamados.adapters.Chamados.ChamadosListAdapter;
import br.com.dticampossales.appsischamados.controllers.ChamadosController;

public class ChamadosActivity extends AppCompatActivity {
//    private ArrayList<JSONObject> dataSource;
    private ChamadosListAdapter chamadosListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        chamadosListAdapter = new ChamadosListAdapter(ChamadosController.getChamadosList(getApplicationContext(), ""));
        populateRecyclerView();

        FloatingActionButton floatingActionButton = findViewById(R.id.chamados_floating);
        floatingActionButton.setOnClickListener(view -> toggleFilterLayout());

        Spinner sectorSpinner = findViewById(R.id.filter_sector);
        Spinner statusSpinner = findViewById(R.id.filter_status);
    }

    private void populateRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.chamados_list);
        ChamadosListAdapter adapter = chamadosListAdapter;

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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