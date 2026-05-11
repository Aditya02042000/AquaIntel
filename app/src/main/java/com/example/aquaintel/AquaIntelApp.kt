package com.example.aquaintel

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

class AquaIntelApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Facebook SDK initialization
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }
}
