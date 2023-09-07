package br.com.dticampossales.appsischamados.controllers;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import Utils.JsonUtil;
import br.com.dticampossales.appsischamados.R;

abstract class BaseController {
    private final Context context;
    private final JSONObject dataSet;
    private final JSONObject tecnicos;
    private final JSONObject setores;
    private final JSONObject tipos;
    private final JSONObject status;

    public BaseController(Context context, JSONObject dataSet) {
        this.context = context;
        this.dataSet = dataSet;

        this.tecnicos = buildPropObject(TypeList.TECNICOS);
        this.setores = buildPropObject(TypeList.SETORES);
        this.tipos = buildPropObject(TypeList.TIPOS);
        this.status = buildPropObject(TypeList.STATUS);
    }

    public Context getContext() {
        return this.context;
    }
    public JSONObject getDataSet() {
        return this.dataSet;
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

    public ArrayList<JSONObject> buildObjectList(String dataSetKey) {
        ArrayList<JSONObject> objects = new ArrayList<>();

        try {
            objects.addAll(JsonUtil.jsonList(getDataSet().getJSONArray(dataSetKey)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return objects;
    }

    private JSONObject buildPropObject(TypeList type) {
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

    public enum TypeList {
        SETORES(1), TECNICOS(2), TIPOS(3), STATUS(4);
        private final int type;
        TypeList(int id) { type = id; }
        public int getTypeList() { return type; }
    }
}
