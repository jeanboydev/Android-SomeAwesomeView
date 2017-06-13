package com.jeanboy.app.awesome.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jeanboy.app.awesome.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeanboy on 2016/11/22.
 */

public class RainbowLineView extends View {

    private Paint mLinePaint;
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private Path linePath;

    private int mWidth, mHeight;
    private float strokeWidth = 3f;

    private float mRadius;
    private float lineAngleStart;
    private float lineAngleEnd;
    private float hourTextY;

    //9-10#21bb20        7-8#8bd43c    5-6#d3ac29   1-4#f34949
    private static final int COLOR_LEVEL1_4 = 0xfff34949;
    private static final int COLOR_LEVEL5_6 = 0xffd3ac29;
    private static final int COLOR_LEVEL7_8 = 0xff8bd43c;
    private static final int COLOR_LEVEL9_10 = 0xff21bb20;
    private static final int COLOR_LINE = 0xffffffff;

    private static final int ITEM_MAX = 5;//线上圆圈总数

    private float defaultTextSize = 12f;
    private float hourTextSize = 16f;
    private PointF[] pointArr = new PointF[ITEM_MAX];
    private float[] circleRadiusArr = new float[]{10f, 12.5f, 15f};
    private float[] circleTextSizeArr = new float[]{11f, 15f, 16f};
    private float circleTextBottomMargin = 32f;
    private float circleTextPadding = 5f;

    private List<RainbowLineData> dataList = new ArrayList<>();


    private ValueAnimator valueAnimator;
    private float currentValue = 0f;
    private boolean isAnimating = false;
    private boolean isShown = false;

    public RainbowLineView(Context context) {
        this(context, null);
    }

