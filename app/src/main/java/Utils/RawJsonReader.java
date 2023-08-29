package Utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.RawRes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RawJsonReader {
    private final Context context;
    private final int resourceId;

    public RawJsonReader(Context context, int resourceId) {
        this.context = context;
        this.resourceId = resourceId;
    }

    public ArrayList<JSONObject> getDataSource() {

        ArrayList<JSONObject> listJSON = null;

        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder jsonString = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray jsonArray = new JSONArray(jsonString.toString());

            listJSON = JsonUtil.jsonList(jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return listJSON;
    }

    public void printDataSource() {
        ArrayList<JSONObject> dataSource = this.getDataSource();
        for (JSONObject var : dataSource)
            Log.println(Log.DEBUG, "RawJsonDataSource", var.toString() + "\n");
    }
}
