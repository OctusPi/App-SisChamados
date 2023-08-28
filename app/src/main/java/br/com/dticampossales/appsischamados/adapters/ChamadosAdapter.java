package br.com.dticampossales.appsischamados.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.dticampossales.appsischamados.R;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ChamadosAdapter extends RecyclerView.Adapter<ChamadosAdapter.ViewHolder> {
    private List<Object> dataSet;

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

    public ChamadosAdapter(List<Object> dataSet) {
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

        viewHolder.chamadoId.setText("ASDF");
        viewHolder.chamadoCode.setText("ASDF");
        viewHolder.chamadoType.setText("ASDF");
        viewHolder.chamadoSector.setText("ASDF");
        viewHolder.chamadoDate.setText("ASDF");
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}