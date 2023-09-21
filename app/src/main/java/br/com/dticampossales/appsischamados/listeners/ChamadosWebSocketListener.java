package br.com.dticampossales.appsischamados.listeners;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChamadosWebSocketListener extends WebSocketListener {
    @Override public void onOpen(WebSocket webSocket, Response response) {
        Log.i(TAG, String.format("OPENED: %s", response.message()));
    }

    @Override public void onMessage(WebSocket webSocket, ByteString text) {
        Log.i(TAG, String.format("MESSAGE: %s", text.toString()));
    }

    @Override public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
        Log.i(TAG, String.format("CLOSE: %d %s", code, reason));
    }

    @Override public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }

    private static final String TAG = "NotificationWebSocket";
}
