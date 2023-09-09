package br.com.dticampossales.appsischamados.widgets.Atendimento;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.com.dticampossales.appsischamados.adapters.Atendimento.AtendimentoRecyclerViewAdapter;
import br.com.dticampossales.appsischamados.adapters.Chamados.ChamadosRecyclerViewAdapter;

public class AtendimentoRecyclerView {
    private final Context context;
    private final RecyclerView recyclerView;
    private AtendimentoRecyclerViewAdapter adapter;
    private final TextView emptyView;

    public AtendimentoRecyclerView(
            Context context,
            RecyclerView recyclerView,
            AtendimentoRecyclerViewAdapter adapter,
            TextView emptyView
    ) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.emptyView = emptyView;
    }

    public void setAdapter(AtendimentoRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    public void build() {
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }
            void checkEmpty() {
                emptyView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(30);

        recyclerView.setAdapter(adapter);
    }
}
