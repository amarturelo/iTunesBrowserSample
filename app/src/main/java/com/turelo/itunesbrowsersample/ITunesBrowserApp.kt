package com.turelo.itunesbrowsersample

import android.app.Application
import timber.log.Timber

class ITunesBrowserApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}