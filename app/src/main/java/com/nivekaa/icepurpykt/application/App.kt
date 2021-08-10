package com.nivekaa.icepurpykt.application

import android.app.Application
import com.zplesac.connectionbuddy.ConnectionBuddy
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        val networkInspectorConfiguration = ConnectionBuddyConfiguration.Builder(this)
            .setNotifyImmediately(true)
            .build()
        ConnectionBuddy.getInstance().init(networkInspectorConfiguration)
    }
}