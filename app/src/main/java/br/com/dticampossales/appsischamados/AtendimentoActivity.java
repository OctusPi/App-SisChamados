package br.com.dticampossales.appsischamados;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import java.util.Objects;

import Utils.Dates;
import Utils.JsonUtil;
import br.com.dticampossales.appsischamados.adapters.Atendimento.AtendimentoRecyclerViewAdapter;
import br.com.dticampossales.appsischamados.controllers.AtendimentoController;
import br.com.dticampossales.appsischamados.databinding.ActivityAtendimentoBinding;
import br.com.dticampossales.appsischamados.validation.Atendimento.AtendimentoSpinnerValidator;
import br.com.dticampossales.appsischamados.validation.Atendimento.AtendimentoTextInputValidator;
import br.com.dticampossales.appsischamados.widgets.Atendimento.AtendimentoRecyclerView;
import br.com.dticampossales.appsischamados.widgets.Common.BaseSpinner;

public class AtendimentoActivity extends AppCompatActivity {

    ActivityAtendimentoBinding binding;
    ConstraintLayout detailsLayout;
    ConstraintLayout reportFormLayout;
    AtendimentoRecyclerView reportsList;
    AtendimentoRecyclerViewAdapter reportsListAdapter;
    AtendimentoController atendimentoController;
    BaseSpinner reportSpinner;
    TextInputLayout reportMessageLayout;
    TextInputEditText reportMessageText;
    TextView backArrow;
    Button sendReportBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atendimento);

        binding = ActivityAtendimentoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        backArrow = findViewById(R.id.details_arrow_back);
        backArrow.setOnClickListener(v -> finish());

        Integer chamadoId = Objects.requireNonNull(getIntent().getExtras()).getInt(getString(R.string.atendimento_id));
        atendimentoController = new AtendimentoController(this, chamadoId);

        bindInformations();

        reportsListAdapter = new AtendimentoRecyclerViewAdapter(atendimentoController);

        reportsList = new AtendimentoRecyclerView(
                this,
                findViewById(R.id.reports_list),
                reportsListAdapter,
                findViewById(R.id.atendimento_list_empty));

        reportsList.build();

        JSONObject statusObject = atendimentoController.getPropperStatus();
        reportSpinner = new BaseSpinner(
                getApplicationContext(),
                findViewById(R.id.report_status_spinner),
                atendimentoController.getMappedPropObject(statusObject));

        reportSpinner.build();

        detailsLayout = findViewById(R.id.details);
        reportFormLayout = findViewById(R.id.report_form);

        reportMessageLayout = findViewById(R.id.report_message_layout);
        reportMessageText = findViewById(R.id.report_message_input);

        sendReportBtn = findViewById(R.id.report_submit);

        sendReportBtn.setOnClickListener(v -> sendReport());

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_detalhes) {
                makeDetailsVisible();
                return true;
            } else if (item.getItemId() == R.id.menu_relatorio) {
                makeReportVisible();
                return true;
            }
            return false;
        });
    }

    private void makeDetailsVisible() {
        detailsLayout.setVisibility(detailsLayout.getVisibility() != View.VISIBLE ? View.VISIBLE : View.GONE);
        reportFormLayout.setVisibility(View.GONE);
    }

    private void makeReportVisible() {
        reportFormLayout.setVisibility(reportFormLayout.getVisibility() != View.VISIBLE ? View.VISIBLE : View.GONE);
        detailsLayout.setVisibility(View.GONE);
    }

    private void bindInformations() {
        JSONObject detalhes = atendimentoController.getDetalhes();

        binding.detailsStatus.setText(makeText(atendimentoController.getStatus(),
                JsonUtil.getJsonVal(detalhes, getString(R.string.chamado_status))));
        binding.detailsType.setText(makeText(atendimentoController.getTipos(),
                JsonUtil.getJsonVal(detalhes, getString(R.string.chamado_type))));
        binding.detailsSector.setText(makeText(atendimentoController.getSetores(),
                JsonUtil.getJsonVal(detalhes, getString(R.string.chamado_setor))));

        binding.detailsDataabr.setText(((String) binding.detailsDataabr.getText())
                .concat(" " + makeDate(detalhes, getString(R.string.chamado_dataabr))));
        binding.detailsDataprev.setText(((String) binding.detailsDataprev.getText())
                .concat(" " + makeDate(detalhes, getString(R.string.chamado_dataprev))));
        binding.detailsDataatend.setText(((String) binding.detailsDataatend.getText())
                .concat(" " + makeDate(detalhes, getString(R.string.chamado_dataatm))));

        binding.detailsCode.setText(makeText(detalhes, getString(R.string.chamado_code)));
        binding.detailsEquipment.setText(makeText(detalhes, getString(R.string.chamado_equipamento)));
        binding.detailsDescription.setText(makeText(detalhes, getString(R.string.chamado_descricao)));
    }

    private void sendReport() {
        boolean statusIsValid = AtendimentoSpinnerValidator.validate(reportSpinner),
                messageIsValid = AtendimentoTextInputValidator.validate(reportMessageLayout, reportMessageText);

        if (statusIsValid && messageIsValid) {
            reportMessageText.setEnabled(false);
            atendimentoController.sendReport(reportSpinner.getSelectedKey(), String.valueOf(reportMessageText.getText()));
            reportsListAdapter.refresh();
            atendimentoController = new AtendimentoController(this, atendimentoController.getChamadoId());
            bindInformations();
            detailsLayout.setVisibility(View.GONE);
            reportFormLayout.setVisibility(View.GONE);
            reportMessageText.setEnabled(true);
        }
    }

    private String makeText(JSONObject object, String key) {
        String value = JsonUtil.getJsonVal(object, key);
        return StringEscapeUtils.unescapeHtml4(value);
    }

    private String makeDate(JSONObject object, String key) {
        String value = JsonUtil.getJsonVal(object, key);
        return Dates.fmtLocal(value);
    }
}