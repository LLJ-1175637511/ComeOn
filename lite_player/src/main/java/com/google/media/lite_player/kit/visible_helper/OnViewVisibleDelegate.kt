package com.google.media.lite_player.kit.visible_helper

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-22-2025
 */
interface OnViewVisibleDelegate {
    fun setVisibility(visibility: Int)
    fun getVisibility(): Int

    fun setVisible(visible: Boolean)
    fun getVisible(): Boolean
}