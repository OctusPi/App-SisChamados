package br.com.dticampossales.appsischamados;

import android.os.Bundle;
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

        FloatingActionButton floatingActionButton = findViewById(R.id.chamados_floating);
        floatingActionButton.setOnClickListener(view -> toggleFilterLayout());

        ChamadosSpinner sectorSpinner = new ChamadosSpinner(
                findViewById(R.id.filter_sector),
                ChamadosController.getPropList(this, ChamadosController.TypeList.SETORES));

        ChamadosSpinner statusSpinner = new ChamadosSpinner(
                findViewById(R.id.filter_status),
                ChamadosController.getPropList(this, ChamadosController.TypeList.STATUS));

        sectorSpinner.build();
        statusSpinner.build();

        Button filterChamadosBtn = findViewById(R.id.filter_button);

        filterChamadosBtn.setOnClickListener(view -> chamadosListAdapter.applyFilter(ChamadosController.getChamadosList(
                getApplicationContext(),
                ChamadosController.makeFilter(
                        sectorSpinner.getSelectedItemId(),
                        statusSpinner.getSelectedItemId()))));
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
        private final ArrayList<String> names;
        private final ArrayList<String> ids;

        public ChamadosSpinner(Spinner spinner, ArrayList<JSONObject> optionsList) {
            this.spinner = spinner;
            this.names = new ArrayList<>();
            this.ids = new ArrayList<>();

            this.ids.add(getString(R.string.filter_id_default));
            this.names.add(getString(R.string.filter_name_default));

            for (JSONObject option : optionsList) {
                ids.add(JsonUtil.getJsonVal(option, getString(R.string.filter_id)));
                names.add(JsonUtil.getJsonVal(option, getString(R.string.filter_name)));
            }
        }

        private void build() {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    getApplicationContext(), R.layout.spinner_item, names);

            arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

            spinner.setAdapter(arrayAdapter);
        }

        public String getSelectedItemId() {
            return ids.get(spinner.getSelectedItemPosition());
        }
    }
}