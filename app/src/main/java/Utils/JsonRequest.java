package Utils;

import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.RequestBody;

public class JsonRequest {
    public static JSONObject request(String urlApi) throws ExecutionException, InterruptedException {
        HttpJson httpJson = new HttpJson();
        CompletableFuture<JSONObject> future = httpJson.asyncRequest(urlApi);
        return future.get();
    }

    public static JSONObject postRequest(String urlApi, RequestBody formData) throws ExecutionException, InterruptedException {
        HttpJson httpJson = new HttpJson();
        CompletableFuture<JSONObject> future = httpJson.asyncPostRequest(urlApi, formData);
        return future.get();
    }
}
