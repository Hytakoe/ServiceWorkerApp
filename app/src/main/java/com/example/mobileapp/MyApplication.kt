package com.example.mobileapp

import android.app.Application
import com.example.mobileapp.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(AppModule().appModule)
        }
    }
}