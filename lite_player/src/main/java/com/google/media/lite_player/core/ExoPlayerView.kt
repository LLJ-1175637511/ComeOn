package com.google.media.lite_player.core

import android.content.Context
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import com.google.media.lite_player.api.ui.ISurfaceViewDelegate
import com.google.media.lite_player.api.ui.onVideoSizeChanged
import com.google.media.lite_player.databinding.LiteExoPlayerViewBinding
import com.google.media.lite_player.api.controller.IPlayerController
import com.google.media.lite_player.ui.PlayerController

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
    }

    private val componentListener = ComponentListener()

    @OptIn(UnstableApi::class)
    override fun buildSurfaceViewDelegate(): ISurfaceViewDelegate {
        //返回当前的SurfaceView代理
        return LiteSurfaceView(context) {
            binding.contentFrame.setAspectRatio(it)
        }
    }

    override fun buildPlayerController(): IPlayerController {
        return PlayerController(binding.exoController)
    }

    override fun initializePlayer(player: Player?) {
        player ?: return
        //校验主线程
        if (Thread.currentThread() != Looper.getMainLooper().thread) {
            throw IllegalStateException("setPlayer must be called on the main thread")
        }
        if (player == this.player) return
        //释放旧的播放器
        this.player?.removeListener(componentListener)

        surfaceViewDelegate.setPlayer(player)

        this.player = player

        controller.setPlayer(player)

        player.addListener(componentListener)
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

    private inner class ComponentListener : Player.Listener {
        override fun onVideoSizeChanged(videoSize: VideoSize) {
            surfaceViewDelegate.onVideoSizeChanged(player, videoSize)
        }
    }

}