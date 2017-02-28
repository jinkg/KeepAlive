package com.jinyalin.keepalive.guard;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;


import com.jinyalin.keepalive.KeepAliveManager;

import static com.jinyalin.log.LogUtil.F;

/**
 * @author jinyalin
 * @since 2017/2/28.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {
    private static final String TAG = "JobSchedulerService";

    @Override
    public boolean onStartJob(JobParameters params) {
        F(TAG, "onStartJob JobId = " + params.getJobId());
        KeepAliveManager.startWatchdogService(this);
        jobFinished(params, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
