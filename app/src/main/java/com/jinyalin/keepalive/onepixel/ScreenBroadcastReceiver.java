package com.jinyalin.keepalive.onepixel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.jinyalin.keepalive.KeepAliveManager;

/**
 * @author jinyalin
 * @since 2017/2/15.
 */

public class ScreenBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, Intent.ACTION_SCREEN_OFF)) {
            KeepAliveManager.startOnePixelActivity(context);
        } else if (TextUtils.equals(action, Intent.ACTION_SCREEN_ON)) {
            KeepAliveManager.finishOnePixelActivity(context);
        }
    }
}
