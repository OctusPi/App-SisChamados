package br.com.dticampossales.appsischamados.fragments.Chamados;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

import Utils.RawJsonReader;
import br.com.dticampossales.appsischamados.R;
import br.com.dticampossales.appsischamados.adapters.Chamados.ChamadosListAdapter;

public class ChamadosListFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView chamadosList;
        ChamadosListAdapter chamadosListAdapter;

        View view = inflater.inflate(R.layout.chamados_list, container, false);

        // Implementation of RawJsonReader Util, for the most aproximated experience with the real API
        RawJsonReader rawJsonReader = new RawJsonReader(view.getContext(), R.raw.data_test);
        ArrayList<JSONObject> dataSource = rawJsonReader.getDataSource();
        rawJsonReader.printDataSource();

        chamadosList = view.findViewById(R.id.chamados_list);
        chamadosList.setLayoutManager(new LinearLayoutManager(view.getContext()));

        chamadosListAdapter = new ChamadosListAdapter(dataSource);
        chamadosList.setAdapter(chamadosListAdapter);

        return view;
    }
}