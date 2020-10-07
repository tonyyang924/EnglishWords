package tw.tonyyang.englishwords

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import tw.tonyyang.englishwords.database.AppDatabase
import tw.tonyyang.englishwords.database.AppDatabase.Companion.getDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Stetho.initializeWithDefaults(this)
        db = getDatabase(this)
    }

    companion object {
        var db: AppDatabase? = null
            private set

        lateinit var appContext: Context
    }
}