package com.google.media.lite_player.plugin

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-23-2025
 */
interface IPlayerPluginManager {

    fun addPlugin(plugin: BasePlayerPlugin)

    fun removePlugin(plugin: BasePlayerPlugin)

}