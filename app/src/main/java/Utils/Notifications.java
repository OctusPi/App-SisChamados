package Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import br.com.dticampossales.appsischamados.R;

public class Notifications {

    public Notification createNotification(Context context, JSONObject chamado) {
        String setor = JsonUtil.getJsonVal(chamado, "setor");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "")
                .setSmallIcon(R.drawable.baseline_build_24)
                .setContentTitle(JsonUtil.getJsonVal(chamado, "setor"))
                .setContentText(JsonUtil.getJsonVal(chamado, "descricao"))
                .setPriority(getPriority(Integer.parseInt(setor)));

        return builder.build();
    }

    private int getPriority(int setor) {
        final int SETOR_HOSPITAL = 1;
        if (setor == SETOR_HOSPITAL) {
            return NotificationCompat.PRIORITY_HIGH;
        }
        return NotificationCompat.PRIORITY_DEFAULT;
    }

    private NotificationChannel createNotificationChannel(Context context, String channelId) {
        CharSequence name = "Chamados Channel";
        String description = "Channel for chamados";

        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(channelId, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        return channel;
    }
}
