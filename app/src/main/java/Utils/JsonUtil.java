package Utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JsonUtil {

    public static JSONObject requestJson(Context context, String url) throws IOException, JSONException {

        JSONObject jsonObject;
        String writeRequest = "temp_request.json";

        // Create instance OkHttpClient
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(context, "Falha ao Processar Solicitação.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        String strJson = response.body().string();
                        if(!RWFiles.WriteFileInternalDir(context, writeRequest, strJson)){
                            Toast.makeText(context, "Falha ao Gravar Requisicão.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(context, "Falha ao Processar Solicitação.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        File directory = context.getFilesDir();
        File file = new File(directory, writeRequest);

        if(file.exists()){
            jsonObject = new JSONObject(RWFiles.ReadFileInternalDir(context, writeRequest));
            return jsonObject;
        }else{
            return new JSONObject();
        }
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
