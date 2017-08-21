package pl.emget.assetproviderapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;

import pl.emget.assetproviderapp.content.ContentManager;
import pl.emget.assetproviderapp.permissions.PermissionsManager;
import pl.emget.assetproviderapp.volley.VolleyDownloadManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String INTERNAL_FILE_NAME = "sample_image.jpg";
    public static final String INTERNAL_FILE_PARENT_DIR = "private_images";
    public static final String EXTERNAL_FILE_PARENT_DIR_PATH = Environment.getExternalStorageDirectory() + File
            .separator + "FLIR" + File.separator + "TestFileProvider";

    private static final String ABS_PATH_TO_THERMAL_IMAGE = Environment.getExternalStorageDirectory() + File
            .separator + "FLIR" + File.separator + "FLIR0001.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionsManager.verifyStoragePermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * After this call the sample text file is downloaded both to internal storage and external storage.
     *
     * @param view
     */
    public void onRequestDownload(View view) {
        VolleyDownloadManager downloadManager = new VolleyDownloadManager(this);
        downloadManager.requestDataMethod1();
        //downloadManager.requestDataMethod2();
    }

    /**
     * After this call the simulated IR file is copied to internal storage.
     *
     * @param view
     */
    public void onPrepareFiles(View view) {
        prepareSimulatedInternalFiles();
    }

    /**
     * Once triggered it sends the internal file in the intent.
     *
     * @param view
     */
    public void onShare(View view) {
        // intent with IR jpeg file
        // triggerSendingIntent();
        // intent with sample text file
        triggerSendingIntentTestOnly();
    }

    /**
     * Copies the IR file to app internal storage.
     */
    public void prepareSimulatedInternalFiles() {
        File flirIrImageOnExternalStorage = new File(ABS_PATH_TO_THERMAL_IMAGE);
        File imagesInternalDir = new File(getFilesDir(), INTERNAL_FILE_PARENT_DIR);
        File internalFile = new File(imagesInternalDir, INTERNAL_FILE_NAME);
        if (!internalFile.exists()) {
            ContentManager.copyFile(flirIrImageOnExternalStorage.getAbsolutePath(), internalFile.getAbsolutePath());
        } else {
            Log.d(TAG, "File exists: " + internalFile.getAbsolutePath());
        }
        // read the file content
        ContentManager.readFileContent(new File(imagesInternalDir, VolleyDownloadManager.VOLLEY_FILE));
    }

    /**
     * Will use actual IR file.
     */
    private void triggerSendingIntent() {
        // read the internal file
        File imagePath = new File(getFilesDir(), INTERNAL_FILE_PARENT_DIR);
        File fileToShare = new File(imagePath, INTERNAL_FILE_NAME);
        // use a FileProvider to construct the URI for the internal file
        Uri contentUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider",
                fileToShare);

        Log.d(TAG, contentUri.toString());
        Log.d(TAG, contentUri.getPath());

        Intent intent = new Intent();
        // this is handled by Flir Tools app
        intent.setAction("com.flir.viewer.EDIT_FLIR_IMAGE");
        // the contentUri points to the internal file in AssetProviderApp private app space
        intent.setDataAndTypeAndNormalize(contentUri, ContentManager.getMimeType(contentUri.toString()));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // give read permission
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION); // and give write permission

        // not needed, because we set flags in the intent
        //        this.grantUriPermission("com.flir.viewer", contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent
        //                .FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(intent);
    }

    /**
     * Will use sample text file.
     */
    private void triggerSendingIntentTestOnly() {
        // read the internal file
        File imagePath = new File(getFilesDir(), INTERNAL_FILE_PARENT_DIR);
        File fileToShare = new File(imagePath, VolleyDownloadManager.VOLLEY_FILE);
        // use a FileProvider to construct the URI
        Uri contentUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider",
                fileToShare);

        Log.d(TAG, contentUri.toString());
        Log.d(TAG, contentUri.getPath());

        Intent intent = new Intent();
        //intent.setAction(Intent.ACTION_EDIT);
        // this is handled by my test app
        intent.setAction("com.flir.viewer.EDIT_FLIR_TEXT");
        // the contentUri points to the internal file in AssetProviderApp private app space
        intent.setDataAndTypeAndNormalize(contentUri, ContentManager.getMimeType(contentUri.toString()));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // give read permission
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION); // and give write permission

        startActivity(intent);
    }

}
