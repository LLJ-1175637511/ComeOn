package com.google.media.lite_player.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class BouncingBallsLoader @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {
        private const val BALL_COUNT = 3
        private const val BALL_SIZE_PX = 12
        private const val MIN_VIEW_SIZE_PX = 40
        private const val JUMP_DURATION = 1000
        private const val FALL_DURATION = 600
        private const val TOTAL_DURATION = JUMP_DURATION + FALL_DURATION
        private const val DELAY_BETWEEN_BALLS = 200
        private const val SPACING_DP = 8
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F4F6F9")
        style = Paint.Style.FILL
    }
    private val ballY = FloatArray(BALL_COUNT)
    private var centerY = 0f
    private var startX = 0f
    private var maxJumpHeight = 0f
    private var spacingPx = 0f
    private var animator: ValueAnimator? = null

    init {
        spacingPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            SPACING_DP.toFloat(),
            context.resources.displayMetrics
        )
        for (i in 0 until BALL_COUNT) {
            ballY[i] = 0f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minSize = MIN_VIEW_SIZE_PX
        val width = resolveSize(minSize, widthMeasureSpec)
        val height = resolveSize(minSize, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerY = h * 3f / 4f
        maxJumpHeight = h / 4f
        val totalWidth = (BALL_SIZE_PX * 2 * BALL_COUNT) + (spacingPx * (BALL_COUNT - 1))
        startX = (w - totalWidth) / 2f + BALL_SIZE_PX
        setupAnimation()
    }

    private fun setupAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = TOTAL_DURATION.toLong()
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                updateBallPositions(progress)
                invalidate()
            }
            start()
        }
    }

    private fun updateBallPositions(globalProgress: Float) {
        val segment = 1.0f / BALL_COUNT
        for (i in 0 until BALL_COUNT) {
            val start = i * segment
            val end = start + segment
            val localProgress = when {
                globalProgress >= start && globalProgress < end -> (globalProgress - start) / segment
                globalProgress >= end -> 1f
                else -> 0f
            }
            ballY[i] = when {
                localProgress > 0f && localProgress <= 0.5f ->
                    centerY - (Math.sin((localProgress * Math.PI)).toFloat() * maxJumpHeight)
                localProgress > 0.5f && localProgress <= 1f ->
                    centerY - (Math.sin(((1 - localProgress) * Math.PI)).toFloat() * maxJumpHeight)
                else -> centerY
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until BALL_COUNT) {
            val x = startX + i * (BALL_SIZE_PX * 2 + spacingPx)
            canvas.drawCircle(x, ballY[i], BALL_SIZE_PX.toFloat(), paint)
        }
    }

    fun startAnimation() {
        if (animator == null || !animator!!.isRunning) {
            setupAnimation()
        }
    }

    fun stopAnimation() {
        animator?.cancel()
        animator = null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (animator != null && animator?.isRunning == false) {
            animator?.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}