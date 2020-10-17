package tw.tonyyang.englishwords

import android.app.Application
import com.facebook.stetho.Stetho
import tw.tonyyang.englishwords.database.AppDatabase
import tw.tonyyang.englishwords.database.AppDatabase.Companion.getDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        db = getDatabase(this)
    }

    companion object {
        @JvmStatic
        var db: AppDatabase? = null
            private set
    }
}