    public RainbowLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RainbowLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        initAnim();
    }

    private void initView() {
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);


        linePath = new Path();

        defaultTextSize = sp2px(getContext(), defaultTextSize);
        hourTextSize = sp2px(getContext(), hourTextSize);
        strokeWidth = dip2px(getContext(), strokeWidth);
        circleTextBottomMargin = dip2px(getContext(), circleTextBottomMargin);
        circleTextPadding = dip2px(getContext(), circleTextPadding);
    }

    private void initAnim() {
        valueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(1500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (linePath != null) {
                    linePath.reset();
                }
                currentValue = 0f;
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                currentValue = 1f;
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

        setupData();
    }

    private void setupData() {
        //弧线半径
        mRadius = mWidth / 3 * 4;

        double lineAngle = Math.asin(mWidth / 2 / mRadius) * 2;
        double lineAngleCell = lineAngle / (ITEM_MAX + 1);
        lineAngleStart = (float) ((Math.PI / 2 * 3 - lineAngle / 2) / Math.PI * 180);
        lineAngleEnd = (float) (lineAngle / Math.PI * 180);
        //底部时间文本y坐标
        hourTextY = (float) (mRadius - mRadius * Math.cos(lineAngle / 2));

        //线上圆点
        PointF point1 = new PointF();
        point1.x = (float) (-Math.sin(lineAngleCell * 2) * mRadius);
        point1.y = (float) (mRadius - Math.cos(lineAngleCell * 2) * mRadius);
        pointArr[0] = point1;

        PointF point2 = new PointF();
        point2.x = (float) (-Math.sin(lineAngleCell) * mRadius);
        point2.y = (float) (mRadius - Math.cos(lineAngleCell) * mRadius);
        pointArr[1] = point2;

        PointF point3 = new PointF(0f, 0f);
        pointArr[2] = point3;

        PointF point4 = new PointF();
        point4.x = (float) (Math.sin(lineAngleCell) * mRadius);
        point4.y = (float) (mRadius - Math.cos(lineAngleCell) * mRadius);
        pointArr[3] = point4;

        PointF point5 = new PointF();
        point5.x = (float) (Math.sin(lineAngleCell * 2) * mRadius);
        point5.y = (float) (mRadius - Math.cos(lineAngleCell * 2) * mRadius);
        pointArr[4] = point5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawColor(Color.TRANSPARENT);

        if (!isShown) return;

        if (dataList.isEmpty()) return;

        mLinePaint.setColor(COLOR_LINE);
        mLinePaint.setStrokeWidth(strokeWidth);

        mCirclePaint.setColor(COLOR_LINE);

        mTextPaint.setColor(COLOR_LINE);
        mTextPaint.setStrokeWidth(2f);
        mTextPaint.setTextSize(sp2px(getContext(), 11f));
        mTextPaint.setStyle(Paint.Style.FILL);

        drawLine(canvas);

        int index = (int) (currentValue / (1f / (ITEM_MAX + 1)));


        for (int i = 0; i < index - 1; i++) {
            drawLineCircle(canvas, i);
        }

        if (currentValue >= 1f) {
            drawText(canvas);
        }
    }

    //绘制弧线
    private void drawLine(Canvas canvas) {
        RectF rectF = new RectF(-mRadius, 0, mRadius, mRadius * 2);
        linePath.addArc(rectF, lineAngleStart, lineAngleEnd * currentValue);
        canvas.drawPath(linePath, mLinePaint);
    }

    //绘制线上圆
    private void drawLineCircle(Canvas canvas, int index) {
        Path path = new Path();
        path.addCircle(pointArr[index].x, pointArr[index].y, getCircleRadius(index), Path.Direction.CW);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getLevelColor(dataList.get(index).getLevel()));
        canvas.drawPath(path, paint);
    }

    //获取圆半径
    private float getCircleRadius(int index) {
        if (index == 2) {
            return dip2px(getContext(), circleRadiusArr[2]);
        } else if (index == 3 || index == 1) {
            return dip2px(getContext(), circleRadiusArr[1]);
        } else {
            return dip2px(getContext(), circleRadiusArr[0]);
        }
    }

    //获取圆内文本大小
    private float getRadiusTextSize(int index) {
        if (index == 2) {
            return sp2px(getContext(), circleTextSizeArr[2]);
        } else if (index == 3 || index == 1) {
            return sp2px(getContext(), circleTextSizeArr[1]);
        } else {
            return sp2px(getContext(), circleTextSizeArr[0]);
        }
    }

    //获取天气等级背景颜色
    private int getLevelColor(int level) {
        if (level >= 1 && level <= 4) {
            return COLOR_LEVEL1_4;
        } else if (level >= 5 && level <= 6) {
            return COLOR_LEVEL5_6;
        } else if (level >= 7 && level <= 8) {
            return COLOR_LEVEL7_8;
        } else if (level >= 9 && level <= 10) {
            return COLOR_LEVEL9_10;
        } else {
            return Color.TRANSPARENT;
        }
    }

    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offY = fontTotalHeight / 2 - fontMetrics.bottom;
        for (int i = 0; i < ITEM_MAX; i++) {
            //绘制圆内文本
            mTextPaint.setTextSize(getRadiusTextSize(i));
            canvas.drawText(String.valueOf(dataList.get(i).getLevel()), pointArr[i].x, pointArr[i].y + offY, mTextPaint);
            //绘制底部时间文本
            mTextPaint.setTextSize(hourTextSize);
            canvas.drawText(dataList.get(i).getHour(), pointArr[i].x, hourTextY + circleTextBottomMargin / 3 * 2, mTextPaint);
            if (i > 0 && i < (ITEM_MAX - 1)) {
                //测量温度tips文本宽度
                float textW = mTextPaint.measureText(dataList.get(i).getTemperatureTip());
                //绘制中间温度tips背景图
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_wendu);
                NinePatch patch = new NinePatch(bitmap, bitmap.getNinePatchChunk(), null);
                float pathLeft = pointArr[i].x - textW / 2 - circleTextPadding;
                float pathTop = pointArr[i].y - fontTotalHeight / 2 - circleTextBottomMargin - circleTextPadding;
                float pathRight = pathLeft + textW + circleTextPadding * 2;
                float pathBottom = pathTop + fontTotalHeight + circleTextPadding * 2;

                Rect rect = new Rect((int) pathLeft, (int) pathTop, (int) pathRight, (int) pathBottom);
                patch.draw(canvas, rect);
                bitmap.recycle();
                //绘制中间温度tips文本
                mTextPaint.setTextSize(defaultTextSize);
                canvas.drawText(dataList.get(i).getTemperatureTip(), pathLeft + textW / 2 + circleTextPadding,
                        pathTop + fontTotalHeight / 2 + circleTextPadding / 2 * 3, mTextPaint);
            }
        }
    }


    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public void showWithAnim() {
        isShown = true;
        if (isAnimating) return;
        if (valueAnimator != null) {
            valueAnimator.start();
        }
    }

    public void setData(List<RainbowLineData> dataList) {
        if (dataList == null) return;
        if (dataList.isEmpty() || dataList.size() < ITEM_MAX) return;
        this.dataList.clear();
        this.dataList.addAll(dataList);
    }
}
