package br.com.dticampossales.appsischamados.adapters.Chamados;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import br.com.dticampossales.appsischamados.AtendimentoActivity;
import br.com.dticampossales.appsischamados.R;
import br.com.dticampossales.appsischamados.controllers.ChamadosController;

public class ChamadosRecyclerViewAdapter extends RecyclerView.Adapter<ChamadosRecyclerViewAdapter.ViewHolder> {
    private ArrayList<JSONObject> chamados;
    private JSONObject tecnicos;
    private JSONObject setores;
    private JSONObject status;
    private JSONObject tipos;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView chamadoId;
        private final TextView chamadoCode;
        private final TextView chamadoType;
        private final TextView chamadoSector;
        private final TextView chamadoDate;
        private final TextView chamadoDatePrev;
        private final TextView chamadoTechnician;
        private final View chamadoStatus;

        public ViewHolder(View view) {
            super(view);

            chamadoId = view.findViewById(R.id.chamado_id);
            chamadoCode = view.findViewById(R.id.chamado_code);
            chamadoType = view.findViewById(R.id.chamado_type);
            chamadoSector = view.findViewById(R.id.chamado_sector);
            chamadoDate = view.findViewById(R.id.chamado_date);
            chamadoDatePrev = view.findViewById(R.id.chamado_date_prev);
            chamadoTechnician = view.findViewById(R.id.chamado_tec);
            chamadoStatus = view.findViewById(R.id.chamado_status);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(view.getContext(), AtendimentoActivity.class);
                intent.putExtra(view.getContext().getString(R.string.atendimento_id), Integer.parseInt((String) chamadoId.getText()));

                view.getContext().startActivity(intent);
            });
        }
    }

    public ChamadosRecyclerViewAdapter(ChamadosController chamadosController) {
        this.chamados = chamadosController.getChamados();
        this.tecnicos = chamadosController.getTecnicos();
        this.setores = chamadosController.getSetores();
        this.status = chamadosController.getStatus();
        this.tipos = chamadosController.getTipos();
    }

    public void applyFilter(ChamadosController chamadosController) {
        this.chamados = chamadosController.getChamados();
        this.tecnicos = chamadosController.getTecnicos();
        this.setores = chamadosController.getSetores();
        this.status = chamadosController.getStatus();
        this.tipos = chamadosController.getTipos();

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.chamado_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Context context = viewHolder.itemView.getContext();

        viewHolder.chamadoId.setText(makeText(position, context.getString(R.string.chamado_id)));
        viewHolder.chamadoCode.setText(makeText(position, context.getString(R.string.chamado_code)));
        viewHolder.chamadoType.setText(getTextById(tipos, position, context.getString(R.string.chamado_type)));
        viewHolder.chamadoSector.setText(getTextById(setores, position, context.getString(R.string.chamado_sector)));
        viewHolder.chamadoDate.setText(makeDate(position, context.getString(R.string.chamado_date)));
        viewHolder.chamadoDatePrev.setText(makeDate(position,  context.getString(R.string.chamado_date)));
        viewHolder.chamadoTechnician.setText(getTextById(tecnicos, position, context.getString(R.string.chamado_tec)));
        viewHolder.chamadoStatus.setBackgroundColor(makeStatusColorById(context, position));
    }

    @Override
    public int getItemCount() {
        return chamados.size();
    }

    private String makeText(Integer position, String key) {
        return StringEscapeUtils.unescapeHtml4(JsonUtil.getJsonVal(chamados.get(position), key));
    }

    private String getTextById(JSONObject jsonObject, Integer position, String key) {
        return StringEscapeUtils.unescapeHtml4(JsonUtil.getJsonVal(jsonObject, makeText(position, key)));
    }

    private String makeDate(Integer position, String key) {
        return Dates.fmtLocal(JsonUtil.getJsonVal(chamados.get(position), key));
    }

    private Integer makeStatusColorById(Context context, Integer position) {
        int color = context.getColor(R.color.bs_indigo);

        int statusId = Integer.parseInt(JsonUtil.getJsonVal(chamados.get(position), context.getString(R.string.api_status_key)));

        switch (statusId) {
            case 1:
                color = context.getColor(R.color.bs_red);
                break;
            case 2:
                color = context.getColor(R.color.bs_blue);
                break;
            case 3:
                color = context.getColor(R.color.bs_teal);
                break;
            case 4:
                color = context.getColor(R.color.bs_orange);
                break;
            case 5:
                color = context.getColor(R.color.bs_gray);
                break;
        }

        return color;
    }
}