package com.flir.intenttestapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import java.io.File;

public class SurfaceViewActivity extends AppCompatActivity {

    private MediaPlayerWrapper mMediaPlayerWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view);
    }

    public void onStreamingStartTrigger(View view) {
        String addr = MediaPlayerWrapper.prepareAddress(findViewById(R.id.editTextAddress));
        SurfaceHolder holder = ((SurfaceView)findViewById(R.id.streamHolder)).getHolder();
        mMediaPlayerWrapper = new MediaPlayerWrapper(this);
        mMediaPlayerWrapper.start(addr, holder);
    }

    public void onStreamingStopTrigger(View view) {
        mMediaPlayerWrapper.stop();
    }
}
