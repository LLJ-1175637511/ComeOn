package com.comeon.android.library.ext

import android.view.View
import com.comeon.android.library.utils.ViewUtils

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 2025-07-11
 */

fun View.safePostDelayed(action: () -> Unit, delayMillis: Long) {
    ViewUtils.safePostDelayed(this, action, delayMillis)
}

fun View.doOnViewDetachFromWindow(action: () -> Unit) {
    ViewUtils.doOnViewDetachFromWindow(this, action)
}