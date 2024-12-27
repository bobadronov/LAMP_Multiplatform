package org.bigblackowl.lamp.app;

import android.app.Application
import org.bigblackowl.lamp.di.initKoin
import org.koin.android.ext.koin.androidContext

class LAMPApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@LAMPApplication)
        }
    }
}