package br.com.dticampossales.appsischamados.listeners;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import Utils.JsonRequest;
import Utils.JsonUtil;
import Utils.NotificationsUtil;
import Utils.Persistence;
import Utils.Security;
import br.com.dticampossales.appsischamados.AtendimentoActivity;
import br.com.dticampossales.appsischamados.R;

public class NotificationsObserver {
    private final Context context;

    public NotificationsObserver(Context context) {
        this.context = context;
    }

    public void setInitialValues() {
        JSONObject response = makeRequest();
        setPersistenceValues(response);
    }

    public void runAsync() {

    }

    public void run() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                JSONObject response = makeRequest();
                if (!response.toString().equals(getLastNotify())) {
                    pushNotification(JsonUtil.getJsonObject(response, context.getString(R.string.api_detalhes_key)));
                    setPersistenceValues(response);
                }
            }
        },0,1000 * 10);
    }

    private void pushNotification(JSONObject chamadoDetails) {
        int chamadoId = Integer.parseInt(JsonUtil.getJsonVal(chamadoDetails, context.getString(R.string.chamado_id)));

        Log.i("msg", String.valueOf(chamadoId));

        NotificationCompat.Builder builder = NotificationsUtil.makeNotificationBuilder(context,
            NotificationsUtil.getNotificationChannel(context, context.getString(R.string.channel_id)).getId())
                .setContentTitle(JsonUtil.getJsonVal(chamadoDetails, context.getString(R.string.chamado_code)))
                .setContentText(JsonUtil.getJsonVal(chamadoDetails, context.getString(R.string.chamado_setor)))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(JsonUtil.getJsonVal(chamadoDetails, context.getString(R.string.chamado_descricao))))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(makeNotificationIntent(chamadoId));

        NotificationsUtil.notify(context, chamadoId, builder.build());
    }

    private String getLastNotify() {
        return Persistence.getStringVal(context, context.getString(R.string.sp_notification_key));
    }

    private PendingIntent makeNotificationIntent(Integer chamadoId) {
        Intent atendimentoIntent = new Intent(context, AtendimentoActivity.class);
        atendimentoIntent.putExtra(context.getString(R.string.atendimento_id), chamadoId);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(atendimentoIntent);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setPersistenceValues(JSONObject response) {
        Persistence.setStringVal(context, context.getString(R.string.sp_notification_key), response.toString());
    }

    private JSONObject makeRequest() {
        String notificationsUrl = String.format(context.getString(R.string.api_notify), Security.getHashLogin(context));
        try {
            return JsonRequest.request(notificationsUrl);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String TAG = "AtendimentoWebSocketListener";
}
