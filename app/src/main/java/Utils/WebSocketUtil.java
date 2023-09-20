package Utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocketListener;

public class WebSocketUtil {
    public static okhttp3.WebSocket create(Request request, WebSocketListener webSocketListener) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        return client.newWebSocket(request, webSocketListener);
    }
}
