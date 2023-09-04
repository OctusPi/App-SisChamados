package br.com.dticampossales.appsischamados;

import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import Utils.JsonUtil;
import br.com.dticampossales.appsischamados.adapters.Chamados.ChamadosListAdapter;
import br.com.dticampossales.appsischamados.controllers.ChamadosController;

public class ChamadosActivity extends AppCompatActivity {
    private ChamadosListAdapter chamadosListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        chamadosListAdapter = new ChamadosListAdapter(
                ChamadosController.getChamadosList(getApplicationContext(), getString(R.string.api_search_default)));

        populateRecyclerView();

        ChamadosController.getMappedPropObject(this, ChamadosController.TypeList.SETORES);

        FloatingActionButton floatingActionButton = findViewById(R.id.chamados_floating);
        floatingActionButton.setOnClickListener(view -> toggleFilterLayout());

        ChamadosSpinner sectorSpinner = new ChamadosSpinner(
                findViewById(R.id.filter_sector),
                ChamadosController.getMappedPropObject(this, ChamadosController.TypeList.SETORES));

        ChamadosSpinner statusSpinner = new ChamadosSpinner(
                findViewById(R.id.filter_status),
                ChamadosController.getMappedPropObject(this, ChamadosController.TypeList.STATUS));

        sectorSpinner.build();
        statusSpinner.build();

        Button filterChamadosBtn = findViewById(R.id.filter_button);

        filterChamadosBtn.setOnClickListener(view -> chamadosListAdapter.applyFilter(ChamadosController.getChamadosList(
                getApplicationContext(),
                ChamadosController.makeFilter(
                        sectorSpinner.getSelected(),
                        statusSpinner.getSelected()))));
    }

    private void populateRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.chamados_list);
        ChamadosListAdapter adapter = chamadosListAdapter;

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        
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

    private class ChamadosSpinner {
        private final Spinner spinner;

        private final Map<String, ArrayList<String>> optionsMap;
        private final ArrayList<String> options;

        public ChamadosSpinner(Spinner spinner, Map<String, ArrayList<String>> optionsMap) {
            this.spinner = spinner;
            this.optionsMap = optionsMap;

            this.optionsMap.put("0", new ArrayList<>(
                    Arrays.asList(getString(R.string.filter_id_default),
                            getString(R.string.filter_name_default))));

            this.options = new ArrayList<>();

            for (String option : optionsMap.keySet()) {
                options.add(Objects.requireNonNull(optionsMap.get(option)).get(1));
            }
        }

        private void build() {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, options);

            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

            spinner.setAdapter(arrayAdapter);
        }

        public String getSelected() {
            String key = String.valueOf(spinner.getSelectedItemPosition());
            Log.i("msg", Objects.requireNonNull(optionsMap.get(key)).get(0));

            return Objects.requireNonNull(optionsMap.get(key)).get(0);
        }
    }
}