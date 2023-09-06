package br.com.dticampossales.appsischamados;

import static br.com.dticampossales.appsischamados.R.id;
import static br.com.dticampossales.appsischamados.R.layout;
import static br.com.dticampossales.appsischamados.R.string;

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
    private ConstraintLayout loadingLayout;
    private FloatingActionButton floatingActionButton;
    private Button filterChamadosBtn;
    private Button clearChamadosBtn;
    private ChamadosSpinner sectorSpinner;
    private ChamadosSpinner statusSpinner;
    private ChamadosController chamadosController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_chamados);

        floatingActionButton = findViewById(id.chamados_floating);
        floatingActionButton.setOnClickListener(view -> toggleFilterLayout());

        loadingLayout = findViewById(id.loading_view);
        chamadosController = new ChamadosController(this, getString(string.api_search_default));
        chamadosListAdapter = new ChamadosListAdapter(chamadosController);

        populateRecyclerView();

        sectorSpinner = new ChamadosSpinner(findViewById(id.filter_sector),
                chamadosController.getMappedPropObject(ChamadosController.TypeList.SETORES));

        statusSpinner = new ChamadosSpinner(findViewById(id.filter_status),
                chamadosController.getMappedPropObject(ChamadosController.TypeList.STATUS));

        sectorSpinner.build();
        statusSpinner.build();

        filterChamadosBtn = findViewById(id.filter_button);
        clearChamadosBtn = findViewById(id.clear_button);

        filterChamadosBtn.setOnClickListener(view -> filter());
        clearChamadosBtn.setOnClickListener(view -> clear());
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
        recyclerView.setItemViewCacheSize(30);
        
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

    public String makeFilter(String sector, String status) {
        return sector + "," + status;
    }

    private void filter() {
        setLoadingVisibility(true);
        toggleButtonsClick();

        ChamadosController chamadosController = new ChamadosController(this,
                makeFilter(sectorSpinner.getSelected(), statusSpinner.getSelected()));

        chamadosListAdapter.applyFilter(chamadosController);

        setLoadingVisibility(false);
        toggleButtonsClick();

        toggleFilterLayout();
    }

    private void clear() {
        sectorSpinner.selectInitial();
        statusSpinner.selectInitial();

        filter();
    }

    private void toggleButtonsClick() {
        if (clearChamadosBtn.isClickable()) {
            clearChamadosBtn.setClickable(false);
            filterChamadosBtn.setClickable(false);
        } else {
            clearChamadosBtn.setClickable(true);
            filterChamadosBtn.setClickable(true);
        }
    }

    private void setLoadingVisibility(Boolean isVisible) {
        loadingLayout.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
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

        public void selectInitial() {
            spinner.setSelection(0);
        }
    }
}