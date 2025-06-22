package com.comeon.android.library.utils

import com.comeon.android.library.log.CoLogger
import com.comeon.android.library.log.ILogger

object LoggerFactory {

    @JvmStatic
    fun newLogger(module: String): ILogger {
        return CoLogger(module)
    }

}