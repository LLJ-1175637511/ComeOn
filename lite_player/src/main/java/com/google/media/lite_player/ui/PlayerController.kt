package com.google.media.lite_player.ui

import android.util.Log
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import com.comeon.android.app.home.video.ui.kit.OnLongTouchCallback
import com.comeon.android.app.home.video.ui.kit.VideoOnTouchListener
import com.comeon.android.library.utils.DisplayUtils
import com.comeon.android.library.utils.ToastUtils
import com.google.media.lite_player.api.controller.ControllerActionListener
import com.google.media.lite_player.api.controller.OnViewVisibleListener
import com.google.media.lite_player.api.controller.IPlayerControllerVisibleManager
import com.google.media.lite_player.api.controller.IPlayerController
import com.google.media.lite_player.databinding.ViewLiteExoPlaybackControlBinding
import com.google.media.lite_player.kit.AutoHideVisibleAlphaAnimHelper

sealed class ControllerAction {
    class Close : ControllerAction()
    data class Lock(val isLock: Boolean) : ControllerAction()
}

open class PlayerController(
    val binding: ViewLiteExoPlaybackControlBinding,
    private val playerControllerAnimManager: IPlayerControllerVisibleManager = PlayerControllerVisibleManager(
        binding = binding,
        autoHideOnControllerVisibleMils = ANIM_AUTO_HIDE_MILS,
        controllerAnimDurationMils = ANIM_DURATION_MILS
    ),
    isHideControllerOnInit: Boolean = DisplayUtils.isLandscape(binding.root.context)
) : IPlayerController {

    companion object {
        private const val TAG = "PlayerController"
        const val ANIM_AUTO_HIDE_MILS = 2000L
        const val ANIM_DURATION_MILS: Long = 300L
    }

    private val lockAnimHelper = AutoHideVisibleAlphaAnimHelper(
        view = binding.ivLockImg,
        duration = ANIM_DURATION_MILS,
        autoHideMils = ANIM_AUTO_HIDE_MILS
    )

    private val controllerActionListeners = mutableMapOf<String, ControllerActionListener>()

    private val lockViewVisibleListener = OnViewVisibleListener { isVisible -> lockAnimHelper.setVisible(isVisible) }

    init {
        if (isHideControllerOnInit) {
            playerControllerAnimManager.hideControllerAnim(false)
            lockAnimHelper.setVisible(isVisible = false, useAnim = false)
        }
    }

    override fun registerControllerCallbacks(controllerActionListener: ControllerActionListener) {
        val key = controllerActionListener.hashCode()
        controllerActionListeners[key.toString()] = controllerActionListener
    }

    override fun unregisterControllerCallbacks(controllerActionListener: ControllerActionListener) {
        val key = controllerActionListener.hashCode().toString()
        controllerActionListeners.remove(key)
    }

    override fun setPlayer(player: Player?) {
        if (player == null) {
            hideController()
            return
        }
        playerControllerAnimManager.registerControllerVisibleListener(lockViewVisibleListener)
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
            if (player.isPlaying) return@setOnClickListener
            player.play()
        }
        binding.exoPause.setOnClickListener {
            if (!player.isPlaying) return@setOnClickListener
            player.pause()
        }
        binding.btnVideoReturn.setOnClickListener {
            notifyControllerAction(ControllerAction.Close())
        }
        player.addListener(object : Player.Listener {

            override fun onPlayerError(error: PlaybackException) {
                // 处理播放器错误
                binding.exoPlay.isVisible = true
                binding.exoPause.isVisible = false
                ToastUtils.showToast(binding.root.context, "播放错误: ${error.message}")
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        // 播放器处于空闲状态
                        Log.d(TAG, "Player is idle")
                    }

                    Player.STATE_BUFFERING -> {
                        // 正在缓冲数据
                        Log.d(TAG, "Player is buffering")
                    }

                    Player.STATE_READY -> {
                        // 可以开始播放
                        Log.d(TAG, "Player is ready")
                    }

                    Player.STATE_ENDED -> {
                        // 播放结束
                        Log.d(TAG, "Player has ended playback")
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

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                Log.d(TAG, "Playback parameters changed: ${playbackParameters.speed}x")
            }
        })
        (binding.root.parent as ViewGroup).setOnTouchListener(VideoOnTouchListener(object :
            OnLongTouchCallback {
            override fun onStart() {
                player.playbackParameters = player.playbackParameters.withSpeed(2.0f)
                ToastUtils.showToast(binding.root.context, "已切换到2.0倍速")
            }

            override fun onActionUp() {
                player.playbackParameters = player.playbackParameters.withSpeed(1.0f)
                ToastUtils.showToast(binding.root.context, "已切换到1.0倍速")
            }
        }))
        //设置初始状态为解锁状态
        binding.ivLockImg.setCheckListener { _, isChecked ->
            if (isChecked) hideController()
            else showController()
            notifyControllerAction(ControllerAction.Lock(isChecked))
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
    }

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

    override fun release() {
        controllerActionListeners.clear()
    }

    private fun notifyControllerAction(action: ControllerAction) {
        controllerActionListeners.values.forEach {
            it.onAction(action)
        }
    }
}