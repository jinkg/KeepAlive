package com.jinyalin.keepalive.guard;

import static com.jinyalin.log.LogUtil.D;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
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

    private static final int JOB_ID = 2;
    private static final long INTERVAL_WAKE_UP = 2 * 60 * 1000L;

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
        String action = WATCHDOG_ACTION;
        if (intent != null) {
            action = intent.getAction();
        }
        D(TAG, "onStartCommand action = " + action);
        if (TextUtils.equals(WATCHDOG_STOP_ACTION, action)) {
            stop();
        } else if (TextUtils.equals(WATCHDOG_ACTION, action)) {
            if (Build.VERSION.SDK_INT < 21) {
                WatchdogApi.startWatchdog(this, getClass(), WATCHDOG_ACTION);
            } else {
                JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,
                        new ComponentName(this, JobSchedulerService.class));
                builder.setPeriodic(INTERVAL_WAKE_UP);
                if (Build.VERSION.SDK_INT >= 24) {
                    builder.setPeriodic(JobInfo.getMinPeriodMillis(), JobInfo.getMinFlexMillis());
                }
                builder.setPersisted(true);
                JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                scheduler.schedule(builder.build());
            }
        }
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void stop() {
        WatchdogApi.stopWatchdog(this);
        stopSelf();
    }
}
