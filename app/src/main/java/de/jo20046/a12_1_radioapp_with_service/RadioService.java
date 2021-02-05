package de.jo20046.a12_1_radioapp_with_service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class RadioService extends Service {

    private final IBinder myBinder = new MyBinder();
    private MediaPlayer mp;
    private String stream;
    String tag = "mytag";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(tag, "onStartCommand() - Service");
        stream = intent.getExtras().getString("Stream");
        startMediaPlayer();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(tag, "onCreate() - Service");
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(tag, "onBind() - Service");
        return myBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMediaPlayer();
    }

    public void startMediaPlayer() {
        stopMediaPlayer();
        mp = new MediaPlayer();
        try {
            mp.setDataSource(stream);

            // Version "blocking"
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stopMediaPlayer() {
        if (mp != null) mp.release();
    }

    public class MyBinder extends Binder {
        RadioService getService() {
            Log.d(tag, "getService() - Binder");
            return RadioService.this;
        }
    }
}
