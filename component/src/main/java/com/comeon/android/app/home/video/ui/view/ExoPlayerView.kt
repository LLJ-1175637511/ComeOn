package com.comeon.android.app.home.video.ui.view

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import androidx.annotation.OptIn
import androidx.core.view.isVisible
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerControlView
import androidx.media3.ui.PlayerView
import androidx.media3.ui.R
import com.comeon.android.component.databinding.ViewExoPlaybackControlBinding
import com.comeon.android.library.utils.ToastUtils

class ExoPlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): PlayerView(context, attrs, defStyleAttr) {

    private val playerControlView = findViewById<PlayerControlView>(R.id.exo_controller)
    val binding = ViewExoPlaybackControlBinding.bind(playerControlView)

    fun initializePlayer(player: ExoPlayer) {
        if (this.player != null) {
            // 如果已经有播放器实例，先释放旧的播放器
            return
        }
        this.player = player
        player.apply {
            // 设置重复模式
            // Player.REPEAT_MODE_ALL 无限重复
            // Player.REPEAT_MODE_ONE 重复一次
            // Player.REPEAT_MODE_OFF 不重复
            repeatMode = Player.REPEAT_MODE_OFF
            // 设置当缓冲完毕后直接播放视频
            playWhenReady = true
        }
        binding.exoPlay.setOnClickListener {
            if (player.isPlaying)  return@setOnClickListener
            player.play()
        }
        binding.exoPause.setOnClickListener {
            if (!player.isPlaying) return@setOnClickListener
            player.pause()
        }
        player.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                // 处理播放器错误
                binding.exoPlay.isVisible = true
                binding.exoPause.isVisible = false
                ToastUtils.showToast(context, "播放错误: ${error.message}")
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        // 播放器处于空闲状态
                    }
                    Player.STATE_BUFFERING -> {
                        // 正在缓冲数据
                    }
                    Player.STATE_READY -> {
                        // 可以开始播放
                    }
                    Player.STATE_ENDED -> {
                        // 播放结束
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    binding.exoPlay.isVisible = false
                    binding.exoPause.isVisible = true
                } else {
                    binding.exoPlay.isVisible = true
                    binding.exoPause.isVisible = false
                }
            }
        })
//        binding.exoProgress.scr(false)
    }

    fun play(path: String) {
        safePlayer {
            if (isPlaying) {
                stop()
            }
            setMediaItem(MediaItem.fromUri(path))
            prepare()
        }
    }

    private fun safePlayer(action: Player.() -> Unit) {
        player?.let { action(it) }
    }

}