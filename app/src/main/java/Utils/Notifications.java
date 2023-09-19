package Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import br.com.dticampossales.appsischamados.R;

public class Notifications {
    public static NotificationCompat.Builder makeNotificationBuilder(Context context, String notificationChannelId) {
        return new NotificationCompat.Builder(context, notificationChannelId)
                .setSmallIcon(R.drawable.logo);
    }

    public static NotificationChannel buildNotificationChannel(Context context, String channelId, String channelName) {
        NotificationChannel channel = new NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        return channel;
    }
}
