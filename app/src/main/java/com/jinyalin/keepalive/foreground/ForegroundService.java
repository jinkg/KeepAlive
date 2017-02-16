package com.jinyalin.keepalive.foreground;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jinyalin.keepalive.KeepAliveManager;
import com.jinyalin.keepalive.Stoppable;

/**
 * @author jinyalin
 * @since 2017/2/14.
 */

public class ForegroundService extends Service implements Stoppable {
    private static final int NOTIFY_ID = 8888;

    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = new Notification();
        startForeground(NOTIFY_ID, notification);

        startService(new Intent(this, InnerService.class));

        KeepAliveManager.registerStoppable(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void stop() {
        stopSelf();
    }

    public static class InnerService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
            Notification notification = new Notification();
            startForeground(NOTIFY_ID, notification);

            stopSelf();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            stopForeground(true);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
