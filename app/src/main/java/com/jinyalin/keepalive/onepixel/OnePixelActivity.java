package com.jinyalin.keepalive.onepixel;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.jinyalin.keepalive.KeepAliveManager;
import com.jinyalin.keepalive.Stoppable;

/**
 * @author jinyalin
 * @since 2017/2/15.
 */

public class OnePixelActivity extends Activity implements Stoppable {
    private static final String TAG = "OnePixelActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.width = 1;
        params.height = 1;
        window.setAttributes(params);

        KeepAliveManager.setOnePixelActivity(this);
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop: ");
        finish();
        KeepAliveManager.setOnePixelActivity(null);
    }
}
