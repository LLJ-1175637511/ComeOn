package com.google.media.lite_player.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.google.media.lite_player.databinding.ViewPlayerLoadingLayerBinding

/**
 * @author liulinjie @ Zhihu Inc.
 * @since 07-21-2025
 */
class PlayerLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = ViewPlayerLoadingLayerBinding.inflate(LayoutInflater.from(context), this, true)

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == VISIBLE) {
            binding.animLoadingView.startAnimation()
        } else {
            binding.animLoadingView.stopAnimation()
        }
    }

}