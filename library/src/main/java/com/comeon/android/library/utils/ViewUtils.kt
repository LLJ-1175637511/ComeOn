package com.comeon.android.library.utils

import android.view.View

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 2025-07-11
 */
object ViewUtils {

    fun safePostDelayed(view: View, action: () -> Unit, delayMillis: Long) {
        var listener: View.OnAttachStateChangeListener? = null

        val runnable = Runnable {
            view.removeOnAttachStateChangeListener(listener)
            action()
        }

        listener = object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}

            override fun onViewDetachedFromWindow(v: View) {
                view.removeCallbacks(runnable)
                view.removeOnAttachStateChangeListener(listener)
            }
        }

        view.addOnAttachStateChangeListener(listener)

        view.postDelayed(runnable, delayMillis)
    }

    fun safePostDelayed(view: View, runnable: Runnable, delayMillis: Long) {
        val listener = object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}

            override fun onViewDetachedFromWindow(v: View) {
                view.removeCallbacks(runnable)
                view.removeOnAttachStateChangeListener(this)
            }
        }
        view.addOnAttachStateChangeListener(listener)
        view.postDelayed(runnable, delayMillis)
    }

    fun doOnViewDetachFromWindow(view: View, action: () -> Unit) {
        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}

            override fun onViewDetachedFromWindow(v: View) {
                action()
                view.removeOnAttachStateChangeListener(this)
            }
        })
    }

}