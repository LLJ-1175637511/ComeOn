package com.google.media.lite_player.core

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import com.google.media.lite_player.api.ui.ISurfaceViewDelegate
import com.google.media.lite_player.api.ui.onVideoSizeChanged
import com.google.media.lite_player.databinding.LiteExoPlayerViewBinding
import com.google.media.lite_player.plugin.BasePlayerPlugin
import com.google.media.lite_player.plugin.ViewPlayerPlugin
import com.google.media.lite_player.plugin_imp.VideoSizeChangePlugin

class ExoPlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BasePlayerView(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "ExoPlayerView"
    }

    private val binding = LiteExoPlayerViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.contentFrame.addView(
            surfaceViewDelegate.getSurfaceView(), 0, LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        addPlugin(
            VideoSizeChangePlugin(
                onSizeChangeCallback = { videoSize ->
                    surfaceViewDelegate.onVideoSizeChanged(player, videoSize)
                }
            ))
    }

    @OptIn(UnstableApi::class)
    override fun buildSurfaceViewDelegate(): ISurfaceViewDelegate {
        //返回当前的SurfaceView代理
        return LiteSurfaceView(context) {
            binding.contentFrame.setAspectRatio(it)
        }
    }

    override fun addPlugin(plugin: BasePlayerPlugin) {
        when (plugin) {
            is ViewPlayerPlugin -> {
                addViewPlugin(plugin)
            }
        }
        super.addPlugin(plugin)
    }

    private fun addViewPlugin(plugin: ViewPlayerPlugin) {
        val container: FrameLayout = when (plugin.viewPriority()) {
            ViewPlayerPlugin.ViewPriority.LOW -> {
                binding.playerLowContainer
            }

            ViewPlayerPlugin.ViewPriority.MIDDLE -> {
                binding.playerMidContainer
            }

            ViewPlayerPlugin.ViewPriority.HIGH -> {
                binding.playerHighContainer
            }
        }
        container.addView(plugin.onCreateView(context), plugin.layoutParams() ?: defaultLayoutParam())
    }

    private fun defaultLayoutParam() = LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT
    ).also {
        it.gravity = Gravity.CENTER
    }

    fun stop() {
        player?.apply {
            stop()
        }
    }

    fun pause() {
        player?.apply {
            pause()
        }
    }

    fun resume() {
        player?.apply {
            play()
        }
    }

    fun play(path: String) {
        player?.apply {
            if (isPlaying) {
                stop()
            }
            setMediaItem(MediaItem.fromUri(path))
            prepare()
        }
    }

}