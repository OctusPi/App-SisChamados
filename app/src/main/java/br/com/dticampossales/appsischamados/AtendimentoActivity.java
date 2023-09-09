package br.com.dticampossales.appsischamados;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import br.com.dticampossales.appsischamados.adapters.Atendimento.AtendimentoRecyclerViewAdapter;
import br.com.dticampossales.appsischamados.controllers.AtendimentoController;
import br.com.dticampossales.appsischamados.widgets.Atendimento.AtendimentoRecyclerView;
import br.com.dticampossales.appsischamados.widgets.Chamados.ChamadosRecyclerView;

public class AtendimentoActivity extends AppCompatActivity {
    FloatingActionButton detailsFab;
    FloatingActionButton reportFormFab;
    ConstraintLayout detailsLayout;
    ConstraintLayout reportFormLayout;
    AtendimentoRecyclerView reportsList;
    AtendimentoController atendimentoController;

    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atendimento);

        Integer chamadoId = Objects.requireNonNull(getIntent().getExtras()).getInt(getString(R.string.atendimento_id));
        atendimentoController = new AtendimentoController(this, chamadoId);

        reportsList = new AtendimentoRecyclerView(
                this,
                findViewById(R.id.reports_list),
                new AtendimentoRecyclerViewAdapter(atendimentoController),
                findViewById(R.id.atendimento_list_empty));

        reportsList.build();

        detailsLayout = findViewById(R.id.details);
        reportFormLayout = findViewById(R.id.report_form);

        detailsFab = findViewById(R.id.chamado_floating_details);
        reportFormFab = findViewById(R.id.chamado_floating_report_form);

        detailsFab.setOnClickListener(view -> toggleDetailsVisibility());
        reportFormFab.setOnClickListener(view -> toggleReportFormVisibility());
    }

    private void toggleDetailsVisibility() {
        detailsLayout.setVisibility(
                detailsLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        reportFormLayout.setVisibility(View.GONE);
    }

    private void toggleReportFormVisibility() {
        reportFormLayout.setVisibility(
                reportFormLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        detailsLayout.setVisibility(View.GONE);
    }

    private void bindInformations() {

    }
}