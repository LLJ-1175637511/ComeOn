package com.google.media.lite_player.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
public class BouncingBallsLoader extends View {

    private static final int BALL_COUNT = 3;
    private static final int BALL_SIZE_PX = 12; // 小球大小12像素
    private static final int MIN_VIEW_SIZE_PX = 40; // 最小宽高40像素
    private static final int JUMP_DURATION = 1000; // 跳起时间增加一倍：1000ms
    private static final int FALL_DURATION = 600;  // 落地时间增加一倍：600ms
    private static final int TOTAL_DURATION = JUMP_DURATION + FALL_DURATION; // 总周期1600ms
    private static final int DELAY_BETWEEN_BALLS = 200; // 小球间的延迟200ms
    private static final int SPACING_DP = 8; // 小球间距8dp

    private final Paint paint = new Paint();
    private final float[] ballY = new float[BALL_COUNT]; // 小球的Y坐标
    private float centerY; // 中心Y坐标
    private float startX; // 起始X坐标
    private float maxJumpHeight; // 最大跳起高度
    private float spacingPx; // 小球间距（像素）
    private ValueAnimator animator;

    public BouncingBallsLoader(Context context) {
        super(context);
        init(context);
    }

    public BouncingBallsLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

        // 将8dp转换为像素
        spacingPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                SPACING_DP,
                context.getResources().getDisplayMetrics()
        );

        // 设置小球初始位置
        for (int i = 0; i < BALL_COUNT; i++) {
            ballY[i] = 0;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 设置最小宽高为40px
        int minSize = MIN_VIEW_SIZE_PX;
        int width = resolveSize(minSize, widthMeasureSpec);
        int height = resolveSize(minSize, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerY = h / 2f;
        maxJumpHeight = h / 4f; // 跳起高度为视图高度的1/4

        // 计算总宽度（小球直径 + 间距）
        float totalWidth = (BALL_SIZE_PX * 2 * BALL_COUNT) + (spacingPx * (BALL_COUNT - 1));

        // 计算起始X位置（水平居中）
        startX = (w - totalWidth) / 2f + BALL_SIZE_PX;

        setupAnimation();
    }

    private void setupAnimation() {
        if (animator != null) {
            animator.cancel();
        }

        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(TOTAL_DURATION);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            updateBallPositions(progress);
            invalidate();
        });
        animator.start();
    }

    private void updateBallPositions(float globalProgress) {
        // 每个小球的时间段
        float segment = 1.0f / BALL_COUNT;

        for (int i = 0; i < BALL_COUNT; i++) {
            // 计算当前小球的局部进度
            float start = i * segment;
            float end = start + segment;

            float localProgress;
            if (globalProgress >= start && globalProgress < end) {
                localProgress = (globalProgress - start) / segment;
            } else if (globalProgress >= end) {
                localProgress = 1f; // 小球完成动画
            } else {
                localProgress = 0f; // 小球未开始动画
            }

            // 使用正弦函数创建平滑弹跳效果
            if (localProgress > 0 && localProgress <= 0.5f) {
                // 上升阶段
                ballY[i] = centerY - (float) (Math.sin(localProgress * Math.PI) * maxJumpHeight);
            } else if (localProgress > 0.5f && localProgress <= 1f) {
                // 下降阶段
                ballY[i] = centerY - (float) (Math.sin((1 - localProgress) * Math.PI) * maxJumpHeight);
            } else {
                ballY[i] = centerY; // 静止在地面
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < BALL_COUNT; i++) {
            // 计算X位置：起始位置 + (小球直径 + 间距) * 索引
            float x = startX + i * (BALL_SIZE_PX * 2 + spacingPx);
            canvas.drawCircle(x, ballY[i], BALL_SIZE_PX, paint);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (animator != null && !animator.isRunning()) {
            animator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            animator.cancel();
        }
    }
}