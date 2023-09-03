package br.com.dticampossales.appsischamados.controllers;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import Utils.JsonUtil;
import Utils.Security;
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

    public static ArrayList<JSONObject> getChamadosList(Context context, String search) {
        ArrayList<JSONObject> chamadosList = new ArrayList<>();

        String hashLogin = Security.getHashLogin(context);

        if (!hashLogin.equals("")) {

            try {
                String chamadosUrl = String.format(context.getString(R.string.api_chamados), hashLogin, search);
                JSONObject jsonObject = JsonUtil.requestJson(chamadosUrl);
                chamadosList.addAll(JsonUtil.jsonList(jsonObject.getJSONArray(context.getString(R.string.api_chamados_key))));

            } catch (ExecutionException | InterruptedException | JSONException e) {
                Toast.makeText(context, R.string.app_fail, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        return chamadosList;
    }

    public static ArrayList<JSONObject> getPropList(Context context, ListType type, String search) {
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

        String hashLogin = Security.getHashLogin(context);

        if (!hashLogin.equals("")) {
            String chamadosUrl = String.format(context.getString(R.string.api_chamados), hashLogin, search);
            String finalPropKey = propKey;

            try {
                JSONObject jsonObject = JsonUtil.requestJson(chamadosUrl);
                propList.addAll(JsonUtil.jsonList(jsonObject.getJSONArray(finalPropKey)));
            } catch (ExecutionException | InterruptedException | JSONException e) {
                Toast.makeText(context, R.string.app_fail, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        return propList;
    }
}
