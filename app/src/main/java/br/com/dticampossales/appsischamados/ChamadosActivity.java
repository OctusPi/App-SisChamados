package br.com.dticampossales.appsischamados;

import static br.com.dticampossales.appsischamados.R.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.SortedMap;

import br.com.dticampossales.appsischamados.adapters.Chamados.ChamadosListAdapter;
import br.com.dticampossales.appsischamados.controllers.ChamadosController;

public class ChamadosActivity extends AppCompatActivity {
    private ChamadosListAdapter chamadosListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_chamados);

        ConstraintLayout loadingLayout = findViewById(id.loading_view);

        ChamadosController chamadosController = new ChamadosController(this, getString(string.api_search_default), loadingLayout);

        chamadosListAdapter = new ChamadosListAdapter(chamadosController);

        populateRecyclerView();

        FloatingActionButton floatingActionButton = findViewById(id.chamados_floating);
        floatingActionButton.setOnClickListener(view -> toggleFilterLayout());

        ChamadosSpinner sectorSpinner = new ChamadosSpinner(
                findViewById(id.filter_sector),
                chamadosController.getMappedPropObject(ChamadosController.TypeList.SETORES));


        ChamadosSpinner statusSpinner = new ChamadosSpinner(
                findViewById(id.filter_status),
                chamadosController.getMappedPropObject(ChamadosController.TypeList.STATUS));

        sectorSpinner.build();
        statusSpinner.build();

        Button filterChamadosBtn = findViewById(id.filter_button);
        Button clearChamadoBtn = findViewById(id.clear_button);

        filterChamadosBtn.setOnClickListener(view -> {
            toggleFilterLayout();
            ChamadosController filteredController = new ChamadosController(this,
                    chamadosListAdapter.makeFilter(
                        sectorSpinner.getSelected(), statusSpinner.getSelected()
                    ), loadingLayout);

            chamadosListAdapter.applyFilter(filteredController);
        });

        clearChamadoBtn.setOnClickListener(view -> {
            toggleFilterLayout();
            ChamadosController filteredController = new ChamadosController(this, getString(string.api_search_default), loadingLayout);
            chamadosListAdapter.applyFilter(filteredController);
            sectorSpinner.build(); statusSpinner.build();
        });
    }

    private void populateRecyclerView() {
        RecyclerView recyclerView = findViewById(id.chamados_list);
        ChamadosListAdapter adapter = chamadosListAdapter;

        TextView emptyMessage = findViewById(id.chamados_list_empty);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }
            void checkEmpty() {
                emptyMessage.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(15);
        
        recyclerView.setAdapter(adapter);
    }

    private void toggleFilterLayout() {
        ConstraintLayout filterLayout = findViewById(id.filter_layout);
        if (filterLayout.getVisibility() != View.VISIBLE) {
            filterLayout.setVisibility(View.VISIBLE);
        } else {
            filterLayout.setVisibility(View.GONE);
        }
    }

    private class ChamadosSpinner {
        private final Spinner spinner;
        private final SortedMap<Integer, ArrayList<String>> optionsMap;
        private final ArrayList<String> options;

        public ChamadosSpinner(Spinner spinner, SortedMap<Integer, ArrayList<String>> optionsMap) {
            this.spinner = spinner;
            this.optionsMap = optionsMap;

            this.optionsMap.put(Integer.parseInt(getString(string.filter_id_default)),
                    new ArrayList<>(Arrays.asList(getString(string.filter_id_default), getString(string.filter_name_default))));

            this.options = new ArrayList<>();

            for (Integer option : optionsMap.keySet()) {
                options.add(StringEscapeUtils.unescapeHtml4(Objects.requireNonNull(optionsMap.get(option)).get(1)));
            }
        }

        private void build() {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    getApplicationContext(), layout.spinner_item, options);

            arrayAdapter.setDropDownViewResource(layout.spinner_dropdown);

            spinner.setAdapter(arrayAdapter);
        }

        public String getSelected() {
            Integer key = spinner.getSelectedItemPosition();
            return Objects.requireNonNull(optionsMap.get(key)).get(0);
        }
    }
}