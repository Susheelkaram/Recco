package com.susheelkaram

import android.app.Application
import appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by Susheel Kumar Karam
 * Website - SusheelKaram.com
 */
class ReccoApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ReccoApp)
            modules(appModule)
        }
    }
}