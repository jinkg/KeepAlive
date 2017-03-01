package com.jinyalin.keepalive.onepixel;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.jinyalin.keepalive.KeepAliveManager;
import com.jinyalin.keepalive.Stoppable;

/**
 * @author jinyalin
 * @since 2017/2/15.
 */

public class ScreenBroadcastReceiver extends BroadcastReceiver implements Stoppable {
    private static Stoppable sOnePixelActivity;

    public ScreenBroadcastReceiver() {
        KeepAliveManager.registerStoppable(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, Intent.ACTION_SCREEN_OFF)) {
            startOnePixelActivity(context);
        } else if (TextUtils.equals(action, Intent.ACTION_SCREEN_ON)) {
            finishOnePixelActivity();
        }
    }

    private static void startOnePixelActivity(Context context) {
        Intent activityIntent = new Intent(context, OnePixelActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(activityIntent);
    }

    private static void finishOnePixelActivity() {
        if (sOnePixelActivity != null) {
            sOnePixelActivity.stop();
            sOnePixelActivity = null;
        }
    }

    public static void setOnePixelActivity(Stoppable onePixelActivity) {
        sOnePixelActivity = onePixelActivity;
    }

    @Override
    public void stop() {
        finishOnePixelActivity();
    }
}
