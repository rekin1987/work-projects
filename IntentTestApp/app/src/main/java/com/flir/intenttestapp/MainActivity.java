package com.flir.intenttestapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onIntentTrigger(View view) {
        // triggered when button is clicked
        triggerSendingIntent();
        //triggerSendingIntentForTools(this, "com.flir.viewer");
    }

    private void triggerSendingIntent() {
        // TODO make sure this file exists on your device
        final String ABS_PATH_TO_THERMAL_IMAGE = Environment.getExternalStorageDirectory() + File.separator + "FLIR" +
                File.separator + "FLIR0001.jpg";
        File file = new File(ABS_PATH_TO_THERMAL_IMAGE);
        //File file = getSampleInternalFile();

        // Get URI and MIME type of file
        Uri uri = Uri.fromFile(file).normalizeScheme();
        String mime = getMimeType(uri.toString());
        Log.d(TAG, "URI=" + uri.toString());
        Log.d(TAG, "mime=" + mime);

        // Open file with user selected app
        Intent intent = new Intent();
        //intent.setAction(Intent.ACTION_VIEW);
        intent.setAction("com.flir.viewer.EDIT_FLIR_IMAGE");
        intent.setDataAndTypeAndNormalize(uri, mime);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // set a specific destination app
        //intent.setClassName("com.flir.viewer","com.flir.viewer.MainActivity");
        startActivity(intent);
    }

    private String getMimeType(String url) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(url);
        String mime = null;
        if (ext != null) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return mime;
    }

    private File getSampleInternalFile() {
        String filename = "myfile_priv1.txt";
        File file = new File(getFilesDir(), filename);
        if(file.exists()){
            return file;
        }

        String string = "Hello world 555!";
        FileOutputStream outputStream;

        try {
            // private file won't be available when provided in an intent
            // MODE_WORLD_READABLE is deprecated and throws exception since Android 7.0
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

//    private void triggerSendingIntentForTools(Context context, String packageName){
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setClassName("com.facebook.katana",
//                "com.facebook.katana.ShareLinkActivity");
//        sharingIntent.putExtra(Intent.EXTRA_TEXT, "your title text");
//        startActivity(sharingIntent);
//
//
//            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
//            if (intent == null) {
//                // Bring user to the market or let them choose an app?
//                intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("market://details?id=" + packageName));
//            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//    }

    public void onStreamingSurfaceViewTrigger(View view) {
        startActivity(new Intent(this, SurfaceViewActivity.class));
    }

    public void onStreamingVideoViewTrigger(View view) {
        startActivity(new Intent(this, VideoViewActivity.class));
    }

    public void onStreamingNativePlayerTrigger(View view) {
        startActivity(new Intent(this, NativePlayerActivity.class));
    }

    public void onTestActivityTrigger(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }
}
