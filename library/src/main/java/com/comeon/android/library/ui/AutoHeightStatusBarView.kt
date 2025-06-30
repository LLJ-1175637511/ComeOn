package com.comeon.android.library.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.WindowInsets

class AutoHeightStatusBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        val onApplyWindowInsets = super.onApplyWindowInsets(insets)
        val top = onApplyWindowInsets.systemWindowInsetTop
        val params = layoutParams
        params.height = top
        layoutParams = params
        return onApplyWindowInsets
    }

}