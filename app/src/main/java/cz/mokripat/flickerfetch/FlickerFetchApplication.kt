package cz.mokripat.flickerfetch

import android.app.Application
import cz.mokripat.flickerfetch.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Main application class.
 * Initializes Koin dependency injection.
 */
class FlickerFetchApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@FlickerFetchApplication)
            modules(appModule)
        }
    }
}