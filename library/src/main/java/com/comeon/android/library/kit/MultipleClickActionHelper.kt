package com.comeon.android.library.kit

import java.lang.ref.WeakReference

/**
 * MultipleClickActionHelper 用于处理多次点击事件
 * @param requiredClickCount 需要的点击次数
 * @param action 点击达到次数后执行的操作
 */
class MultipleClickActionHelper(
    private val requiredClickCount: Int,
    action: () -> Unit
) {
    private var clickCount = 0 // 当前点击次数
    private var lastClickTime = 0L // 上次点击的时间戳
    private val timeInterval: Long = 500 // 时间间隔，单位毫秒
    private val weakAction = WeakReference(action)

    fun registerClick() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime <= timeInterval) {
            clickCount++
            if (clickCount == requiredClickCount) {
                weakAction.get()?.invoke()
                reset()
            }
        } else {
            reset()
            clickCount = 1
        }
        lastClickTime = currentTime
    }

    private fun reset() {
        clickCount = 0
        lastClickTime = 0L
    }
}