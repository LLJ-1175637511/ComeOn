package com.google.media.lite_player.ui

import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.doOnDetach
import com.comeon.android.library.ext.doOnViewDetachFromWindow
import com.comeon.android.library.ui.animator.AnimatorChainSet
import com.google.media.lite_player.R
import com.google.media.lite_player.api.controller.IPlayerControllerVisibleManager
import com.google.media.lite_player.api.controller.OnViewVisibleListener
import com.google.media.lite_player.databinding.ViewLiteExoPlaybackControlBinding

/**
 * @author liulinjie
 * @since 2025-07-10
 */
open class PlayerControllerVisibleManager(
    private val binding: ViewLiteExoPlaybackControlBinding,
    private val autoHideOnControllerVisibleMils: Long = 2000L,
    private val controllerAnimDurationMils: Long = 300L
) : IPlayerControllerVisibleManager {

    private var controllerVisibleListeners = mutableListOf<OnViewVisibleListener>()

    private val topBarHeight = binding.root.resources.getDimension(R.dimen.player_top_bar_height)
    private val bottomBarHeight =
        binding.root.resources.getDimension(R.dimen.player_bottom_bar_height)

    private val topBarHideTransactionY = -topBarHeight
    private val topBarShowTransactionY = 0f

    private val bottomBarHideTransactionY = bottomBarHeight
    private val bottomBarShowTransactionY = 0f

    protected open val autoHideControllerRunnable = Runnable {
        if (isControllerVisible()) {
            hideControllerAnim(true)
        }
    }

    protected open val showControllerAnim by lazy {
        AnimatorChainSet.Builder().apply {
            next(
                ofTranslationY(
                    topBarHideTransactionY,
                    topBarShowTransactionY,
                    binding.clVideoTop
                ).also {
                    it.duration = 300L
                },
                ofTranslationY(
                    bottomBarHideTransactionY,
                    bottomBarShowTransactionY,
                    binding.clVideoBottom
                ).also { it.duration = 300L }
            )
        }.build(isAutoStart = true).also {
            it.doOnStart {
                notifyControllerVisibleListeners(true)
            }
            it.doOnEnd {
                startAutoHideController()
            }
        }
    }

    protected open val hideControllerAnim by lazy {
        AnimatorChainSet.Builder().apply {
            next(
                ofTranslationY(
                    topBarShowTransactionY,
                    topBarHideTransactionY,
                    binding.clVideoTop
                ).also { it.duration = controllerAnimDurationMils },
                ofTranslationY(
                    bottomBarShowTransactionY,
                    bottomBarHideTransactionY,
                    binding.clVideoBottom
                ).also {
                    it.duration = controllerAnimDurationMils
                }
            )
        }.build(isAutoStart = true).also {
            it.doOnStart {
                notifyControllerVisibleListeners(false)
            }
        }
    }

    init {
        binding.root.doOnViewDetachFromWindow {
            release()
        }
    }

    private fun ofTranslationY(startValue: Float, endValue: Float, target: View): ObjectAnimator {
        return ObjectAnimator.ofFloat(target, "translationY", startValue, endValue)
    }

    override fun showControllerAnim(useAnim: Boolean) {
        if (!useAnim) {
            showController()
            return
        }
        if (showControllerAnim.isRunning) {
            return
        }
        showControllerAnim.start()
    }

    override fun hideControllerAnim(useAnim: Boolean) {
        if (!useAnim) {
            hideController()
            return
        }
        if (hideControllerAnim.isRunning) {
            return
        }
        hideControllerAnim.start()
    }

    private fun hideController() {
        binding.clVideoTop.translationY = topBarHideTransactionY
        binding.clVideoBottom.translationY = bottomBarHideTransactionY
    }

    private fun showController() {
        binding.clVideoTop.translationY = topBarShowTransactionY
        binding.clVideoBottom.translationY = bottomBarShowTransactionY
    }

    override fun isControllerVisible(): Boolean {
        return binding.clVideoTop.translationY == 0f && binding.clVideoBottom.translationY == 0f
    }

    open fun release() {
        binding.root.removeCallbacks(autoHideControllerRunnable)
        showControllerAnim.cancel()
        hideControllerAnim.cancel()
        controllerVisibleListeners.clear()
    }

    open fun startAutoHideController() {
        binding.root.removeCallbacks(autoHideControllerRunnable)
        binding.root.postDelayed(autoHideControllerRunnable, autoHideOnControllerVisibleMils)
    }

    override fun registerControllerVisibleListener(listener: OnViewVisibleListener) {
        if (controllerVisibleListeners.contains(listener)) return
        controllerVisibleListeners.add(listener)
    }

    override fun unregisterControllerVisibleListener(listener: OnViewVisibleListener) {
        controllerVisibleListeners.remove(listener)
    }

    private fun notifyControllerVisibleListeners(isVisible: Boolean) {
        controllerVisibleListeners.forEach { listener ->
            listener.onVisibleChanged(isVisible)
        }
    }

}