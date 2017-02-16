package com.jinyalin.keepalive.normal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jinyalin.keepalive.KeepAliveManager;
import com.jinyalin.keepalive.Stoppable;

/**
 * @author jinyalin
 * @since 2017/2/15.
 */

public class NormalService extends Service implements Stoppable {

    @Override
    public void onCreate() {
        super.onCreate();
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
}
