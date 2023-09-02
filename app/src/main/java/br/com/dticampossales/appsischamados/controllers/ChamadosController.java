package br.com.dticampossales.appsischamados.controllers;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import Utils.JsonUtil;
import br.com.dticampossales.appsischamados.R;

public class ChamadosController {
    private Context context;
    private String hash;

    private final String chamadosUrl;


    public ChamadosController(Context context, String hash) {
        this.context = context;
        this.hash = hash;

        chamadosUrl = String.format(context.getString(R.string.api_called), hash);
    }

    public ArrayList<JSONObject> getChamadosList() {
        ArrayList<JSONObject> chamadosList = new ArrayList<>();

        try {
            JSONObject dataObject = JsonUtil.requestJson(context, chamadosUrl);

            chamadosList = JsonUtil.jsonList(
                    new JSONArray(JsonUtil.getJsonVal(dataObject, context.getString(R.string.api_chamados_key))));

        } catch (IOException | JSONException e) {
            Toast.makeText(context, R.string.app_fail, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return chamadosList;
    }

    public ArrayList<JSONObject> getSetoresList() {
        ArrayList<JSONObject> setoresList = new ArrayList<>();

        try {
            JSONObject dataobject = JsonUtil.requestJson(context, chamadosUrl);

            setoresList = JsonUtil.jsonList(
                    new JSONArray(JsonUtil.getJsonVal(dataobject, context.getString(R.string.api_setores_key))));

        } catch (IOException | JSONException e) {
            Toast.makeText(context, R.string.app_fail, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return setoresList;
    }

    public ArrayList<JSONObject> getTecnicosList() {
        ArrayList<JSONObject> setoresList = new ArrayList<>();

        try {
            JSONObject dataobject = JsonUtil.requestJson(context, chamadosUrl);

            setoresList = JsonUtil.jsonList(
                    new JSONArray(JsonUtil.getJsonVal(dataobject, context.getString(R.string.api_tec_key))));

        } catch (IOException | JSONException e) {
            Toast.makeText(context, R.string.app_fail, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return setoresList;
    }
}
