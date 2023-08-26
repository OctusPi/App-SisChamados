package Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Util {
    public static String getJsonStr(JSONObject obj, String key){
        try {
            return !obj.getString(key).equals("null") ? obj.getString(key) : "***";
        } catch (JSONException e) {
            return "Dado Não Localizado";
        }
    }

    public static JSONObject strToJson(String strData) throws JSONException {
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(strData);
        } catch (JSONException e) {
            jsonObj = new JSONObject("");
        }

        return jsonObj;
    }
}
