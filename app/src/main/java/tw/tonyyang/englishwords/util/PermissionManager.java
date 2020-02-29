package tw.tonyyang.englishwords.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import org.androidannotations.annotations.EBean;

import static org.androidannotations.annotations.EBean.Scope.Singleton;
import static tw.tonyyang.englishwords.RequestCodeStore.REQUEST_EXTERNAL_STORAGE;

/**
 * Created by tonyyang on 2017/7/5.
 */

@EBean(scope = Singleton)
public class PermissionManager {

    public interface PermissionCallback {
        void onPermissionGranted();

        void onShowGuide();
    }

    private Context context;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    PermissionManager(Context context) {
        this.context = context;
    }

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

    private boolean getFlag() {
        return GlobalPreference.getPreference(context).getBoolean(GlobalPreference.FLAG_PERMISSIONS_STORAGE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setFlag(Activity activity) {
        boolean flag = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        GlobalPreference.getPreference(context).put(GlobalPreference.FLAG_PERMISSIONS_STORAGE, flag);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean needShowGuide(Activity activity) {
        return getFlag() && !ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
