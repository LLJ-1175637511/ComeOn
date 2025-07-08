package com.google.media.lite_player.kit

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import androidx.core.view.isVisible

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 2025-07-10
 */
open class VisibleAlphaAnimHelper(private val view: View) {

    companion object {
        private const val TAG = "VisibleAlphaAnimHelper"
        private const val DEFAULT_DURATION = 300L
    }

    protected open val duration: Long = DEFAULT_DURATION

    protected val showAnim by lazy {
        view.ofAlpha(0f, 1f)
    }

    protected val hideAnim by lazy {
        view.ofAlpha(1f, 0f)
    }

    private var targetVisibility: Int = view.visibility

    open fun setVisibility(visibility: Int, useAnim: Boolean = true) {
        if (!useAnim) {
            hideAnim.cancel()
            showAnim.cancel()
            view.visibility = visibility
            return
        }
        if (visibility == view.visibility) return
        targetVisibility = visibility
        if (visibility == View.VISIBLE) {
            hideAnim.cancel()
            showAnim.start()
        } else if (visibility == View.GONE || visibility == View.INVISIBLE) {
            showAnim.cancel()
            hideAnim.start()
        }
    }

    fun getVisibility(): Int {
        return view.visibility
    }

    fun isVisible(): Boolean {
        return view.isVisible || showAnim.isRunning
    }

    fun setVisible(isVisible: Boolean, useAnim: Boolean = true) {
        setVisibility(if (isVisible) View.VISIBLE else View.GONE, useAnim)
    }

    private fun View.ofAlpha(startValue: Float, endValue: Float): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "alpha", startValue, endValue).also { animator ->
            animator.duration = duration
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    view.alpha = startValue
                }

                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = targetVisibility
                }
            })
        }
    }

}