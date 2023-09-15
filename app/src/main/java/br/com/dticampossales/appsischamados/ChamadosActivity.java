package br.com.dticampossales.appsischamados;

import static br.com.dticampossales.appsischamados.R.id;
import static br.com.dticampossales.appsischamados.R.layout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import br.com.dticampossales.appsischamados.adapters.Chamados.ChamadosRecyclerViewAdapter;
import br.com.dticampossales.appsischamados.controllers.BaseController;
import br.com.dticampossales.appsischamados.controllers.ChamadosController;
import br.com.dticampossales.appsischamados.widgets.Chamados.ChamadosRecyclerView;
import br.com.dticampossales.appsischamados.widgets.Common.BaseSpinner;

public class ChamadosActivity extends AppCompatActivity {
    private ChamadosRecyclerViewAdapter chamadosRecyclerViewAdapter;
    private ConstraintLayout loadingLayout;
    private Button filterChamadosBtn;
    private Button clearChamadosBtn;
    private BaseSpinner sectorSpinner;
    private BaseSpinner statusSpinner;
    private String spinnerState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_chamados);

        FloatingActionButton floatingActionButton = findViewById(id.chamados_floating);
        floatingActionButton.setOnClickListener(view -> toggleFilterLayout());

        loadingLayout = findViewById(id.loading_view);
        ChamadosController chamadosController = new ChamadosController(this, "");
        setSpinnerState("");

        chamadosRecyclerViewAdapter = new ChamadosRecyclerViewAdapter(chamadosController);

        ChamadosRecyclerView chamadosRecyclerView = new ChamadosRecyclerView(
                getApplicationContext(),
                findViewById(id.chamados_list),
                chamadosRecyclerViewAdapter,
                findViewById(id.chamados_list_empty));

        chamadosRecyclerView.build();

        JSONObject setoresObject = chamadosController.getSetores();
        sectorSpinner = new BaseSpinner(
                getApplicationContext(),
                findViewById(id.filter_sector),
                chamadosController.getMappedPropObject(setoresObject));

        JSONObject statusObject = chamadosController.getStatus();
        statusSpinner = new BaseSpinner(
                getApplicationContext(),
                findViewById(id.filter_status),
                chamadosController.getMappedPropObject(statusObject));

        sectorSpinner.build();
        statusSpinner.build();

        filterChamadosBtn = findViewById(id.filter_button);
        clearChamadosBtn = findViewById(id.clear_button);

        filterChamadosBtn.setOnClickListener(view -> setFilterBySpinners());
        clearChamadosBtn.setOnClickListener(view -> clear());
    }

    @Override
    protected void onResume() {
        super.onResume();
        filter(spinnerState);
    }

    private void setSpinnerState(String state) {
        spinnerState = state;
    }

    private void toggleFilterLayout() {
        ConstraintLayout filterLayout = findViewById(id.filter_layout);
        filterLayout.setVisibility(filterLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    public String makeFilter(String sector, String status) {
        return sector + "," + status;
    }

    private void filter(String filter) {
        ChamadosController chamadosController = new ChamadosController(this, filter);
        chamadosRecyclerViewAdapter.applyFilter(chamadosController);
    }
    private void setFilterBySpinners() {
        setLoading(true); toggleButtonsClick();
        setSpinnerState(makeFilter(sectorSpinner.getSelectedKey(), statusSpinner.getSelectedKey()));
        filter(spinnerState);
        setLoading(false); toggleButtonsClick();
        toggleFilterLayout();
    }

    private void clear() {
        sectorSpinner.selectInitial();
        statusSpinner.selectInitial();
        filter("");
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

    private void setLoading(Boolean isVisible) {
        loadingLayout.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }
}