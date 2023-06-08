package com.swalif

import android.app.Application
import androidx.compose.runtime.collectAsState
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.flow
import logcat.AndroidLogcatLogger
import logcat.logcat

@HiltAndroidApp
class SwalifApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this)
        MobileAds.initialize(this)

    }
}