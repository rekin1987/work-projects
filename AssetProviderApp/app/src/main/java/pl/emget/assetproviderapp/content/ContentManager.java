package pl.emget.assetproviderapp.content;

import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class with helper methods related to content management.
 */
public class ContentManager {

    private static final String TAG = ContentManager.class.getSimpleName();

    /**
     * Reads out the file and prints it's content to Logcat.
     *
     * @param file a file to be read
     */
    public static void readFileContent(File file) {
        Log.d(TAG, "readFileContent(): " + file.getAbsolutePath());
        try {
            BufferedReader r = new BufferedReader(new FileReader(file));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            Log.d(TAG, "TOTAL: " + total);
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copies a file from source to destination.
     *
     * @param src  source file absolute path
     * @param dest destination file absolute path
     */
    public static void copyFile(String src, String dest) {
        Log.d(TAG, "copyFile(): SRC=" + src + ", DEST=" + dest);
        File sourceFile = new File(src);
        File destinationFile = new File(dest);

        try {
            InputStream is = new FileInputStream(sourceFile);
            OutputStream os = new FileOutputStream(destinationFile);
            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) > 0) {
                os.write(buff, 0, len);
            }
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Determine the MIME type from the URI.
     */
    public static String getMimeType(String uri) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(uri);
        String mime = null;
        if (ext != null) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return mime;
    }

}
