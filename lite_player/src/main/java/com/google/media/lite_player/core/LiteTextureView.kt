package com.google.media.lite_player.core

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.view.TextureView
import android.view.View
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import com.google.media.lite_player.api.ILitePlayer
import com.google.media.lite_player.api.ui.IRatioFrameView
import com.google.media.lite_player.api.ui.ISurfaceViewDelegate

/**
 * @author liulinjie
 * @since 2025-07-09
 */
class LiteTextureView(
    context: Context,
    private val contentFrame: IRatioFrameView
) : ISurfaceViewDelegate, ILitePlayer {

    private var player: Player? = null

    private val surfaceView: TextureView = TextureView(context)

    private var textureViewRotation = 0

    private val componentListener = object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            v: View?,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            applyTextureViewRotation(surfaceView, textureViewRotation)
        }
    }

    override fun getSurfaceView(): View {
        return surfaceView
    }

    override fun setSurfaceViewVisibility(visibility: Int) {}

    override fun updateAspectRatio() {
        var videoAspectRatio = ISurfaceViewDelegate.computeVideoAspectRatio(this.player)

        if (surfaceView is TextureView) {
            val videoSize = player?.videoSize ?: VideoSize.UNKNOWN
            val unappliedRotationDegrees = videoSize.unappliedRotationDegrees
            // Try to apply rotation transformation when our surface is a TextureView.
            if (videoAspectRatio > 0
                && (unappliedRotationDegrees == 90 || unappliedRotationDegrees == 270)
            ) {
                // We will apply a rotation 90/270 degree to the output texture of the TextureView.
                // In this case, the output video's width and height will be swapped.
                videoAspectRatio = 1 / videoAspectRatio
            }
            if (textureViewRotation != 0) {
                surfaceView.removeOnLayoutChangeListener(componentListener)
            }
            textureViewRotation = unappliedRotationDegrees
            if (textureViewRotation != 0) {
                // The texture view's dimensions might be changed after layout step.
                // So add an OnLayoutChangeListener to apply rotation after layout step.
                surfaceView.addOnLayoutChangeListener(componentListener)
            }
            applyTextureViewRotation(surfaceView, textureViewRotation)
        }

        contentFrame.setVideoAspectRatio(videoAspectRatio)
    }

    private fun applyTextureViewRotation(textureView: TextureView, textureViewRotation: Int) {
        val transformMatrix = Matrix()
        val textureViewWidth = textureView.width.toFloat()
        val textureViewHeight = textureView.height.toFloat()
        if (textureViewWidth != 0f && textureViewHeight != 0f && textureViewRotation != 0) {
            val pivotX = textureViewWidth / 2
            val pivotY = textureViewHeight / 2
            transformMatrix.postRotate(textureViewRotation.toFloat(), pivotX, pivotY)

            // After rotation, scale the rotated texture to fit the TextureView size.
            val originalTextureRect = RectF(0f, 0f, textureViewWidth, textureViewHeight)
            val rotatedTextureRect = RectF()
            transformMatrix.mapRect(rotatedTextureRect, originalTextureRect)
            transformMatrix.postScale(
                textureViewWidth / rotatedTextureRect.width(),
                textureViewHeight / rotatedTextureRect.height(),
                pivotX,
                pivotY
            )
        }
        textureView.setTransform(transformMatrix)
    }

    override fun setPlayer(player: Player?) {
        player ?: return
        if (this.player == player) return
        this.player?.let {
            // If the player is already set, remove the old video surface.
            if (it.isCommandAvailable(Player.COMMAND_SET_VIDEO_SURFACE)) {
                it.setVideoSurfaceView(null)
            }
            it.clearVideoTextureView(surfaceView)
        }
        this.player = player
        if (player.isCommandAvailable(Player.COMMAND_SET_VIDEO_SURFACE)) {
            player.setVideoTextureView(null)
        }
        player.setVideoTextureView(surfaceView)
        if (player.isCommandAvailable(Player.COMMAND_SET_VIDEO_SURFACE)) {
            player.setVideoTextureView(surfaceView)
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