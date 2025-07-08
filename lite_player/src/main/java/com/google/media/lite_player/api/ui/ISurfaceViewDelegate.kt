package com.google.media.lite_player.api.ui

import android.view.View
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import com.google.media.lite_player.api.ILitePlayer

/**
 * @author liulinjie
 * @since 2025-07-09
 */
interface ISurfaceViewDelegate: ILitePlayer {
    fun getSurfaceView(): View

    fun setSurfaceViewVisibility(visibility: Int)

    fun updateAspectRatio()

    companion object {
        fun computeVideoAspectRatio(player: Player?): Float {
            val videoSize = player?.videoSize ?: VideoSize.UNKNOWN
            val width = videoSize.width
            val height = videoSize.height
            return if (height == 0 || width == 0) 0f else width * videoSize.pixelWidthHeightRatio / height
        }
    }
}

fun ISurfaceViewDelegate.onVideoSizeChanged(player: Player?, videoSize: VideoSize) {
    if (videoSize == VideoSize.UNKNOWN || player == null || player.playbackState == Player.STATE_IDLE) {
        return
    }
    updateAspectRatio()
}