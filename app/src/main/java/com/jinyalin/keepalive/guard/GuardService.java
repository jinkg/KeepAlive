package com.jinyalin.keepalive.guard;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jinyalin.keepalive.KeepAliveManager;
import com.jinyalin.keepalive.Stoppable;
import com.yalin.watchdog.WatchdogApi;

/**
 * @author jinyalin
 * @since 2017/2/14.
 */

public class GuardService extends Service implements Stoppable {
    public static final String WATCHDOG_ACTION = "com.yalin.ACTION.WATCHDOG";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        KeepAliveManager.registerStoppable(this);

        WatchdogApi.startWatchdog(this, getClass(), WATCHDOG_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void stop() {
        WatchdogApi.stopWatchdog(this);
        stopSelf();
    }
}
