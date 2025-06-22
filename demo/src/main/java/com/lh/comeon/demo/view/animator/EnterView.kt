package com.lh.comeon.demo.view.animator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.comeon.android.component.ui.animator.AnimatorChainSet
import com.comeon.android.demo.databinding.ViewEnterTestBinding
import com.comeon.android.library.ext.dp2px
import com.comeon.android.library.utils.DisplayUtils

class EnterView @JvmOverloads constructor(
    context: android.content.Context,
    attrs: android.util.AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ViewEnterTestBinding.inflate(LayoutInflater.from(context), this, true)

    fun start(isUsedAnimatorChainSet: Boolean) {
        playAnimator(binding.flContent, isUsedAnimatorChainSet)
    }

    private fun playAnimator(
        containerView: View,
        isUsedAnimatorChainSet: Boolean
    ) {
        val screenWidth = DisplayUtils.getScreenWidthPixels(context).toFloat()
        // 阶段1：初始状态 向右平移到屏幕外
        containerView.translationX = screenWidth
        containerView.isVisible = true
        val movedDuration = 3000L
        // 阶段2：0-500ms平移动画
        val translate1 = ObjectAnimator.ofFloat(
            containerView, "translationX",
            screenWidth, dp2px(44).toFloat()
        ).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 500
        }

        // 阶段3：160-500ms透明度动画（与阶段2叠加）
        val alpha1 = ObjectAnimator.ofFloat(
            containerView, "alpha", 0f, 1f
        ).apply {
            duration = 340  // 500-160=340ms
        }

        // 阶段4：500-2500ms平移动画
        val translate2 = ObjectAnimator.ofFloat(
            containerView, "translationX",
            dp2px(44).toFloat(), dp2px(12).toFloat()
        ).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = movedDuration  // 2500-500=2000ms
        }

        // 阶段5：2500-3000ms宽度平移
        val translate3 = ObjectAnimator.ofFloat(
            containerView, "translationX",
            dp2px(12).toFloat(), -screenWidth
        ).apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = 500
        }

        // 阶段6：3000-3340ms透明度动画
        val alpha2 = ObjectAnimator.ofFloat(
            containerView, "alpha", 1f, 0f
        ).apply {
            startDelay = 160
            duration = 340  // 340ms
        }

        if (isUsedAnimatorChainSet) {
            AnimatorChainSet
                .build()
                .next(translate1, alpha1)
                .next(translate2)
                .next(translate3, alpha2)
                .build()
                .apply {
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            containerView.isVisible = false
                        }
                    })
                }
                .start()
        } else {
            AnimatorSet().apply {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        containerView.isVisible = false
                    }
                })
                //执行顺序 after -> play -> before
                //所有after中的动画同事执行 所有before中的动画同时执行
                play(translate2)
                    .after(alpha1)
                    .after(translate1)
                    .before(translate3)
                    .before(alpha2)
                start()
            }
        }
    }

}