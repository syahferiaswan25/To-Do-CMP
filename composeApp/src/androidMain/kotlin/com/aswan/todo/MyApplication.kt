package com.aswan.todo

import android.app.Application
import com.aswan.todo.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initializeKoin(
            config = {androidContext(this@MyApplication) }
        )
    }
}