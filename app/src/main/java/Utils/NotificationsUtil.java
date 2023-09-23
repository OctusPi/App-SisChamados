package Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import br.com.dticampossales.appsischamados.R;

public class NotificationsUtil {
    public static void notify(Context context, int notificationId, Notification notification) {
        getNotificationManager(context).notify(notificationId, notification);
    }
    public static NotificationCompat.Builder makeNotificationBuilder(Context context, String notificationChannelId) {
        return new NotificationCompat.Builder(context, notificationChannelId)
                .setSmallIcon(R.drawable.logo);
    }

    public static NotificationChannel getNotificationChannel(Context context, String channelId) {
        return getNotificationManager(context).getNotificationChannel(channelId);
    }

    public static NotificationChannel buildNotificationChannel(Context context, String channelId, String channelName) {
        NotificationChannel channel = new NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT);

        getNotificationManager(context).createNotificationChannel(channel);

        return channel;
    }

    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
