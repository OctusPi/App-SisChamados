package br.com.dticampossales.appsischamados.adapters.Atendimento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import java.util.ArrayList;

import Utils.Dates;
import Utils.JsonUtil;
import br.com.dticampossales.appsischamados.R;
import br.com.dticampossales.appsischamados.controllers.AtendimentoController;

public class AtendimentoRecyclerViewAdapter extends RecyclerView.Adapter<AtendimentoRecyclerViewAdapter.ViewHolder> {
    private ArrayList<JSONObject> historico;
    private JSONObject tecnicos;
    private Context context;
    private Integer chamadoId;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tecnico;
        private final TextView reportDate;
        private final TextView reportMsg;

        public ViewHolder(View view) {
            super(view);

            tecnico = view.findViewById(R.id.report_tec);
            reportDate = view.findViewById(R.id.report_date);
            reportMsg = view.findViewById(R.id.report_msg);
        }
    }

    public AtendimentoRecyclerViewAdapter(AtendimentoController atendimentoController) {
        setAdapterData(atendimentoController);
    }

    public void reload() {
        setAdapterData(new AtendimentoController(context, chamadoId));
    }

    public void setAdapterData(AtendimentoController atendimentoController) {
        this.context = atendimentoController.getContext();
        this.chamadoId = atendimentoController.getChamadoId();
        this.historico = atendimentoController.getHistorico();
        this.tecnicos = atendimentoController.getTecnicos();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.report_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Context context = viewHolder.itemView.getContext();

        viewHolder.tecnico.setText(getTextById(tecnicos, position, context.getString(R.string.report_tec)));
        viewHolder.reportDate.setText(makeDate(position, context.getString(R.string.report_date)));
        viewHolder.reportMsg.setText(makeText(position, context.getString(R.string.report_msg)));
    }

    @Override
    public int getItemCount() {
        return historico.size();
    }

    private String makeText(Integer position, String key) {
        return StringEscapeUtils.unescapeHtml4(JsonUtil.getJsonVal(historico.get(position), key));
    }

    private String getTextById(JSONObject jsonObject, Integer position, String key) {
        return StringEscapeUtils.unescapeHtml4(JsonUtil.getJsonVal(jsonObject, makeText(position, key)));
    }

    private String makeDate(Integer position, String key) {
        return Dates.fmtLocal(JsonUtil.getJsonVal(historico.get(position), key));
    }
}