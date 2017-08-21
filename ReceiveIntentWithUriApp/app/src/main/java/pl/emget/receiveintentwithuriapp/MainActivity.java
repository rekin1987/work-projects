package pl.emget.receiveintentwithuriapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Uri mProvidedUri;
    private TextView mStatusLabel;
    private TextView mContentLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent() != null && getIntent().getData() != null) {
            mProvidedUri = getIntent().getData();
            Log.d(TAG, "ReceivedURI <toString>= " + mProvidedUri.toString());
            Log.d(TAG, "ReceivedURI <getPath>= " + mProvidedUri.getPath());
            mStatusLabel = (TextView) findViewById(R.id.statusText);
            mContentLabel = (TextView) findViewById(R.id.contentText);

            mStatusLabel.setText(mProvidedUri.toString());
        }
    }

    public void onReadFromUri(View view) {
        readFromUri(mProvidedUri);
    }

    public void onWriteToUri(View view) {
        writeToUri(mProvidedUri);
    }

    private void readFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder wholeText = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                wholeText.append(line).append('\n');
            }
            Log.d(TAG, "readFromInput whole text = " + wholeText);
            mContentLabel.setText(wholeText);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToUri(Uri uri) {
        try {
            String writeMode = "w"; // overwrite any previous content
            OutputStream output = getContentResolver().openOutputStream(uri, writeMode);
            String line1 = "First line written at: " + new Date(System.currentTimeMillis()).toString() + "\n";
            output.write(line1.getBytes());
            output.write("Second line added\n".getBytes());
            output.close();
            Toast.makeText(this, "New content written to file.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
