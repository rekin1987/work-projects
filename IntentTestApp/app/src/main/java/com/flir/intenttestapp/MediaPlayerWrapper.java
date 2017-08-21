package com.flir.intenttestapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.EditText;

/**
 * Created by psuszek on 2017-01-19.
 */

public class MediaPlayerWrapper {

    private static final String STREAM_ADDRESS = "rtsp://192.168.%s/mpeg4/";
    private static final String TAG = MediaPlayerWrapper.class.getSimpleName();

    private Context mContext;
    private MediaPlayer mMediaPlayer;

    public MediaPlayerWrapper(Context context) {
        mContext = context;
    }

    public static String prepareAddress(View anEditText){
        return String.format(STREAM_ADDRESS, ((EditText)anEditText).getText().toString());
    }

    public void start(String address, SurfaceHolder holder) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnInfoListener(mPlayerInfoListener);
        mMediaPlayer.setOnPreparedListener(mPlayerPreparedListener);
        mMediaPlayer.setOnErrorListener(mPlayerErrorListener);

        try {
            String dataSource = address;
            mMediaPlayer.setDataSource(dataSource);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepareAsync();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
//        mSurfaceRecorderTest = new SurfaceRecorderTest();
//        mSurfaceRecorderTest.testEncodeVideoToMp4(holder.getSurface());
//        mSurfaceRecorderTest.rec();
    }

//    private SurfaceRecorderTest mSurfaceRecorderTest;

    private final MediaPlayer.OnInfoListener mPlayerInfoListener = new MediaPlayer.OnInfoListener() {

        @Override
        public boolean onInfo(final MediaPlayer mp, final int what, final int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    Log.v(TAG, ">>> buffering data");
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    Log.v(TAG, "<<< buffering finished");
                    break;
            }
            return false;
        }
    };

    private final MediaPlayer.OnPreparedListener mPlayerPreparedListener = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(final MediaPlayer mp) {
            Log.d(TAG, "onPrepared()");
            mp.start();
//            mSurfaceRecorderTest.rec();
        }
    };

    private final MediaPlayer.OnErrorListener mPlayerErrorListener = new MediaPlayer.OnErrorListener() {

        @SuppressLint("DefaultLocale")
        @Override
        public boolean onError(final MediaPlayer mp, final int what, final int extra) {
            Log.d(TAG, "onError( what = " + what + " , extra = " + extra + " )");
            return false;
        }
    };

    public void stop() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }
}
