package tw.tonyyang.englishwords;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.androidannotations.annotations.EApplication;

import io.fabric.sdk.android.Fabric;
import tw.tonyyang.englishwords.database.AppDatabase;

/**
 * Created by tonyyang on 2017/5/15.
 */

@EApplication
public class App extends Application {

    private static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        FirebaseAnalytics.getInstance(this);
        Stetho.initializeWithDefaults(this);
        db = AppDatabase.getAppDatabase(this);
    }

    public static AppDatabase getDb() {
        return db;
    }
}
