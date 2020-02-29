package tw.tonyyang.englishwords.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;

import static tw.tonyyang.englishwords.RequestCodeStore.REQUEST_EXTERNAL_STORAGE;

/**
 * Created by tonyyang on 2017/7/5.
 */

public class PermissionManager {

    private static PermissionManager instance = null;

    public static synchronized PermissionManager getInstance() {
        // double-check locking
        if (instance == null) {
            synchronized (PermissionManager.class) {
                if (instance == null) {
                    instance = new PermissionManager();
                }
            }
        }
        return instance;
    }

    public interface PermissionCallback {
        void onPermissionGranted();

        void onShowGuide();
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @TargetApi(Build.VERSION_CODES.M)
    public void verifyStoragePermissions(Activity activity, Fragment fragment, PermissionCallback permissionCallback) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (needShowGuide(activity)) {
                permissionCallback.onShowGuide();
                return;
            }

            setFlag(activity);
            fragment.requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            permissionCallback.onPermissionGranted();
        }
    }

    private boolean getFlag(Context context) {
        return GlobalPreference.getPreference(context).getBoolean(GlobalPreference.FLAG_PERMISSIONS_STORAGE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setFlag(Activity activity) {
        boolean flag = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        GlobalPreference.getPreference(activity).put(GlobalPreference.FLAG_PERMISSIONS_STORAGE, flag);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean needShowGuide(Activity activity) {
        return getFlag(activity) && !ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
