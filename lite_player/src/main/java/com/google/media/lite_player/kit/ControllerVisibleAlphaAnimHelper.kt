package com.google.media.lite_player.kit

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.core.view.isVisible
import com.comeon.android.library.ext.safePostDelayed
import com.comeon.android.library.utils.ViewUtils

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 2025-07-10
 */
open class ControllerVisibleAlphaAnimHelper(private val view: View) : VisibleAlphaAnimHelper(view) {

    companion object {
        private const val TAG = "ControllerVisibleAlphaAnimHelper"
        private const val DEFAULT_AUTO_HIDE_MILS_DURATION = 5000L
    }

    open val autoHideMils: Long = DEFAULT_AUTO_HIDE_MILS_DURATION

    private val autoHideAction = {
        if (isVisible()) {
            setVisible(false)
        }
    }

    init {
        showAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                checkAutoHide()
            }
        })
    }

    override fun setVisibility(visibility: Int, useAnim: Boolean) {
        super.setVisibility(visibility, useAnim)
        checkAutoHide()
    }

    /**
     * 展示view后，如果需要自动隐藏，则延时执行隐藏操作
     */
    private fun checkAutoHide() {
        if (isVisible()) {
            ViewUtils.safePostDelayed(view, autoHideAction, autoHideMils)
        }
    }

}