package com.jinyalin.keepalive.guard;

import static com.jinyalin.log.LogUtil.D;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import android.text.TextUtils;
import com.jinyalin.keepalive.KeepAliveManager;
import com.jinyalin.keepalive.Stoppable;
import com.jinyalin.log.LogUtil;
import com.yalin.watchdog.WatchdogApi;

/**
 * @author jinyalin
 * @since 2017/2/14.
 */

public class GuardService extends Service implements Stoppable {

    private static final String TAG = "GuardService";

    public static final String WATCHDOG_ACTION = "com.yalin.ACTION.WATCHDOG";
    public static final String WATCHDOG_STOP_ACTION = "com.yalin.ACTION.STOP_WATCHDOG";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        D(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        D(TAG, "onStartCommand action = " + action);
        if (TextUtils.equals(WATCHDOG_STOP_ACTION, action)){
            stop();
        } else if(TextUtils.equals(WATCHDOG_ACTION, action)){
            WatchdogApi.startWatchdog(this, getClass(), WATCHDOG_ACTION);
        }
        return super.onStartCommand(intent, flags, startId);
//        return START_NOT_STICKY;
    }

    @Override
    public void stop() {
        WatchdogApi.stopWatchdog(this);
        stopSelf();
    }
}
