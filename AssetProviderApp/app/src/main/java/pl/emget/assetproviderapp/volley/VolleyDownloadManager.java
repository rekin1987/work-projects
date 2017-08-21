package pl.emget.assetproviderapp.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;

import pl.emget.assetproviderapp.MainActivity;

/**
 * Class used for data download from web. Here we only download a single file.
 */
public class VolleyDownloadManager {

    private static final String TAG = VolleyDownloadManager.class.getSimpleName();

    private static final String WEB_FILE_URL = "http://emget.pl/tutorials/windows_tips.txt";
    public static final String VOLLEY_FILE = "volley_file.txt";

    private Context mContext;

    public VolleyDownloadManager(Context context) {
        mContext = context;
    }

    /**
     * Method uses default Volley StringRequest object to perform file download.
     */
    public void requestDataMethod1() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WEB_FILE_URL, new Response
                .Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Log.d(TAG, "received successful response");
                    // store to external storage
                    writeBytesToFile(MainActivity.EXTERNAL_FILE_PARENT_DIR_PATH, VOLLEY_FILE, response.getBytes());
                    // store to internal storage
                    writeBytesToFile(mContext.getFilesDir().getAbsolutePath() + File.separator + MainActivity
                            .INTERNAL_FILE_PARENT_DIR, VOLLEY_FILE, response.getBytes());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "That didn't work!");
                error.printStackTrace();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(stringRequest);
    }

    /**
     * Method uses custom Volley request to perform file download.
     */
    public void requestDataMethod2() {
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, WEB_FILE_URL, new
                Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                if (response != null) {
                    // store to external storage
                    writeBytesToFile(MainActivity.EXTERNAL_FILE_PARENT_DIR_PATH, VOLLEY_FILE, response);
                    // store to internal storage
                    writeBytesToFile(mContext.getFilesDir().getAbsolutePath() + File.separator + MainActivity
                            .INTERNAL_FILE_PARENT_DIR, VOLLEY_FILE, response);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "That didn't work!");
                error.printStackTrace();
            }
        }, null);

        RequestQueue queue = Volley.newRequestQueue(mContext, new HurlStack());
        queue.add(request);
    }

    /**
     * Writes data to an output file.
     *
     * @param parentDir directory where the output file will be stored
     * @param filename  name of the output file
     * @param data      actual data which will be stored within the file
     */
    private void writeBytesToFile(String parentDir, String filename, byte[] data) {
        File dir = new File(parentDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(parentDir, filename);
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
