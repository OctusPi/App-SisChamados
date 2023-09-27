package br.com.dticampossales.appsischamados.workers;

import android.app.NotificationChannel;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import Utils.NotificationsUtil;
import br.com.dticampossales.appsischamados.R;
import br.com.dticampossales.appsischamados.observers.NotificationsObserver;

public class NotificationWorker extends Worker {
    private static final String TAG = "NotificationWorker";
    private final NotificationsObserver observer;
    private NotificationChannel atendimentoChannel;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);

        atendimentoChannel = NotificationsUtil.buildNotificationChannel(
                getApplicationContext(),
                getApplicationContext().getString(R.string.channel_id),
                getApplicationContext().getString(R.string.channel_name));

        observer = new NotificationsObserver(getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {
        observer.bootstrap();
        return Result.success();
    }
}
