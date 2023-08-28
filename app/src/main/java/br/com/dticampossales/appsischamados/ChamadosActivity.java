package br.com.dticampossales.appsischamados;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import br.com.dticampossales.appsischamados.adapters.ChamadosAdapter;

public class ChamadosActivity extends AppCompatActivity {

    private ChamadosAdapter chamadosAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        DataBindingUtil.setContentView(this, R.layout.activity_chamados);
    }
}