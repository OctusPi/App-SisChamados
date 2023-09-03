package Utils;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpClientUtil {
    public static CompletableFuture<JSONObject> asyncJson(String urlApi) {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urlApi).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                call.cancel();
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        ResponseBody respBody = response.body();
                        if (respBody != null) {
                            String responseBody = respBody.string();
                            JSONObject jsonObject = new JSONObject(responseBody);
                            future.complete(jsonObject);
                        } else {
                            future.completeExceptionally(new Exception("Empty Respose"));
                        }
                    } catch (JSONException e) {
                        future.completeExceptionally(e);
                    }
                    response.close();
                } else {
                    future.completeExceptionally(new Exception("Wrong Response: " + response.code()));
                }
            }
        });

        return future;
    }
}