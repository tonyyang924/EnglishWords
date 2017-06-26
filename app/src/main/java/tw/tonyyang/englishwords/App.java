package tw.tonyyang.englishwords;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import org.androidannotations.annotations.EApplication;

import io.fabric.sdk.android.Fabric;

/**
 * Created by tonyyang on 2017/5/15.
 */

@EApplication
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
    }
}
