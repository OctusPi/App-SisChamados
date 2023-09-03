package Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import br.com.dticampossales.appsischamados.ChamadosActivity;
import br.com.dticampossales.appsischamados.R;

public class JsonUtil {

    public static JSONObject requestJson(String urlApi) throws ExecutionException, InterruptedException {
        JSONObject jsonObject;
        HttpJson httpJson = new HttpJson();
        CompletableFuture<JSONObject> future = httpJson.asyncRequest(urlApi);

        jsonObject = future.get();
        return jsonObject;
    }

    public static ArrayList<JSONObject> jsonList(JSONArray jsonArray) throws JSONException {

        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObjects.add(jsonObject);
        }

        return jsonObjects;
    }

    public static String getJsonVal(JSONObject obj, String key){
        try {
            return !obj.getString(key).equals("null") ? obj.getString(key) : "***";
        } catch (JSONException e) {
            e.printStackTrace();
            return "Dado Não Localizado";
        }
    }
}
