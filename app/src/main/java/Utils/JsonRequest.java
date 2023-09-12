package Utils;

import android.view.View;
import android.widget.ProgressBar;

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

    public static JSONObject progressRequest(String urlApi, ProgressBar progressBar) throws ExecutionException, InterruptedException {

        /*
        method not finalized, checking api documentation for further implementation
        THIS IS VERY COMPLEX
        THAT'S WHY KOTLIN WAS CREATED KKKK
        */

        progressBar.setVisibility(View.VISIBLE);
        HttpJson httpJson = new HttpJson();
        CompletableFuture<JSONObject> future = httpJson.asyncRequest(urlApi);
        progressBar.setVisibility(View.GONE);
        return future.get();
    }
}
