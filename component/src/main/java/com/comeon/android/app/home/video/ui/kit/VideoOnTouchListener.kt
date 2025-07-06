package com.comeon.android.app.home.video.ui.kit

import android.view.MotionEvent
import android.view.View
import java.util.Timer
import java.util.TimerTask

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 2025-07-05
 */
class VideoOnTouchListener(
    private var onLongTouchCallback: OnLongTouchCallback ?= null
): View.OnTouchListener {
    //识别长按
    private val LONG_PRESS_DURATION = 800L // 长按持续时间，单位毫秒
    private var timer: Timer? = null
    private var isLongPressed = false
    private var startX = 0f
    private var startY = 0f
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isLongPressed = false
                startX = event.x
                startY = event.y
                timer = Timer().apply {
                    schedule(object :TimerTask(){
                        override fun run() {
                            isLongPressed = true
                            v.post {
                                onLongTouchCallback?.onStart()
                            }
                        }
                    }, LONG_PRESS_DURATION)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                // 检测是否移动超过一定距离，如果超过则取消长按
                val deltaX = event.x - startX
                val deltaY = event.y - startY
                if (Math.sqrt((deltaX * deltaX + deltaY * deltaY).toDouble()) > 20) {
                    timer?.cancel()
                    timer = null
                    isLongPressed = false
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                timer?.cancel()
                timer = null
                if (isLongPressed) {
                    v.post { onLongTouchCallback?.onActionUp() }
                }
            }
        }
        return true
    }
}