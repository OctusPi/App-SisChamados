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
    private JSONObject dataSet;
    private ArrayList<JSONObject> chamadosList;
    private Context context;

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

    public ChamadosListAdapter(Context context) {
        this.context = context;
        this.dataSet = makeDataSet(context.getString(R.string.api_search_default));
        this.chamadosList = makeChamadosList(context.getString(R.string.api_search_default));
    }

    public void applyFilter(String search) {
        this.chamadosList = makeChamadosList(search);
        notifyDataSetChanged();
    }

    public ArrayList<JSONObject> getChamadosList() {
        return this.chamadosList;
    }

    public JSONObject getTecnicos() {
        return ChamadosController.getPropObject(context, ChamadosController.TypeList.TECNICOS);
    }

    public JSONObject getTipos() {
        return ChamadosController.getPropObject(context, ChamadosController.TypeList.TIPOS);
    }

    public JSONObject getSetores() {
        return ChamadosController.getPropObject(context, ChamadosController.TypeList.SETORES);
    }

    public JSONObject getDataSet() {
        return this.dataSet;
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

        viewHolder.chamadoCode.setText(makeText(position, context.getString(R.string.chamado_code)));

        viewHolder.chamadoType.setText(getTextById(getTipos(), position, context.getString(R.string.chamado_type)));

        viewHolder.chamadoSector.setText(getTextById(getSetores(), position, context.getString(R.string.chamado_sector)));

        viewHolder.chamadoDate.setText(Dates.fmtLocal(JsonUtil.getJsonVal(
                chamadosList.get(position), context.getString(R.string.chamado_date))));

        viewHolder.chamadoDatePrev.setText(Dates.fmtLocal(JsonUtil.getJsonVal(
                chamadosList.get(position), context.getString(R.string.chamado_date))));

        viewHolder.chamadoTechnician.setText(getTextById(getTecnicos(), position, context.getString(R.string.chamado_tec)));
    }

    @Override
    public int getItemCount() {
        return chamadosList.size();
    }

    private JSONObject makeDataSet(String search) {
        return ChamadosController.getDataSet(context, search);
    }

    private ArrayList<JSONObject> makeChamadosList(String search) {
        return ChamadosController.getChamadosList(context, makeDataSet(search));
    }

    private String makeText(Integer position, String key) {
        return JsonUtil.getJsonVal(chamadosList.get(position), key);
    }
    private String getTextById(JSONObject jsonObject, Integer position, String key) {
        return JsonUtil.getJsonVal(jsonObject, makeText(position, key));
    }

    private String makeDate() {
        return "Falta fazer";
    }
}