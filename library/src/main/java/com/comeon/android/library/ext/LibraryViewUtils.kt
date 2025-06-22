package com.comeon.android.library.ext

import android.view.View
import com.comeon.android.library.utils.DisplayUtils

fun View.dp2px(value: Int): Int {
    return DisplayUtils.dp2px(this.context, value.toFloat())
}

