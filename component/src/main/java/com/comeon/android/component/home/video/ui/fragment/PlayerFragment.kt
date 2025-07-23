package com.comeon.android.component.home.video.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.comeon.android.component.databinding.FragmentPlayerBinding
import com.google.media.lite_player.plugin_imp.ControllerAction
import com.google.media.lite_player.plugin_imp.ControllerActionListener
import com.google.media.lite_player.plugin_imp.ControllerViewPlugin
import com.google.media.lite_player.plugin_imp.ErrorViewPlugin
import com.google.media.lite_player.plugin_imp.LoadingViewPlugin

class PlayerFragment : Fragment() {

    companion object {
        const val TAG = "PlayerFragment"
    }

    private lateinit var binding: FragmentPlayerBinding

    private val player by lazy {
        ExoPlayer.Builder(requireContext()).build().apply {
            // 设置重复模式
            // Player.REPEAT_MODE_ALL 无限重复
            // Player.REPEAT_MODE_ONE 重复一次
            // Player.REPEAT_MODE_OFF 不重复
            repeatMode = Player.REPEAT_MODE_OFF
            // 设置当缓冲完毕后直接播放视频
            playWhenReady = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.videoPlayerView.addPlugin(ControllerViewPlugin(
            context = requireContext(),
            scope = lifecycleScope,
            player = player,
            controllerActionListener = ControllerActionListener {
                when (it) {
                    is ControllerAction.Close -> {
                        requireActivity().finish()
                    }
                    else -> {}
                }
            }
        ))
        binding.videoPlayerView.addPlugin(LoadingViewPlugin())
        binding.videoPlayerView.addPlugin(ErrorViewPlugin(
            retryCallback = {
                player.prepare()
            }
        ))

        binding.videoPlayerView.initializePlayer(player)

        binding.videoPlayerView.play("https://zhstatic.zhihu.com/vip-fe/live/mp4/979.mp4")
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

}