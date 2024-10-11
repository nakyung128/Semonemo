package com.ssafy.semonemo.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SemoNemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
