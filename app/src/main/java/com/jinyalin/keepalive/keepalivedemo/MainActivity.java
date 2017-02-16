package com.jinyalin.keepalive.keepalivedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jinyalin.keepalive.KeepAliveManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startNormalService(View view) {
        KeepAliveManager.startNormalService(this);
    }

    public void startForegroundService(View view) {
        KeepAliveManager.startForegroundService(this);
    }

    public void startGuard(View view) {
        KeepAliveManager.startWatchdogService(this);
    }

    public void startOnePixelActivity(View view) {
        KeepAliveManager.startOnePixelActivityAlive(this);
    }

    public void stopAlive(View view) {
        KeepAliveManager.stopAllAliveService(this);
    }

}
