package com.comeon.android.library.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.comeon.android.library.R

/**
 * 选中状态切换imageView
 * 可以按照CheckBox的方式使用，支持选中状态切换image。
 *
 * example:
 *
 * 1.
 * drawable/check_bg.xml
 * <selector xmlns:android="http://schemas.android.com/apk/res/android">
 *     <item android:state_checked="false" android:drawable="@drawable/check"/>
 *     <item android:state_checked="true" android:drawable="@drawable/uncheck"/>
 * </selector>
 *
 * 2.
 * <com.comeon.android.library.ui.widget.ImageCheckView>
 *  android:src="@drawable/check_bg"
 * </com.comeon.android.library.ui.widget.ImageCheckView>
 *
 */
class ImageCheckView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var isChecked = false

    private val CHECKED_STATE_SET = intArrayOf(
        android.R.attr.state_checked
    )

    private var onCheckListener: OnCheckListener? = null

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ImageCheckView).apply {
            val checked = getBoolean(R.styleable.ImageCheckView_check, false)
            setChecked(checked)
            recycle()
        }
        setOnClickListener {
            toggle()
        }
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray? {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }

    fun setChecked(checked: Boolean) {
        if (isChecked != checked) {
            isChecked = checked
            refreshDrawableState()
            onCheckListener?.onCheckedChanged(this, checked)
        }
    }

    fun toggle() {
        setChecked(!isChecked)
    }

    fun isChecked(): Boolean {
        return isChecked
    }

    fun setCheckListener(onCheckListener: OnCheckListener?) {
        this.onCheckListener = onCheckListener
    }

    fun interface OnCheckListener {
        fun onCheckedChanged(imageCheckView: ImageCheckView, isChecked: Boolean)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onCheckListener = null
    }

}