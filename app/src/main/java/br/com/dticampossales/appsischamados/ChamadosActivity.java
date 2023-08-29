package br.com.dticampossales.appsischamados;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import br.com.dticampossales.appsischamados.fragments.Chamados.ChamadosListFragment;

public class ChamadosActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamados);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.chamados_list_fragment, ChamadosListFragment.class, null)
                    .commit();
        }

        DataBindingUtil.setContentView(this, R.layout.activity_chamados);
    }
}