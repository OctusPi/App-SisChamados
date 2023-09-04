package Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    public static Map<String, ArrayList<String>> mapJsonObject(JSONObject jsonObject) {
        Iterator<String> jsonObjectKey = jsonObject.keys();
        Map<String, ArrayList<String>> jsonMap = new HashMap<>();

        try {
            for (int i = 1; i <= jsonObject.length(); i++) {
                ArrayList<String> arrayList = new ArrayList<>();
                String key = jsonObjectKey.next();

                arrayList.add(key);
                arrayList.add(jsonObject.getString(key));

                jsonMap.put(String.valueOf(i), arrayList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonMap;
    }
}
