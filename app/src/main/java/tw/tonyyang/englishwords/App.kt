package tw.tonyyang.englishwords

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.google.firebase.analytics.FirebaseAnalytics
import io.fabric.sdk.android.Fabric
import tw.tonyyang.englishwords.database.AppDatabase
import tw.tonyyang.englishwords.database.AppDatabase.Companion.getDatabase

class App : Application() {

    companion object {
        @JvmStatic
        var db: AppDatabase? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)
        FirebaseAnalytics.getInstance(this)
        Stetho.initializeWithDefaults(this)
        db = getDatabase(this)
    }
}