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
    public enum TypeList {
        SETORES(1), TECNICOS(2), TIPOS(3), STATUS(4);
        private final int type;
        TypeList(int id) { type = id; }
        public int getTypeList() { return type; }
    }

    public static JSONObject getDataSet(Context context, String search) {
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

    public static ArrayList<JSONObject> getChamadosList(Context context, JSONObject dataSet) {
        ArrayList<JSONObject> chamadosList = new ArrayList<>();

        try {
            chamadosList.addAll(JsonUtil.jsonList(
                    dataSet.getJSONArray(context.getString(R.string.api_chamados_key))));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return chamadosList;
    }

    public static JSONObject getPropObject(Context context, TypeList type) {
        JSONObject fullDataSet = getDataSet(context, context.getString(R.string.api_search_default));
        JSONObject propObject = new JSONObject();

        String propKey = getPropKey(context, type);

        try {
            propObject = fullDataSet.getJSONObject(propKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return propObject;
    }

    public static SortedMap<Integer, ArrayList<String>> getMappedPropObject(Context context, TypeList type) {
        SortedMap<Integer, ArrayList<String>> mappedPropObject = new TreeMap<>();
        JSONObject fullDataSet = getDataSet(context, context.getString(R.string.api_search_default));

        String propKey = getPropKey(context, type);

        try {
            mappedPropObject = JsonUtil.mapJsonPropObject(fullDataSet.getJSONObject(propKey));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mappedPropObject;
    }

    public static String makeFilter(String sector, String status) {
        return sector + "," + status;
    }

    private static String getPropKey(Context context, TypeList type) {

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
