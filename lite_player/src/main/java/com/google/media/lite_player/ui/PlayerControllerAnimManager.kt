package com.google.media.lite_player.ui

import android.animation.ObjectAnimator
import android.view.View
import com.comeon.android.library.ui.animator.AnimatorChainSet
import com.google.media.lite_player.R
import com.google.media.lite_player.api.controller.IPlayerControllerAnimManager
import com.google.media.lite_player.databinding.ViewLiteExoPlaybackControlBinding

/**
 * @author liulinjie
 * @since 2025-07-10
 */
open class PlayerControllerAnimManager(private val binding: ViewLiteExoPlaybackControlBinding) :
    IPlayerControllerAnimManager {

    companion object {
        const val CONTROLLER_ANIM_DURATION_MILS = 300L
    }

    private val topBarHeight = binding.root.resources.getDimension(R.dimen.player_top_bar_height)
    private val bottomBarHeight =
        binding.root.resources.getDimension(R.dimen.player_bottom_bar_height)

    private val topBarHideTransactionY = -topBarHeight
    private val topBarShowTransactionY1 = 0f

    private val bottomBarHideTransactionY = bottomBarHeight
    private val bottomBarShowTransactionY = 0f

    protected val showControllerAnim by lazy {
        AnimatorChainSet.Builder().apply {
            next(
                ofTranslationY(
                    topBarHideTransactionY,
                    topBarShowTransactionY1,
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
        }.build(isAutoStart = true)
    }

    protected val hideControllerAnim by lazy {
        AnimatorChainSet.Builder().apply {
            next(
                ofTranslationY(
                    topBarShowTransactionY1,
                    topBarHideTransactionY,
                    binding.clVideoTop
                ).also { it.duration = CONTROLLER_ANIM_DURATION_MILS },
                ofTranslationY(
                    bottomBarShowTransactionY,
                    bottomBarHideTransactionY,
                    binding.clVideoBottom
                ).also {
                    it.duration = CONTROLLER_ANIM_DURATION_MILS
                }
            )
        }.build(isAutoStart = true)
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
        binding.clVideoTop.translationY = topBarShowTransactionY1
        binding.clVideoBottom.translationY = bottomBarShowTransactionY
    }

    override fun isControllerVisible(): Boolean {
        return binding.clVideoTop.translationY == 0f && binding.clVideoBottom.translationY == 0f
    }

}