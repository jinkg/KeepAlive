package com.jinyalin.keepalive.keepalivedemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jinyalin.keepalive.KeepAliveManager;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keep_alive_activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
