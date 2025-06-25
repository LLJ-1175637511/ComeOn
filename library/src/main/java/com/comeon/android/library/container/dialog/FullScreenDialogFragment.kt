package com.comeon.android.library.container.dialog

import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import com.comeon.android.library.R

open class FullScreenDialogFragment : DialogFragment() {

    open fun isBackgroundDimEnabled() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenAndTransDialogTheme) //dialog全屏
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            attributes = attributes?.also {
                if (isBackgroundDimEnabled()) {
                    it.dimAmount = 0.6f
                } else {
                    it.dimAmount = 0f
                }
            }
            WindowCompat.setDecorFitsSystemWindows(this, false)
        }
    }

}