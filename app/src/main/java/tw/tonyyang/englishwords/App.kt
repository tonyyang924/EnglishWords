package tw.tonyyang.englishwords

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import tw.tonyyang.englishwords.database.AppDatabase
import tw.tonyyang.englishwords.database.AppDatabase.Companion.getDatabase
import tw.tonyyang.englishwords.di.appModule
import tw.tonyyang.englishwords.di.databaseModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Stetho.initializeWithDefaults(this)
        startKoin {
            androidContext(this@App)
            modules(databaseModule)
            modules(appModule)
        }
        db = getDatabase(this)
    }

    companion object {
        lateinit var db: AppDatabase
        lateinit var appContext: Context
    }
}