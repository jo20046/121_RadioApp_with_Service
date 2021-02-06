package de.jo20046.a12_1_radioapp_with_service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

public class RadioActivity extends Activity {

    WebView webView;
    RadioService radioService;
    boolean serviceBound = false;
    String tag = "mytag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(getIntent().getExtras().getString("URL"));
    }

    @Override
    protected void onStart() {
        Log.d(tag, "onStart() - Activity");
        super.onStart();
        Intent intent = new Intent(this, RadioService.class);
        intent.putExtra("Stream", getIntent().getExtras().getString("Stream"));
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, RadioService.class);
        stopService(intent);
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(tag, "onServiceConnected() - Activity");
            RadioService.MyBinder myBinder = (RadioService.MyBinder) service;
            radioService = myBinder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };
}

