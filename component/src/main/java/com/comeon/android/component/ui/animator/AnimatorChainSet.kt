package com.comeon.android.component.ui.animator

import android.animation.Animator
import android.animation.AnimatorSet

class AnimatorChainSet {

    open class Builder {

        private var currentMaxDuration = 0L

        private var animatorList = mutableListOf<Animator>()

        /**
         * 在上一个动画结束后播放
         * @param nextAnim 上一个动画结束后，需要播放的动画
         * @param togetherAnim 需要和[nextAnim]一起播放的动画
         */
        open fun next(nextAnim: Animator, vararg togetherAnim: Animator): Builder {
            addNextAnim(nextAnim, *togetherAnim)
            return this
        }

        /**
         * 构建AnimatorSet
         *
         * @param isAutoStart 是否自动开始；
         * 为false时可以自定义[AnimatorSet]属性后 手动开启
         */
        open fun build(isAutoStart: Boolean = false): AnimatorSet {
            return AnimatorSet().apply {
                playTogether(animatorList)
                if (isAutoStart) start()
            }
        }

        private fun addNextAnim(nextAnim: Animator, vararg togetherAnim: Animator) {
            addAnim(nextAnim, currentMaxDuration)
            togetherAnim.forEach {
                addAnim(it, currentMaxDuration)
            }

            currentMaxDuration += nextAnim.duration
        }

        private fun addAnim(anim: Animator, appendDuration: Long) {
            anim.startDelay += appendDuration
            animatorList.add(anim)
        }
    }

    companion object {
        /**
         * 创建一个AnimatorChainSet.Builder
         */
        @JvmStatic
        fun build(): Builder {
            return Builder()
        }
    }

}