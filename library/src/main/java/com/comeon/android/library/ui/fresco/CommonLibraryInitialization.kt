package com.comeon.android.library.ui.fresco

import android.app.Application
import com.comeon.android.foundation.app.ApplicationLauncherInterface
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.auto.service.AutoService

/**
 * @author liulinjie 
 * @since 2025-05-21
 */
@AutoService(ApplicationLauncherInterface::class)
class CommonLibraryInitialization: ApplicationLauncherInterface {

    override fun onCreate(context: Application) {
        Fresco.initialize(context)
    }

}