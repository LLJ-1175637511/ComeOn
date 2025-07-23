package com.google.media.lite_player.plugin

import android.content.Context
import android.view.View
import android.widget.FrameLayout

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-23-2025
 */
interface ViewPlayerPlugin : BasePlayerPlugin {

    fun onCreateView(context: Context): View

    fun layoutParams(): FrameLayout.LayoutParams? = null

    fun viewPriority(): ViewPriority

    /**
     * View priority
     *
     * SurfaceView
     * LOW
     * ControllerView
     * MIDDLE
     * HIGH
     */
    enum class ViewPriority {
        LOW, MIDDLE, HIGH
    }

}