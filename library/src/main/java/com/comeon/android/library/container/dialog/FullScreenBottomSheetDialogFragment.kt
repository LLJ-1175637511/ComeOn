package com.comeon.android.library.container.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.comeon.android.library.R
import com.comeon.android.library.utils.DisplayUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class FullScreenBottomSheetDialogFragment : BottomSheetDialogFragment() {

    open fun isBackgroundDimEnabled() = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            behavior.apply {
                skipCollapsed = true
                isDraggable = false
                //fix： bottomSheet有默认最大高度（导致页面会被裁剪） 手动设置peekHeight
                //扩展最大高度为全屏（虽然业务上也不可能全屏）
                peekHeight = DisplayUtils.getScreenHeightPixels(context)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenAndTransDialogTheme) //dialog全屏
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            attributes?.windowAnimations = R.style.BottomSheetDialogAnimation
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

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (e: IllegalStateException) {
            //ignore
        }
    }

    override fun dismiss() {
        try {
            super.dismiss()
        } catch (e: IllegalStateException) {
            //ignore
        }
    }

}