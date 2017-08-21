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

public class NativePlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_player);
    }

    public void onStreamingStartTrigger(View view) {
        String addr = MediaPlayerWrapper.prepareAddress(findViewById(R.id.editTextAddress));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(addr));
        startActivity(intent);
    }

}
