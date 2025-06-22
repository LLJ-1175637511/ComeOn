package com.comeon.android.library.ui.fresco.image

import android.graphics.drawable.Animatable
import android.view.ViewGroup
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import java.lang.ref.WeakReference

open class WrapContentControllerListener(
    draweeView: SimpleDraweeView
): BaseControllerListener<ImageInfo>() {
    private val weakReference = WeakReference<SimpleDraweeView>(draweeView)
    override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
        super.onFinalImageSet(id, imageInfo, animatable)
        weakReference.get()?.apply {
            if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT && layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                layoutParams = layoutParams.also {
                    it.width = imageInfo?.width ?: 0
                    it.height = imageInfo?.height ?: 0
                }
                requestLayout()
            }
        }
    }
}