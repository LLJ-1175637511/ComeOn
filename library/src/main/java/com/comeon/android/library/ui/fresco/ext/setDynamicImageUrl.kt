package com.comeon.android.library.ui.fresco.ext

import android.net.Uri
import com.comeon.android.library.ui.fresco.image.WrapContentControllerListener
import com.facebook.common.util.UriUtil
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder
import com.facebook.drawee.controller.AbstractDraweeController
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import java.net.URI

fun SimpleDraweeView.setDynamicImageUrl(uri: Uri, isAutoPlay: Boolean = true) {
    controller = configDynamicController(uri) {
        controllerListener = WrapContentControllerListener(this@setDynamicImageUrl)
        autoPlayAnimations = isAutoPlay
    }
}

fun SimpleDraweeView.setDynamicImageUrl(url: String, isAutoPlay: Boolean = true) {
    setDynamicImageUrl(Uri.parse(url), isAutoPlay)
}

fun SimpleDraweeView.setDynamicImageUrl(res: Int, isAutoPlay: Boolean = true) {
    setDynamicImageUrl(UriUtil.getUriForResourceId(res), isAutoPlay)
}

private fun SimpleDraweeView.configDynamicController(uri: Uri, block: PipelineDraweeControllerBuilder.() -> Unit): DraweeController {
    return Fresco.newDraweeControllerBuilder().apply {
        oldController = controller
        autoPlayAnimations = true
        setUri(uri)
        block()
    }.build()
}