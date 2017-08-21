package pl.emget.sdklibrary;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by psuszek on 2015-10-29.
 */
public class WorkerService extends Service {

    static {
        //System.loadLibrary("");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
