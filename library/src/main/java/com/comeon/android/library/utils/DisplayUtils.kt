package com.comeon.android.library.utils

import android.content.Context

object DisplayUtils {

    fun getScreenWidthPixels(context: Context?): Int {
        return context?.resources?.displayMetrics?.widthPixels ?: 0
    }

    fun getScreenHeightPixels(context: Context?): Int {
        return context?.resources?.displayMetrics?.heightPixels ?: 0
    }

    fun px2dp(context: Context?, pPixels: Float): Int {
        if (context == null) {
            return 0
        }
        val density = context.resources.displayMetrics.density
        return (pPixels / density + 0.5).toInt()
    }

    fun dp2px(context: Context?, dp: Float): Int {
        if (context == null) {
            return 0
        }
        val density = context.resources.displayMetrics.density
        return (dp * density + 0.5f).toInt()
    }

    fun sp2px(context: Context?, sp: Float): Int {
        return if(context == null) {
            0
        } else (sp * context.resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }

}