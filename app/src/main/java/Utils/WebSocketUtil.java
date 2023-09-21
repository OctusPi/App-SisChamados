package Utils;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketUtil {
    public static okhttp3.WebSocket create(String url, WebSocketListener webSocketListener) {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();

        return client.newWebSocket(request, webSocketListener);
    }
}
