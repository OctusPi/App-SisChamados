package Utils;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpJson {

    private final CompletableFuture<JSONObject> future = new CompletableFuture<>();

    public CompletableFuture<JSONObject> asyncRequest(String urlRequest) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urlRequest).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        ResponseBody respBody = response.body();
                        if (respBody != null) {
                            String responseBody = respBody.string();
                            JSONObject jsonObject = new JSONObject(responseBody);
                            future.complete(jsonObject);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return future;
    }

    public CompletableFuture<JSONObject> asyncPostRequest(String urlRequest, RequestBody formData) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(urlRequest).post(formData).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        ResponseBody respBody = response.body();
                        if (respBody != null) {
                            String responseBody = respBody.string();
                            JSONObject jsonObject = new JSONObject(responseBody);
                            future.complete(jsonObject);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return future;
    }

}