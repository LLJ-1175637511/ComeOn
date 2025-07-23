package com.google.media.lite_player.plugin_imp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.google.media.lite_player.databinding.ViewPlayerErrorLayerBinding
import com.google.media.lite_player.plugin.ViewPlayerPlugin

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-23-2025
 */
class ErrorViewPlugin(
    private val retryCallback: () -> Unit
) : ViewPlayerPlugin {

    private lateinit var binding: ViewPlayerErrorLayerBinding

    override fun onCreateView(context: Context): View {
        return ViewPlayerErrorLayerBinding.inflate(LayoutInflater.from(context)).also {
            binding = it
            binding.btnRetry.setOnClickListener {
                retryCallback()
                binding.root.isVisible = false
            }
            binding.root.isVisible = false
        }.root
    }

    override fun viewPriority(): ViewPlayerPlugin.ViewPriority = ViewPlayerPlugin.ViewPriority.MIDDLE

    override fun getPlayerListener(): Player.Listener {
        return object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                binding.root.isVisible = true
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        binding.root.isVisible = false
                    }

                    else -> {}
                }
            }
        }
    }

}