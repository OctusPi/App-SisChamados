package br.com.dticampossales.appsischamados;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import java.util.Objects;
import br.com.dticampossales.appsischamados.fragments.Chamados.ChamadosListFragment;

public class ChamadosActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        getSupportActionBar().setTitle(null);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.chamados_list_fragment, ChamadosListFragment.class, null)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        makeSpinnerItems(
                (Spinner) findViewById(R.id.spinner_filter),
                getResources().getStringArray(R.array.filters));

        makeSpinnerItems((Spinner) findViewById(R.id.spinner_value),
                getResources().getStringArray(R.array.filters));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ConstraintLayout filterLayout = findViewById(R.id.filter_container);
        LinearLayout searchLayout = findViewById(R.id.search_container);

        if (item.getItemId() == R.id.menu_filter) {
            if (filterLayout.getVisibility() != View.VISIBLE) {
                filterLayout.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.GONE);
            } else {
                filterLayout.setVisibility(View.GONE);
            }
        } else if (item.getItemId() == R.id.menu_search) {
            if (searchLayout.getVisibility() != View.VISIBLE) {
                filterLayout.setVisibility(View.GONE);
                searchLayout.setVisibility(View.VISIBLE);
            } else {
                searchLayout.setVisibility(View.GONE);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeSpinnerItems(Spinner spinner, String[] options) {
        ArrayAdapter<CharSequence> arrayAdapter = new ArrayAdapter<>(
                        getApplicationContext(), R.layout.spinner_item, options);

        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}