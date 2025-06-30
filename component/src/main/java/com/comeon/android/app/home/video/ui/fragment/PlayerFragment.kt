package com.comeon.android.app.home.video.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.comeon.android.app.home.video.utils.Logger
import com.comeon.android.component.databinding.FragmentPlayerBinding

class PlayerFragment : Fragment() {

    companion object {
        const val TAG = "PlayerFragment"
    }

    private lateinit var binding: FragmentPlayerBinding

    private val player by lazy {
        ExoPlayer.Builder(requireContext()).build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.videoPlayerView.player = player
        player.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Logger.e(TAG, "Player error: ${error.message}")
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        //播放器停止时的状态
                        Logger.d(TAG, "Player is idle")
                    }

                    Player.STATE_BUFFERING -> {
                        // 正在缓冲数据
                        Logger.d(TAG, "Player is buffering")
                    }

                    Player.STATE_READY -> {
                        // 可以开始播放
                        Logger.d(TAG, "Player is ready to play")
                    }

                    Player.STATE_ENDED -> {
                        // 播放结束
                        Logger.d(TAG, "Player has ended playback")
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    Logger.d(TAG, "Player is playing")
                } else {
                    Logger.d(TAG, "Player is paused or stopped")
                }
            }
        })
        player.apply {
            // 设置重复模式
            // Player.REPEAT_MODE_ALL 无限重复
            // Player.REPEAT_MODE_ONE 重复一次
            // Player.REPEAT_MODE_OFF 不重复
            repeatMode = Player.REPEAT_MODE_OFF
            // 设置当缓冲完毕后直接播放视频
            playWhenReady = true
        }
        player.apply {
            // 停止之前播放的视频
            stop()
            // 设置多个资源，当一个视频播完后自动播放下一个
            setMediaItems(arrayListOf(
                MediaItem.fromUri("https://zhstatic.zhihu.com/vip-fe/live/mp4/979.mp4"),
            ))
            // 开始缓冲
            prepare()
        }
    }

}