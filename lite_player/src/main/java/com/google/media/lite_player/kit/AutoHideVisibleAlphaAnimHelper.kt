package com.google.media.lite_player.kit

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.comeon.android.library.utils.ViewUtils

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 2025-07-10
 */
class AutoHideVisibleAlphaAnimHelper(
    private val view: View,
    duration: Long,
    private val autoHideMils: Long
) : VisibleAlphaAnimHelper(view, duration) {

    private val autoHideAction = Runnable {
        setVisible(false)
    }

    init {
        hideAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                view.removeCallbacks(autoHideAction)
            }
        })
    }

    override fun setVisibility(visibility: Int, useAnim: Boolean) {
        super.setVisibility(visibility, useAnim)
        checkAutoHide(visibility == View.VISIBLE)
    }

    /**
     * 展示view后，如果需要自动隐藏，则延时执行隐藏操作
     */
    private fun checkAutoHide(isVisible: Boolean) {
        if (isVisible) {
            view.removeCallbacks(autoHideAction)
            view.postDelayed(autoHideAction, autoHideMils)
        }
    }

}