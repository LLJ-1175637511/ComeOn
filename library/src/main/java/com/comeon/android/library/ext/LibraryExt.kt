package com.comeon.android.library.ext

import android.content.Context
import androidx.annotation.StringRes
import com.comeon.android.library.utils.ToastUtils

fun Context.showToast(text: String) {
    ToastUtils.showToast(this, text)
}

fun Context.showLongToast(text: String) {
    ToastUtils.showLongToast(this, text)
}

fun Context.showToast(@StringRes textRes: Int) {
    ToastUtils.showToast(this, textRes)
}

fun Context.showLongToast(@StringRes textRes: Int) {
    ToastUtils.showLongToast(this, textRes)
}