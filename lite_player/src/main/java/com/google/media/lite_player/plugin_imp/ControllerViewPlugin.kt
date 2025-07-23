 package com.google.media.lite_player.plugin_imp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.OptIn
import androidx.core.view.isVisible
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.ui.TimeBar
import androidx.media3.ui.TimeBar.OnScrubListener
import com.comeon.android.app.home.video.ui.kit.OnLongTouchCallback
import com.comeon.android.library.utils.DisplayUtils
import com.comeon.android.library.utils.ToastUtils
import com.google.media.lite_player.api.OnActionListener
import com.google.media.lite_player.api.controller.IPlayerController
import com.google.media.lite_player.api.controller.IPlayerControllerVisibleManager
import com.google.media.lite_player.config.Const
import com.google.media.lite_player.databinding.ViewLiteExoPlaybackControlBinding
import com.google.media.lite_player.kit.VideoOnTouchListener
import com.google.media.lite_player.kit.visible_helper.AutoHideVisibleAlphaAnimHelper
import com.google.media.lite_player.plugin.ViewPlayerPlugin
import com.google.media.lite_player.ui.PlayerControllerVisibleManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Formatter
import java.util.Locale

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-23-2025
 */
@OptIn(UnstableApi::class)
class ControllerViewPlugin(
    context: Context,
    private val scope: CoroutineScope,
    private val player: Player,
    isHideControllerOnInit: Boolean = DisplayUtils.isLandscape(context),
    private var controllerActionListener: ControllerActionListener? = null
) : ViewPlayerPlugin, IPlayerController {

    companion object {
        private const val TAG = "PlayerController"
        val ANIM_AUTO_HIDE_MILS = Const.playerConfig.controllerAutoHideAnimDuration
        val ANIM_DURATION_MILS: Long = Const.playerConfig.alphaAnimDuration
    }

    private val binding = ViewLiteExoPlaybackControlBinding.inflate(LayoutInflater.from(context))

    private val playerControllerAnimManager: IPlayerControllerVisibleManager = PlayerControllerVisibleManager(
        binding = binding,
        autoHideOnControllerVisibleMils = ANIM_AUTO_HIDE_MILS,
        controllerAnimDurationMils = ANIM_DURATION_MILS
    )

    /**
     * Lock anim helper
     */
    private val lockAnimHelper = AutoHideVisibleAlphaAnimHelper(
        view = binding.ivLockImg,
        duration = ANIM_DURATION_MILS,
        autoHideMils = ANIM_AUTO_HIDE_MILS
    )

    private var playbackSpeed = player.playbackParameters.speed

    private val touchListener = VideoOnTouchListener(object : OnLongTouchCallback {
        override fun onStart() {
            playbackSpeed = player.playbackParameters.speed
            player.playbackParameters = player.playbackParameters.withSpeed(3.0f)
            ToastUtils.showToast(context, "已切换到3.0倍速")
        }

        override fun onActionUp() {
            player.playbackParameters = player.playbackParameters.withSpeed(playbackSpeed)
            ToastUtils.showToast(context, "已切换到1.0倍速")
        }
    })

    init {
        if (isHideControllerOnInit) {
            playerControllerAnimManager.hideControllerAnim(false)
            lockAnimHelper.setVisible(visible = false, useAnim = false)
        }
        binding.exoPlay.setOnClickListener {
            if (player.isPlaying) return@setOnClickListener
            player.play()
        }
        binding.exoPause.setOnClickListener {
            if (!player.isPlaying) return@setOnClickListener
            player.pause()
        }
        binding.btnVideoReturn.setOnClickListener {
            controllerActionListener?.onAction(ControllerAction.Close())
        }
        player.addListener(object : Player.Listener {

            override fun onPlayerError(error: PlaybackException) {
                // 处理播放器错误
                binding.exoPlay.isVisible = true
                binding.exoPause.isVisible = false
                ToastUtils.showToast(binding.root.context, "播放错误: ${error.message}")
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

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                Log.d(TAG, "Playback parameters changed: ${playbackParameters.speed}x")
            }
        })
        //设置初始状态为解锁状态
        binding.ivLockImg.setCheckListener { _, isChecked ->
            if (isChecked) hideController()
            else showController()
            controllerActionListener?.onAction(ControllerAction.Lock(isChecked))
        }
        binding.clControllerView.setOnClickListener {
            //锁屏状态下 只展示锁定图标
            if (binding.ivLockImg.isChecked()) {
                lockAnimHelper.setVisible(true)
                return@setOnClickListener
            }
            if (playerControllerAnimManager.isControllerVisible()) {
                // 如果控制器遮罩层不可见，说明控制器已显示，点击时隐藏控制器
                hideController()
            } else {
                // 如果控制器遮罩层可见，说明控制器已隐藏，点击时显示控制器
                showController()
            }
        }
        binding.clControllerView.setOnTouchListener(touchListener)
        binding.exoProgress.addListener(
            object : OnScrubListener {
                override fun onScrubStart(timeBar: TimeBar, position: Long) {
//                    scrubbing = true
                    binding.exoDuration.setText(Util.getStringForTime(formatBuilder, formatter, position))
                }

                override fun onScrubMove(timeBar: TimeBar, position: Long) {
                    binding.exoDuration.setText(Util.getStringForTime(formatBuilder, formatter, position))
                }

                override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
//                    scrubbing = false
                    if (!canceled && player != null) {
//                        seekToTimeBarPosition(player, position)
                    }
                }
            }
        )
        scope.launch {
            repeat(100) {
                delay(1000)
                updateProgress()
            }
        }
    }

    override fun onCreateView(context: Context): View {
        return binding.root
    }

    override fun viewPriority(): ViewPlayerPlugin.ViewPriority = ViewPlayerPlugin.ViewPriority.LOW

    override fun showController() {
        //如果锁定图标被选中，则不显示控制器
        if (binding.ivLockImg.isChecked()) return
        lockAnimHelper.setVisible(true)
        playerControllerAnimManager.showControllerAnim()
    }

    override fun hideController() {
        //隐藏锁定图标
        lockAnimHelper.setVisible(false)
        playerControllerAnimManager.hideControllerAnim()
    }

    override fun getPlayerListener(): Player.Listener {
        return object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                if (events.containsAny(
                        Player.EVENT_PLAYBACK_STATE_CHANGED, Player.EVENT_PLAY_WHEN_READY_CHANGED, Player.EVENT_IS_PLAYING_CHANGED
                    )
                ) {
                    updateProgress()
                }
                if (events.containsAny(Player.EVENT_POSITION_DISCONTINUITY, Player.EVENT_TIMELINE_CHANGED)) {
                    updateTimeLine()
                }
            }
        }
    }

    private var currentPosition: Long = 0
    private var currentBufferedPosition: Long = 0
    private val formatBuilder = StringBuilder()
    private val formatter = Formatter(formatBuilder, Locale.getDefault())
    private val window: Timeline.Window = Timeline.Window()

    @OptIn(UnstableApi::class)
    private fun updateProgress() {
        binding.exoProgress.also {
            var bufferedPosition: Long = 0
            val position = player.contentPosition
            bufferedPosition = player.contentBufferedPosition
            val positionChanged = position != currentPosition
            val bufferedPositionChanged = bufferedPosition != currentBufferedPosition
            currentPosition = position
            currentBufferedPosition = bufferedPosition


            binding.exoPosition.text = Util.getStringForTime(formatBuilder, formatter, position)

            it.setPosition(position)
            it.setBufferedPosition(bufferedPosition)
        }
    }

    @OptIn(UnstableApi::class) private fun updateTimeLine() {
        binding.exoDuration.setText(Util.getStringForTime(formatBuilder, formatter, player.contentDuration))
    }

}

fun interface ControllerActionListener : OnActionListener<ControllerAction>

sealed class ControllerAction {
    class Close : ControllerAction()
    data class Lock(val isLock: Boolean) : ControllerAction()
}