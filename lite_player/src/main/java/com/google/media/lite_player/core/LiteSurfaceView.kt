package com.google.media.lite_player.core

import android.content.Context
import android.os.Build
import android.view.SurfaceView
import android.view.View
import androidx.annotation.DoNotInline
import androidx.annotation.RequiresApi
import androidx.media3.common.C
import androidx.media3.common.Player
import com.google.media.lite_player.api.ILitePlayer
import com.google.media.lite_player.api.ui.IRatioFrameView
import com.google.media.lite_player.api.ui.ISurfaceViewDelegate

/**
 * @author liulinjie
 * @since 2025-07-09
 */
class LiteSurfaceView(
    context: Context,
    private val contentFrame: IRatioFrameView,
) : ISurfaceViewDelegate, ILitePlayer {

    private var player: Player? = null

    private val surfaceView: SurfaceView = SurfaceView(context).also {
        if (Build.VERSION.SDK_INT >= 34) {
            Api34.setSurfaceLifecycleToFollowsAttachment(it)
        }
    }

    override fun getSurfaceView(): View {
        return surfaceView
    }

    override fun setSurfaceViewVisibility(visibility: Int) {
        val surfaceView = getSurfaceView()
        if (surfaceView is SurfaceView) {
            // Work around https://github.com/google/ExoPlayer/issues/3160.
            surfaceView.visibility = visibility
        }
    }

    override fun updateAspectRatio() {
        val videoAspectRatio = ISurfaceViewDelegate.computeVideoAspectRatio(this.player)
        contentFrame.setVideoAspectRatio(videoAspectRatio)
    }

    @RequiresApi(34)
    private object Api34 {
        @DoNotInline
        fun setSurfaceLifecycleToFollowsAttachment(surfaceView: SurfaceView) {
            surfaceView.setSurfaceLifecycle(SurfaceView.SURFACE_LIFECYCLE_FOLLOWS_ATTACHMENT)
        }
    }

    override fun setPlayer(player: Player?) {
        player ?: return
        if (this.player == player) return
        this.player?.let {
            // If the player is already set, remove the old video surface.
            if (it.isCommandAvailable(Player.COMMAND_SET_VIDEO_SURFACE)) {
                it.setVideoSurfaceView(null)
            }
            it.clearVideoSurfaceView(surfaceView)
        }
        this.player = player
        if (player.isCommandAvailable(Player.COMMAND_SET_VIDEO_SURFACE)) {
            player.setVideoSurfaceView(null)
        }
        player.setVideoSurfaceView(surfaceView)
        if (player.isCommandAvailable(Player.COMMAND_SET_VIDEO_SURFACE)) {
            player.setVideoSurfaceView(surfaceView)
            if (!player.isCommandAvailable(Player.COMMAND_GET_TRACKS) || player.currentTracks.isTypeSupported(
                    C.TRACK_TYPE_VIDEO
                )
            ) {
                // If the player already is or was playing a video, onVideoSizeChanged isn't called.
                updateAspectRatio()
            }
        }
    }
}