package br.com.dticampossales.appsischamados.controllers;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import Utils.HttpClientUtil;
import Utils.JsonUtil;
import br.com.dticampossales.appsischamados.R;

public class ChamadosController {
    public enum ListType {
        SETORES(1), TECNICOS(2), TIPOS(3), STATUS(4);
        private final int type;
        ListType(int id) {
            type = id;
        }

        public int getListType() {
            return type;
        }
    }

    public static ArrayList<JSONObject> getChamadosList(Context context) {
        ArrayList<JSONObject> chamadosList = new ArrayList<>();

        String hashLogin = context.getSharedPreferences(context.getString(R.string.preference_key), Context.MODE_PRIVATE)
                .getString(context.getString(R.string.is_hash_login), "");

        if (!hashLogin.equals("")) {
            String chamadosUrl = String.format(context.getString(R.string.api_chamados), hashLogin, "0,0");

            CompletableFuture<JSONObject> future = HttpClientUtil.asyncJson(chamadosUrl);
            future.thenAccept(response -> {
                try {
                    chamadosList.addAll(JsonUtil.jsonList(response.getJSONArray(context.getString(R.string.api_chamados_key))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }).exceptionally(e -> {
                e.printStackTrace();
                return null;
            });
        }

        return chamadosList;
    }

    public static ArrayList<JSONObject> getPropList(Context context, ListType type) {
        ArrayList<JSONObject> propList = new ArrayList<>();
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

        String hashLogin = context.getSharedPreferences(context.getString(R.string.preference_key), Context.MODE_PRIVATE)
                .getString(context.getString(R.string.is_hash_login), "");

        if (!hashLogin.equals("")) {
            String chamadosUrl = String.format(context.getString(R.string.api_chamados), hashLogin, "0,0");

            String finalPropKey = propKey;

            CompletableFuture<JSONObject> future = HttpClientUtil.asyncJson(chamadosUrl);
            future.thenAccept(response -> {
                try {
                    propList.addAll(JsonUtil.jsonList(response.getJSONArray(finalPropKey)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }).exceptionally(e -> {
                e.printStackTrace();
                return null;
            });
        }

        return propList;
    }
}
