package com.jeanboy.app.awesome.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jeanboy on 2016/11/22.
 */

public class DashBoardView extends View {

    private static final String TAG = DashBoardView.class.getSimpleName();

    private Paint mMeasurePaint;
    private Paint mBoardPaint;
    private Path mMeasurePath;
    private Path mBoardPath;
    private int mWidth, mHeight;
    private float strokeWidth = 8f;
    private float startAngle;
    private float sweepAngle = 360f;
    private float boardSweepAngle;
    private float boardCurrent = 0f;
    private int level;

    //9-10#21bb20        7-8#8bd43c    5-6#d3ac29   1-4#f34949
    private static final int COLOR_LEVEL1_4 = 0xfff34949;
    private static final int COLOR_LEVEL5_6 = 0xffd3ac29;
    private static final int COLOR_LEVEL7_8 = 0xff8bd43c;
    private static final int COLOR_LEVEL9_10 = 0xff21bb20;
    private static final int COLOR_MEASURE = 0x4dffffff;

    private ValueAnimator valueAnimator;
    private boolean isAnimating = false;

    public DashBoardView(Context context) {
        this(context, null);
    }

    public DashBoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        initAnim();
    }

    private void initView() {
        mMeasurePaint = new Paint();
        mMeasurePaint.setAntiAlias(true);
        mMeasurePaint.setStyle(Paint.Style.STROKE);
        mMeasurePaint.setStrokeCap(Paint.Cap.ROUND);

        mBoardPaint = new Paint();
        mBoardPaint.setAntiAlias(true);
        mBoardPaint.setStyle(Paint.Style.STROKE);
        mBoardPaint.setStrokeCap(Paint.Cap.ROUND);

        mMeasurePath = new Path();
        mBoardPath = new Path();


    }

    private void initAnim() {
        valueAnimator = ValueAnimator.ofFloat(0, 1f).setDuration(1500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                boardCurrent = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (mBoardPath != null) {
                    mBoardPath.reset();
                }
                boardCurrent = 0f;
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                boardCurrent = 1f;
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                boardCurrent = 1f;
                isAnimating = false;
            }
        });
        valueAnimator.setStartDelay(500);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawColor(Color.TRANSPARENT);

        float mRadius = Math.min(mWidth, mHeight) / 2 - strokeWidth;

        RectF rectF = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        drawMeasureView(canvas, rectF);
        drawBoardView(canvas, rectF);
    }

    private void drawMeasureView(Canvas canvas, RectF rectF) {
        mMeasurePaint.setColor(COLOR_MEASURE);
        mMeasurePaint.setStrokeWidth(strokeWidth);
        mMeasurePath.addArc(rectF, startAngle, sweepAngle);
        canvas.drawPath(mMeasurePath, mMeasurePaint);
    }

    private void drawBoardView(Canvas canvas, RectF rectF) {
        mBoardPaint.setColor(getBoardColor());
        mBoardPaint.setStrokeWidth(strokeWidth);
        mBoardPath.addArc(rectF, startAngle, boardCurrent * boardSweepAngle);
        canvas.drawPath(mBoardPath, mBoardPaint);
    }

    private int getBoardColor() {
        if (level > 0 && level <= 4) {
            return COLOR_LEVEL1_4;
        } else if (level > 4 && level <= 6) {
            return COLOR_LEVEL5_6;
        } else if (level > 6 && level <= 8) {
            return COLOR_LEVEL7_8;
        } else if (level > 8 && level <= 10) {
            return COLOR_LEVEL9_10;
        } else {
            return Color.TRANSPARENT;
        }
    }


    public void setStrokeWidth(float dp) {
        this.strokeWidth = dip2px(getContext(), dp);
    }

    /**
     * 设置仪表盘度数
     *
     * @param showAngle
     */
    public void setShowAngle(float showAngle) {
        this.sweepAngle = showAngle;
        this.startAngle = 90f + (360f - sweepAngle) / 2;
    }

    /**
     * 设置进度百分比
     *
     * @param percent
     */
    public void setBoardPercent(float percent) {
        this.boardSweepAngle = sweepAngle * percent;
    }

    /**
     * 设置天气等级
     *
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    public void showWithAnim() {
        if (isAnimating) return;
        if (valueAnimator != null) {
            boardCurrent = 0f;
            mBoardPath.reset();
            valueAnimator.start();
        }
    }

    public void clearAnim() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            boardCurrent = 1f;
        }
    }


    private static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

