package com.comeon.android.foundation

import android.app.Application
import android.util.Log
import com.comeon.android.foundation.spi.InstanceProvider
import com.comeon.android.foundation.app.ApplicationLauncherInterface

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        InstanceProvider.getAll(ApplicationLauncherInterface::class.java).forEach {
            it.onCreate(this)
        }
    }

    companion object {
        private var instance: BaseApplication? = null
        fun get(): BaseApplication {
            return instance ?: throw IllegalStateException("Application not initialized")
        }
    }

}