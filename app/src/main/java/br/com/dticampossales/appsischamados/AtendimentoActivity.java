package br.com.dticampossales.appsischamados;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AtendimentoActivity extends AppCompatActivity {
    FloatingActionButton detailsFab;
    FloatingActionButton reportFormFab;
    ConstraintLayout detailsLayout;
    ConstraintLayout reportFormLayout;
    RecyclerView reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamado);

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
}