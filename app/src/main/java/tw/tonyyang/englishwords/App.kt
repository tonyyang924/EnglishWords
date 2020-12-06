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
        lateinit var db: AppDatabase
        lateinit var appContext: Context
    }
}