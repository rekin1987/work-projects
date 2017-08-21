package pl.emget.permissionscheck;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;

/**
 * A helper class for permissions handling on Android 6.0.
 *
 * @author Pawel Suszek
 */
public class PermissionManager {

    private static PermissionManager mInstance;


    /**
     * Describes the permissions supported by the {@link PermissionManager}.
     */
    public enum PermissionType {
        /**
         * Permission for accessing device storage (READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE).
         * This is required for:
         * 1. Saving captured photos, videos, etc.
         * 2. Browsing app library (photos, videos, etc.)
         * 3. Handling dongle configuration file (allowing to show MSX image)
         */
        STORAGE(1),
        /**
         * Permission for camera (CAMERA).
         * This is required to check the camera state and flash status and allow to turn it on in the app.
         * Valid for Flir One only.
         */
        CAMERA(2),
        /**
         * Permission for microphone (RECORD_AUDIO).
         * This is required for recording a voice annotation for a snapshot.
         */
        MICROPHONE(3);

        private int mRequestCode;

        PermissionType(int requestCode) {
            mRequestCode = requestCode;
        }

        /**
         * Every enum has associated list of Manifest permissions.
         *
         * @return Returns an array of Strings with Manifest permissions for the specific functionality.
         */
        public String[] getPermissionStrings() {
            switch (this) {
                case STORAGE:
                    return new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    };
                case CAMERA:
                    return new String[]{
                            Manifest.permission.CAMERA
                    };
                case MICROPHONE:
                    return new String[]{
                            Manifest.permission.RECORD_AUDIO
                    };
                default:
                    return null;
            }
        }

        /**
         * Every enum requires a request code for permission request.
         *
         * @return Returns an internal request code required to identity the permission request type correctly.
         */
        public int getRequestCode() {
            return this.mRequestCode;
        }

        /**
         * Gets a type of permission basing on a request code.
         *
         * @param requestCode a request code as int value
         * @return Returns one of the {@link PermissionType} types or null if the internal request code is not correct.
         */
        public static PermissionType getPermissionType(int requestCode) {
            PermissionType[] values = PermissionType.values();
            for (int i = 0; i < values.length; ++i) {
                if (values[i].mRequestCode == requestCode) {
                    return values[i];
                }
            }
            return null;
        }
    }

    private PermissionManager() {
        // singleton
    }

    /**
     * Gets a {@link PermissionManager} singleton.
     *
     * @return Returns a {@link PermissionManager} singleton.
     */
    public static PermissionManager getInstance() {
        if (mInstance == null) {
            mInstance = new PermissionManager();
        }
        return mInstance;
    }


    /**
     * Method used to trigger the native Android 6.0 permission request dialog with the requested permission type.
     * Note: the request result is passed to the Activity (see activity parameter) in a {@link ActivityCompat} class protected method:
     * onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults)
     *
     * @param permissionType a type of the requested permission, one of {@link com.flir.viewer.manager.PermissionManager.PermissionType}
     * @param activity       an Activity which will collect the dialog result (i.e. permission granted event) using onRequestPermissionsResult(...) method
     */
    public void requestPermission(PermissionType permissionType, Activity activity) {
        ActivityCompat.requestPermissions(activity,
                permissionType.getPermissionStrings(),
                permissionType.getRequestCode());
    }

    /**
     * Checks whether a specific permission is already granted. There are two scenarios where permission is not required (meaning is granted):
     * 1. When specific permission is already granted by the previous request
     * 2. When the Android OS version is below 6.0
     *
     * @param permissionType a type of requested permission, one of {@link PermissionType}
     * @param context        app context
     * @return Returns true if the specific permission is required (not granted), otherwise returns false (when permission is already granted).
     */
    public boolean isPermissionGranted(PermissionType permissionType, Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            // in API < 23 (means before Android 6.0) permissions are acquired on app installation, so they are always granted
            return true;
        } else {
            String[] permissions = permissionType.getPermissionStrings();
            for (int i = 0; i < permissions.length; ++i) {
                if (PermissionChecker.checkSelfPermission(context,
                        permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    // if any permission is not yet granted it will be required, so return true
                    return false;
                }
            }
            // each Manifest permission for the requested functionality is already granted - thus permission is not required
            return true;
        }
    }

    /**
     * Looks for any missing permission from {@link com.flir.viewer.manager.PermissionManager.PermissionType} elements.
     * It looks in an order of permissions importance as defined in {@link com.flir.viewer.manager.PermissionManager.PermissionType} - lower the request code, higher the importance.
     *
     * @param context app context
     * @return Returns the next missing permission (ordered by importance) as {@link com.flir.viewer.manager.PermissionManager.PermissionType} item.
     */
    public PermissionType getNextMissingPermission(Context context) {
        PermissionType[] values = PermissionType.values();
        // check all defined permissions in order
        for (int i = 0; i < values.length; ++i) {
            if (!isPermissionGranted(values[i], context)) {
                return values[i];
            }
        }
        return null;
    }

}
