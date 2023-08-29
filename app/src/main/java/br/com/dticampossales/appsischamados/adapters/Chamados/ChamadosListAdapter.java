package br.com.dticampossales.appsischamados.adapters.Chamados;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import Utils.JsonUtil;
import br.com.dticampossales.appsischamados.R;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;


public class ChamadosListAdapter extends RecyclerView.Adapter<ChamadosListAdapter.ViewHolder> {
    private ArrayList<JSONObject> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Apenas para teste
        private final TextView chamadoId;
        private final TextView chamadoCode;
        private final TextView chamadoType;
        private final TextView chamadoSector;
        private final TextView chamadoDate;

        public ViewHolder(View view) {
            super(view);

            chamadoId = (TextView) view.findViewById(R.id.chamado_id);
            chamadoCode = (TextView) view.findViewById(R.id.chamado_code);
            chamadoType = (TextView) view.findViewById(R.id.chamado_type);
            chamadoSector = (TextView) view.findViewById(R.id.chamado_sector);
            chamadoDate = (TextView) view.findViewById(R.id.chamado_date);
        }
    }

    public ChamadosListAdapter(ArrayList<JSONObject> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.chamado_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.chamadoId.setText(JsonUtil.getJsonVal(dataSet.get(position),
                viewHolder.itemView.getContext().getString(R.string.chamado_id)));

        viewHolder.chamadoCode.setText(JsonUtil.getJsonVal(dataSet.get(position),
                viewHolder.itemView.getContext().getString(R.string.chamado_code)));

        viewHolder.chamadoType.setText(JsonUtil.getJsonVal(dataSet.get(position),
                viewHolder.itemView.getContext().getString(R.string.chamado_type)));

        viewHolder.chamadoSector.setText(JsonUtil.getJsonVal(dataSet.get(position),
                viewHolder.itemView.getContext().getString(R.string.chamado_sector)));

        viewHolder.chamadoDate.setText(JsonUtil.getJsonVal(dataSet.get(position),
                viewHolder.itemView.getContext().getString(R.string.chamado_date)));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}