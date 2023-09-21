package br.com.dticampossales.appsischamados.listeners;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import Utils.JsonUtil;
import Utils.NotificationsUtil;
import Utils.PersistenceUtil;
import br.com.dticampossales.appsischamados.R;
import br.com.dticampossales.appsischamados.controllers.AtendimentoController;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChamadosWebSocketListener extends WebSocketListener {
    private final Context context;

    public ChamadosWebSocketListener(Context context) {
        this.context = context;
    }

    @Override public void onOpen(@NonNull WebSocket webSocket, Response response) {
        Log.i(TAG, String.format("OPENED: %s\nLAST_CREATED: %s\nLAST_UPDATED: %s",
                response.message(),
                getLastCreated(),
                getLastUpdated()));
    }

    @Override public void onMessage(@NonNull WebSocket webSocket, @NonNull String jsonObjectToString) {
        Log.i(TAG, jsonObjectToString);
        //        Model = {
        //            "last_created": 1,
        //            "last_updated": {"id": 1, "status": "2"}
        //        }
        JSONObject lastData = JsonUtil.stringToJson(jsonObjectToString);
        String lastCreated = JsonUtil.getJsonVal(lastData, "last_created");
        String lastUpdated = JsonUtil.getJsonVal(Objects.requireNonNull(JsonUtil.getJsonObject(lastData, "last_updated")), "id");
    }

    @Override public void onClosing(WebSocket webSocket, int code, @NonNull String reason) {
        webSocket.close(1000, null);
        Log.i(TAG, String.format("CLOSE: %d %s", code, reason));
    }

    @Override public void onFailure(@NonNull WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }

    private String getLastCreated() {
        return PersistenceUtil.getStringVal(context, context.getString(R.string.last_created_chamado));
    }

    private void setLastCreated(JSONObject lastCreated) {
        PersistenceUtil.setStringVal(context, context.getString(R.string.last_created_chamado), lastCreated.toString());
    }

    private String getLastUpdated() {
        return PersistenceUtil.getStringVal(context, context.getString(R.string.last_updated_chamado));
    }

    private void setLastUpdated(JSONObject lastUpdated) {
        PersistenceUtil.setStringVal(context, context.getString(R.string.last_updated_chamado), lastUpdated.toString());
    }

    @SuppressLint("MissingPermission")
    private void notificateChamado(int notificationId, Notification notification) {
        NotificationsUtil.notify(context, notificationId, notification);
    }

    private Notification makeNotification(JSONObject dataSet) {
        JSONObject sectors = JsonUtil.getJsonObject(dataSet, context.getString(R.string.api_setores_key));
        JSONObject detalhes = JsonUtil.getJsonObject(dataSet, context.getString(R.string.api_detalhes_key));

        assert sectors != null;
        assert detalhes != null;

        String sectorId = JsonUtil.getJsonVal(detalhes, context.getString(R.string.chamado_setor));

        String sectorName = makeText(sectors, sectorId);
        String description = makeText(detalhes, context.getString(R.string.chamado_descricao));

        NotificationCompat.Builder builder = NotificationsUtil.makeNotificationBuilder(context,
            NotificationsUtil.getNotificationChannel(context, context.getString(R.string.channel_id)).getId())
                .setContentTitle(sectorName)
                .setContentText(description)
                .setPriority(getNotificationPriority(sectorId));

        return builder.build();
    }

    private int getNotificationPriority(String sectorId) {
        final String SETOR_HOSPITAL = "1";
        if (Objects.equals(sectorId, SETOR_HOSPITAL)) {
            return NotificationCompat.PRIORITY_HIGH;
        }
        return NotificationCompat.PRIORITY_DEFAULT;
    }

    private String makeText(JSONObject object, String key) {
        String value = JsonUtil.getJsonVal(object, key);
        return StringEscapeUtils.unescapeHtml4(value);
    }

    private static final String TAG = "NotificationWebSocket";
}
