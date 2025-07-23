package com.google.media.lite_player.core

import android.content.Context
import android.os.Looper
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.media3.common.Player
import com.google.media.lite_player.api.IPlayerChangedListenerManager
import com.google.media.lite_player.api.OnPlayerChangedListener
import com.google.media.lite_player.api.ui.IBasePlayerView
import com.google.media.lite_player.plugin.BasePlayerPlugin
import com.google.media.lite_player.plugin.IPlayerPluginManager

abstract class BasePlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IBasePlayerView, IPlayerPluginManager, IPlayerChangedListenerManager {

    protected var player: Player? = null
        private set

    val surfaceViewDelegate by lazy { buildSurfaceViewDelegate() }

    protected val plugins = mutableListOf<BasePlayerPlugin>()

    protected val playerChangedListeners = mutableListOf<OnPlayerChangedListener>()

    override fun initializePlayer(player: Player?) {
        player ?: return
        //校验主线程
        if (Thread.currentThread() != Looper.getMainLooper().thread) {
            throw IllegalStateException("setPlayer must be called on the main thread")
        }
        val oldPlayer = this.player

        if (player == oldPlayer) return

        this.player = player

        onPlayerChanged(oldPlayer = oldPlayer, newPlayer = player)
    }

    protected open fun onPlayerChanged(oldPlayer: Player?, newPlayer: Player?) {

        playerChangedListeners.forEach {
            it.onPlayerChangedListener(oldPlayer, newPlayer)
        }

        surfaceViewDelegate.setPlayer(player)

        optionPlugin {
            getPlayerListener()?.let {
                oldPlayer?.removeListener(it)
                newPlayer?.addListener(it)
            }
        }
    }

    override fun addPlugin(plugin: BasePlayerPlugin) {
        plugin.getPlayerListener()?.let {
            player?.removeListener(it)
            player?.addListener(it)
        }
        if (plugins.contains(plugin)) return
        plugins.add(plugin)
        plugin.onAddPlugin()
    }

    override fun removePlugin(plugin: BasePlayerPlugin) {
        plugin.getPlayerListener()?.let { player?.removeListener(it) }
        plugins.remove(plugin)
        plugin.onRemovePlugin()
    }

    protected fun optionPlugin(block: BasePlayerPlugin.() -> Unit) {
        plugins.forEach {
            block(it)
        }
    }

    override fun addPlayerChangedListener(listener: OnPlayerChangedListener) {
        playerChangedListeners.add(listener)
    }

    override fun removePlayerChangedListener(listener: OnPlayerChangedListener) {
        playerChangedListeners.remove(listener)
    }

    override fun onDetachedFromWindow() {
        optionPlugin {
            getPlayerListener()?.let { player?.removeListener(it) }
            onVideoViewDetach()
        }
        plugins.clear()
        playerChangedListeners.clear()
        super.onDetachedFromWindow()
    }
}
