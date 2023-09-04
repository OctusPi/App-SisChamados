package Utils;

import androidx.annotation.NonNull;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class HttpJson {
    private JSONObject jsonGlobal = new JSONObject();
    private CompletableFuture<JSONObject> future = new CompletableFuture<>();

    public CompletableFuture<JSONObject> asyncRequest(String urlRequest) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urlRequest).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace(); // Lidar com a falha da requisição
                future.complete(jsonGlobal); // Completa o futuro com o JSON global
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        ResponseBody respBody = response.body();
                        if (respBody != null) {
                            String responseBody = respBody.string();
                            JSONObject jsonObject = new JSONObject(responseBody);
                            jsonGlobal = jsonObject;
                            future.complete(jsonObject);
                        } else {
                            future.complete(jsonGlobal);
                        }
                    } else {
                        future.complete(jsonGlobal);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    future.complete(jsonGlobal);
                }
            }
        });

        // Retorna o CompletableFuture para permitir que outras partes do código esperem pelo resultado.
        return future;
    }

}