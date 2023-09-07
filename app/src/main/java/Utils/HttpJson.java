package Utils;

import android.content.Context;

import androidx.annotation.NonNull;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class HttpJson {

    private CompletableFuture<JSONObject> future = new CompletableFuture<>();

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

}