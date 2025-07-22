package com.google.media.lite_player.kit.visible_helper

import android.view.View
import androidx.core.view.isVisible

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-22-2025
 */
open class ViewVisibleDelegate(val view: View): OnViewVisibleDelegate {
    override fun setVisibility(visibility: Int) {
        view.visibility = visibility
    }

    override fun getVisibility(): Int = view.visibility

    override fun setVisible(visible: Boolean) {
        view.isVisible = visible
    }

    override fun getVisible(): Boolean = view.isVisible
}