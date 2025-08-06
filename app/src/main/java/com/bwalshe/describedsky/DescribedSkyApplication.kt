package com.bwalshe.describedsky

import android.app.Application
import com.bwalshe.describedsky.data.AppContainer
import com.bwalshe.describedsky.data.DefaultContainer

class DescribedSkyApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultContainer()
    }
}