package com.comeon.android.library.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object ToastUtils {
    @JvmStatic
    fun showToast(context: Context, text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    @JvmStatic
    fun showToast(context: Context, @StringRes textRes: Int) {
        Toast.makeText(
            context,
            textRes,
            Toast.LENGTH_SHORT
        ).show()
    }

    @JvmStatic
    fun showLongToast(context: Context, text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    @JvmStatic
    fun showLongToast(context: Context, @StringRes textRes: Int) {
        Toast.makeText(
            context,
            textRes,
            Toast.LENGTH_LONG
        ).show()
    }
}