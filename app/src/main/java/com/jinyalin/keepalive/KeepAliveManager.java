package com.jinyalin.keepalive;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.jinyalin.keepalive.foreground.ForegroundService;
import com.jinyalin.keepalive.guard.GuardService;
import com.jinyalin.keepalive.normal.NormalService;
import com.jinyalin.keepalive.onepixel.ScreenBroadcastReceiver;

import java.util.HashSet;
import java.util.Set;

/**
 * @author jinyalin
 * @since 2017/2/15.
 */

public class KeepAliveManager {

    private static final Set<Stoppable> sStoppableSet = new HashSet<>();

    private static final ScreenBroadcastReceiver sReceiver = new ScreenBroadcastReceiver();

    public static void registerStoppable(Stoppable stoppable) {
        sStoppableSet.add(stoppable);
    }

    public static void unregisterStoppable(Stoppable stoppable) {
        sStoppableSet.remove(stoppable);
    }

    public static void startNormalService(Context context) {
        context.startService(new Intent(context, NormalService.class));
    }

    public static void startForegroundService(Context context) {
        context.startService(new Intent(context, ForegroundService.class));
    }

    public static void startWatchdogService(Context context) {
        Intent intent = new Intent(GuardService.WATCHDOG_ACTION);
        ComponentName componentName = new ComponentName(context.getPackageName(),
                GuardService.class.getName());
        intent.setComponent(componentName);
        context.startService(intent);
    }

    public static void startOnePixelActivityAlive(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        context.getApplicationContext().registerReceiver(sReceiver, intentFilter);
    }

    public static void stopAllAliveService(Context context) {
        for (Stoppable stoppable : sStoppableSet) {
            stoppable.stop();
        }
        sStoppableSet.clear();

        try {
            context.getApplicationContext().unregisterReceiver(sReceiver);
        } catch (Exception ignore) {
        }
        stopWatchdogService(context);
    }
    private static void stopWatchdogService(Context context){
        Intent intent = new Intent(GuardService.WATCHDOG_STOP_ACTION);
        ComponentName componentName = new ComponentName(context.getPackageName(),
            GuardService.class.getName());
        intent.setComponent(componentName);
        context.startService(intent);
    }
}
