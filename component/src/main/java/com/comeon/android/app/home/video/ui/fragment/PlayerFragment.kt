package com.comeon.android.app.home.video.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        binding.videoPlayerView.initializePlayer(player)
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

        binding.videoPlayerView.play("https://zhstatic.zhihu.com/vip-fe/live/mp4/979.mp4")
        binding.videoPlayerView.binding.btnVideoReturn.setOnClickListener {
            requireActivity().finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

}