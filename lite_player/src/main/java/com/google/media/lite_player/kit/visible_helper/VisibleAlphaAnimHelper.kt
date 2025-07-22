package com.google.media.lite_player.kit.visible_helper

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 2025-07-10
 */
open class VisibleAlphaAnimHelper(private val view: View, private val duration: Long, private val visibleDelegate: OnViewVisibleDelegate = ViewVisibleDelegate(view)): OnViewVisibleDelegate by visibleDelegate {

    init {
        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}

            override fun onViewDetachedFromWindow(v: View) {
                if (showAnim.isRunning) {
                    showAnim.cancel()
                }
                if (hideAnim.isRunning) {
                    hideAnim.cancel()
                }
                view.removeOnAttachStateChangeListener(this)
            }
        })
    }

    private var targetVisibility: Int = getVisibility()

    protected open val showAnim by lazy {
        ofAlpha(0f, 1f).also {
            it.doOnEnd { setVisibility(targetVisibility) }
        }
    }

    protected open val hideAnim by lazy {
        ofAlpha(1f, 0f).also {
            it.doOnEnd { setVisibility(targetVisibility) }
        }
    }

    open fun setVisibility(visibility: Int, useAnim: Boolean) {
        if (!useAnim) {
            cancelAnim()
            setVisibility(visibility)
            return
        }
        if (visibility == getVisibility()) return
        targetVisibility = visibility
        if (visibility == View.VISIBLE) {
            showAnim()
        } else if (visibility == View.GONE || visibility == View.INVISIBLE) {
            hideAnim()
        }
    }

    open fun showAnim() {
        hideAnim.cancel()
        showAnim.start()
    }

    open fun hideAnim() {
        showAnim.cancel()
        hideAnim.start()
    }

    open fun cancelAnim() {
        hideAnim.cancel()
        showAnim.cancel()
    }

    open fun setVisible(visible: Boolean, useAnim: Boolean) {
        setVisibility(if (visible) View.VISIBLE else View.GONE, useAnim)
    }

    private fun ofAlpha(startValue: Float, endValue: Float): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "alpha", startValue, endValue).also { animator ->
            animator.duration = duration
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    view.alpha = startValue
                }

                override fun onAnimationEnd(animation: Animator) {
                    view.alpha = endValue
                }
            })
        }
    }
}