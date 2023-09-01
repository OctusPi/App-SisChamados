package br.com.dticampossales.appsischamados;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;

import Utils.JsonUtil;
import Utils.RawJsonReader;
import br.com.dticampossales.appsischamados.adapters.Chamados.ChamadosListAdapter;
import br.com.dticampossales.appsischamados.adapters.Chamados.ChamadosSpinnerAdapter;

public class ChamadosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        populateRecyclerView(findViewById(R.id.chamados_list),
                RawJsonReader.makeDataSource(this, R.raw.data_test));

        FloatingActionButton floatingActionButton = findViewById(R.id.chamados_floating);
        floatingActionButton.setOnClickListener(view -> toggleFilterLayout());

        makeSpinnerItems(findViewById(R.id.filter_sector),
                RawJsonReader.makeDataSource(this, R.raw.filter_sector_example));

        makeSpinnerItems(findViewById(R.id.filter_equipment),
                RawJsonReader.makeDataSource(this, R.raw.filter_equipment_example));

        makeSpinnerItems(findViewById(R.id.filter_status),
                RawJsonReader.makeDataSource(this, R.raw.filter_status_example));
    }

    private void populateRecyclerView(RecyclerView recyclerView, ArrayList<JSONObject> dataSource) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ChamadosListAdapter(dataSource));
    }

    private void toggleFilterLayout() {
        ConstraintLayout filterLayout = findViewById(R.id.filter_layout);
        if (filterLayout.getVisibility() != View.VISIBLE) {
            filterLayout.setVisibility(View.VISIBLE);
            filterLayout.animate().translationYBy(filterLayout.getHeight());

        } else {
            filterLayout.setVisibility(View.GONE);
            filterLayout.animate().translationYBy(-filterLayout.getHeight());
        }
    }

    private void makeSpinnerItems(Spinner spinner, ArrayList<JSONObject> optionsList) {
        ArrayList<CharSequence> optionsString = new ArrayList<CharSequence>();

        optionsString.add(getString(R.string.filter_default));

        for (int i = 0; i < optionsList.size(); i++) {
            optionsString.add(JsonUtil.getJsonVal(optionsList.get(i), "name"));
        }

        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.spinner_item, optionsString);

        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new ChamadosSpinnerAdapter());
    }


}