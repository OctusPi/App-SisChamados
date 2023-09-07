package br.com.dticampossales.appsischamados.controllers;

import android.content.Context;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import Utils.JsonRequest;
import Utils.JsonUtil;
import Utils.Security;
import br.com.dticampossales.appsischamados.R;

public class ChamadosController extends BaseController {
    private final ArrayList<JSONObject> chamados;

    public ChamadosController(Context context, String search) {
        super(context, setDataSet(context, search));
        this.chamados = buildObjectList(getContext().getString(R.string.api_chamados_key));
    }

    public ArrayList<JSONObject> getChamados() {
        return this.chamados;
    }

    private static JSONObject setDataSet(Context context, String search) {
        JSONObject fullDataSet = new JSONObject();

        String hashLogin = Security.getHashLogin(context);

        if (!hashLogin.equals("")) {
            try {
                String chamadosUrl = String.format(context.getString(R.string.api_chamados), hashLogin, search);
                fullDataSet = JsonRequest.request(chamadosUrl);

            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(context, R.string.app_fail, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        return fullDataSet;
    }
}
