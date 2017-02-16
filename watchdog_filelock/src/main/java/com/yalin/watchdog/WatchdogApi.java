package com.yalin.watchdog;

import android.content.Context;

import java.io.File;

/**
 * @author jinyalin
 * @since 2017/2/16.
 */

public class WatchdogApi {
    public static void startWatchdog(Context context, Class serviceClazz, String action) {
        String result = NativeUtil.runExecutable(context,
                context.getPackageName() + File.separator + serviceClazz.getName(),
                action, 20);
        System.out.println("startWatchdog result: " + result);
    }

    public static void stopWatchdog(Context context) {
        NativeUtil.clearUnusedFileLocks(context, null);
    }
}
