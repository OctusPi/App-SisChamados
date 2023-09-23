package br.com.dticampossales.appsischamados.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import Utils.NotificationsUtil;
import br.com.dticampossales.appsischamados.R;
import br.com.dticampossales.appsischamados.observers.NotificationsObserver;

public class NotificationService extends Service {
    private static final String TAG = "NotificationService";
    private NotificationsObserver observer;

    private NotificationChannel atendimentoChannel;

    @Override
    public void onCreate() {
        Log.i(TAG, "Configuring...");
        atendimentoChannel = NotificationsUtil.buildNotificationChannel(
                getApplicationContext(),
                getString(R.string.channel_id),
                getString(R.string.channel_name));

        observer = new NotificationsObserver(getApplicationContext());
        observer.setInitialValues();

        HandlerThread handlerThread = new HandlerThread("NotificationServiceHandler", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                observer.bootstrap();
                handler.postDelayed(this, 2000);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service Starting");
        startForeground(startId, new Notification.Builder(this, atendimentoChannel.getId()).build());
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service finished");
    }
}
