package br.com.dticampossales.appsischamados.controllers;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import Utils.JsonUtil;
import Utils.Security;
import br.com.dticampossales.appsischamados.R;

public class ChamadosController {
    private final JSONObject dataSet;
    private final ArrayList<JSONObject> chamados;
    private final JSONObject tecnicos;
    private final JSONObject setores;
    private final JSONObject tipos;
    private final JSONObject status;
    private final Context context;

    public enum TypeList {
        SETORES(1), TECNICOS(2), TIPOS(3), STATUS(4);
        private final int type;
        TypeList(int id) { type = id; }
        public int getTypeList() { return type; }
    }

    public ChamadosController(Context context, String search) {
        this.dataSet = setDataSet(context, search);
        this.context = context;

        this.chamados = setChamadosList();
        this.tecnicos = setPropObject(TypeList.TECNICOS);
        this.setores = setPropObject(TypeList.SETORES);
        this.tipos = setPropObject(TypeList.TIPOS);
        this.status = setPropObject(TypeList.STATUS);
    }

    public ArrayList<JSONObject> getChamados() {
        return this.chamados;
    }

    public JSONObject getTecnicos() {
        return this.tecnicos;
    }

    public JSONObject getSetores() {
        return this.setores;
    }

    public JSONObject getStatus() {
        return this.status;
    }

    public JSONObject getTipos() {
        return this.tipos;
    }

    public SortedMap<Integer, ArrayList<String>> getMappedPropObject(TypeList type) {
        SortedMap<Integer, ArrayList<String>> mappedPropObject = new TreeMap<>();

        String propKey = getPropKey(context, type);

        try {
            mappedPropObject = JsonUtil.mapJsonPropObject(dataSet.getJSONObject(propKey));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mappedPropObject;
    }

    private JSONObject setDataSet(Context context, String search) {
        JSONObject fullDataSet = new JSONObject();

        String hashLogin = Security.getHashLogin(context);

        if (!hashLogin.equals("")) {
            try {
                String chamadosUrl = String.format(context.getString(R.string.api_chamados), hashLogin, search);
                fullDataSet = JsonUtil.requestJson(chamadosUrl);

            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(context, R.string.app_fail, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        return fullDataSet;
    }

    private ArrayList<JSONObject> setChamadosList() {
        ArrayList<JSONObject> chamadosList = new ArrayList<>();

        try {
            chamadosList.addAll(JsonUtil.jsonList(
                    dataSet.getJSONArray(context.getString(R.string.api_chamados_key))));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return chamadosList;
    }

    private JSONObject setPropObject(TypeList type) {
        JSONObject propObject = new JSONObject();

        String propKey = getPropKey(context, type);

        try {
            propObject = dataSet.getJSONObject(propKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return propObject;
    }

    private String getPropKey(Context context, TypeList type) {

        String propKey = "";

        switch (type) {
            case SETORES:
                propKey = context.getString(R.string.api_setores_key);
                break;
            case TECNICOS:
                propKey = context.getString(R.string.api_tecnicos_key);
                break;
            case TIPOS:
                propKey = context.getString(R.string.api_tipos_key);
                break;
            case STATUS:
                propKey = context.getString(R.string.api_status_key);
                break;
        }

        return propKey;
    };
}
