package br.com.dticampossales.appsischamados.adapters.Chamados;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Utils.Dates;
import Utils.JsonUtil;
import br.com.dticampossales.appsischamados.R;
import br.com.dticampossales.appsischamados.controllers.ChamadosController;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChamadosListAdapter extends RecyclerView.Adapter<ChamadosListAdapter.ViewHolder> {
    private ArrayList<JSONObject> dataSource;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView chamadoCode;
        private final TextView chamadoType;
        private final TextView chamadoSector;
        private final TextView chamadoDate;
        private final TextView chamadoDatePrev;
        private final TextView chamadoTechnician;

        public ViewHolder(View view) {
            super(view);

            chamadoCode = view.findViewById(R.id.chamado_code);
            chamadoType = view.findViewById(R.id.chamado_type);
            chamadoSector = view.findViewById(R.id.chamado_sector);
            chamadoDate = view.findViewById(R.id.chamado_date);
            chamadoDatePrev = view.findViewById(R.id.chamado_date_prev);
            chamadoTechnician = view.findViewById(R.id.chamado_tec);
        }
    }

    public ChamadosListAdapter(ArrayList<JSONObject> dataSource) {
        this.dataSource = dataSource;
    }

    public void applyFilter(ArrayList<JSONObject> filteredDataSet) {
        this.dataSource = filteredDataSet;
        notifyDataSetChanged();
    }

    public ArrayList<JSONObject> getDataSet() {
        return this.dataSource;
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

        viewHolder.chamadoCode.setText(JsonUtil.getJsonVal(
                dataSource.get(position), context.getString(R.string.chamado_code)));

        viewHolder.chamadoType.setText(JsonUtil.getJsonVal(
                dataSource.get(position), context.getString(R.string.chamado_type)));

        viewHolder.chamadoSector.setText(JsonUtil.getJsonVal(
                dataSource.get(position), context.getString(R.string.chamado_sector)));

        viewHolder.chamadoDate.setText(Dates.fmtLocal(JsonUtil.getJsonVal(
                dataSource.get(position), context.getString(R.string.chamado_date))));

        viewHolder.chamadoDatePrev.setText(Dates.fmtLocal(JsonUtil.getJsonVal(
                dataSource.get(position), context.getString(R.string.chamado_date))));

        viewHolder.chamadoTechnician.setText(JsonUtil.getJsonVal(
                dataSource.get(position), context.getString(R.string.chamado_tec)));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}