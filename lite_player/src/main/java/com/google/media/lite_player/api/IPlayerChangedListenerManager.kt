package com.google.media.lite_player.api

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-24-2025
 */
interface IPlayerChangedListenerManager {

    fun addPlayerChangedListener(listener: OnPlayerChangedListener)

    fun removePlayerChangedListener(listener: OnPlayerChangedListener)

}