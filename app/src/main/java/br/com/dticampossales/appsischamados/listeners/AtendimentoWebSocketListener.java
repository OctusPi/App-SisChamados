package br.com.dticampossales.appsischamados.listeners;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import org.apache.commons.text.StringEscapeUtils;
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

public class AtendimentoWebSocketListener extends WebSocketListener {
    private final Context context;

    public AtendimentoWebSocketListener(Context context) {
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

        JSONObject lastData = JsonUtil.stringToJson(jsonObjectToString);
        AtendimentoController atendimentoController;

        if (verifyLastCreatedChanged(lastData)) {
            String chamadoId = JsonUtil.getJsonVal(lastData, "last_created");
            atendimentoController = new AtendimentoController(context, Integer.parseInt(chamadoId));

            pushNotification(atendimentoController.getDataSet());
            setLastCreated(chamadoId);

        } else if (verifyLastUpdatedChanged(lastData)) {
            JSONObject chamadoObject = JsonUtil.getJsonObject(lastData, "last_updated");
            assert chamadoObject != null;
            String chamadoId = JsonUtil.getJsonVal(chamadoObject, "id");
            atendimentoController = new AtendimentoController(context, Integer.parseInt(chamadoId));

            pushNotification(atendimentoController.getDataSet());
            setLastUpdated(chamadoId);
        }
    }

    @Override public void onClosing(WebSocket webSocket, int code, @NonNull String reason) {
        Log.i(TAG, String.format("CLOSE: %d %s", code, reason));
        webSocket.close(1000, null);
    }

    @Override public void onFailure(@NonNull WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }

    private String getLastCreated() {
        return PersistenceUtil.getStringVal(context, context.getString(R.string.last_created_chamado));
    }

    private void setLastCreated(String lastCreated) {
        PersistenceUtil.setStringVal(context, context.getString(R.string.last_created_chamado), lastCreated);
    }

    private String getLastUpdated() {
        return PersistenceUtil.getStringVal(context, context.getString(R.string.last_updated_chamado));
    }

    private void setLastUpdated(String lastUpdated) {
        PersistenceUtil.setStringVal(context, context.getString(R.string.last_updated_chamado), lastUpdated);
    }

    private boolean verifyLastCreatedChanged(JSONObject messageData) {
        String lastCreated = JsonUtil.getJsonVal(messageData, "last_created");
        return !lastCreated.equals(getLastCreated());
    }

    private boolean verifyLastUpdatedChanged(JSONObject messageData) {
        String lastUpdated = JsonUtil.getJsonVal(messageData, "last_updated");
        return !lastUpdated.equals(getLastUpdated());
    }

    private void pushNotification(JSONObject dataSet) {
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

        int chamadoId = Integer.parseInt(JsonUtil.getJsonVal(detalhes, context.getString(R.string.chamado_id)));
        NotificationsUtil.notify(context, chamadoId, builder.build());
    }

    private int getNotificationPriority(String sectorId) {
        final String SETOR_HOSPITAL = context.getString(R.string.hospital_id);
        if (Objects.equals(sectorId, SETOR_HOSPITAL)) {
            return NotificationCompat.PRIORITY_HIGH;
        }
        return NotificationCompat.PRIORITY_DEFAULT;
    }

    private String makeText(JSONObject object, String key) {
        String value = JsonUtil.getJsonVal(object, key);
        return StringEscapeUtils.unescapeHtml4(value);
    }

    private static final String TAG = "AtendimentoWebSocketListener";
}
