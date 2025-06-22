package com.comeon.android.library.ui.fresco.ext

import android.net.Uri
import com.comeon.android.library.ui.fresco.image.WrapContentControllerListener
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView

fun SimpleDraweeView.setDynamicImageUrl(url: String, isAutoPlay: Boolean = true) {
    controller = Fresco.newDraweeControllerBuilder()
        .setOldController(controller)
        .setControllerListener(WrapContentControllerListener(this))
        .setUri(Uri.parse(url))
        .setAutoPlayAnimations(isAutoPlay)
        .build()
}