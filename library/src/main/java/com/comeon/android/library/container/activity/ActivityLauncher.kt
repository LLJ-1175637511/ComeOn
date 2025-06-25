package com.comeon.android.library.container.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.comeon.android.library.kit.ActivityLifecycleCallbacksAdapter
import com.comeon.android.foundation.app.ApplicationLauncherInterface
import com.comeon.android.library.ext.logger
import com.google.auto.service.AutoService

@AutoService(ApplicationLauncherInterface::class)
class ActivityLauncher : ApplicationLauncherInterface {
    override fun onCreate(context: Application) {
        context.registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacksAdapter() {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    logger.d(TAG, "onActivityCreated: ${activity.javaClass.simpleName}")
                    BaseActivity.addActivity(activity)
                }

                override fun onActivityDestroyed(activity: Activity) {
                    logger.d(TAG, "onActivityDestroyed: ${activity.javaClass.simpleName}")
                    BaseActivity.removeActivity(activity)
                }
            }
        )
    }

    companion object {
        private const val TAG = "BaseActivity"
    }

}