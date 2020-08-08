package com.turelo.itunesbrowsersample

import android.app.Application
import com.turelo.itunesbrowsersample.di.appModule
import com.turelo.itunesbrowsersample.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import org.koin.core.logger.Level

class ITunesBrowserApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initTimber()
        initDI()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initDI() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@ITunesBrowserApp)
            modules(
                listOf(
                    appModule,
                    viewModelModule
                )
            )
        }
    }
}