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
import android.widget.VideoView;

import java.io.File;

public class VideoViewActivity extends AppCompatActivity {

    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
    }

    public void onStreamingStartTrigger(View view) {
        String addr = MediaPlayerWrapper.prepareAddress(findViewById(R.id.editTextAddress));
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setVideoURI(Uri.parse(addr));
        mVideoView.requestFocus();
        mVideoView.start();
    }

    public void onStreamingStopTrigger(View view) {
        mVideoView.stopPlayback();
    }
}
