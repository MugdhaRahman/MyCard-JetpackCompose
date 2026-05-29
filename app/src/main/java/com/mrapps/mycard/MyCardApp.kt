package com.mrapps.mycard

import android.app.Application
import com.mrapps.mycard.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyCardApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyCardApp)
            modules(appModule)
        }
    }
}
