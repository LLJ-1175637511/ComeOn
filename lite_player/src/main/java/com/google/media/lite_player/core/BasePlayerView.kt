package com.google.media.lite_player.core

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.media3.common.Player
import com.google.media.lite_player.api.ui.IBasePlayerView
import com.google.media.lite_player.api.controller.IPlayerController

abstract class BasePlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IBasePlayerView {

    protected var player: Player? = null

    val surfaceViewDelegate by lazy { buildSurfaceViewDelegate() }

    val controller: IPlayerController by lazy { buildPlayerController() }

}
