package com.kuxinwei.timetask.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.kuxinwei.timetask.R;

/**
 * Created by kuxinwei on 2014/8/13.
 */
public class ClockView extends View {


    private static final int DEFAULT_BORDER_COLOR = Color.parseColor("#AA66CC");
    private static final int DEFAULT_TIMER_COLOR = Color.parseColor("#9933CC");
    private float mBorderPaintWidth;
    private float mTimerPaintWidth;
    private int mBorderColor;
    private int mTimerColor;
    private Paint mBorderPaint;
    private Paint mTimerPaint;
    private int hour;
    private int minute;
    private float pivotX;
    private float pivotY;

    private PointF mHourPoint = new PointF();
    private PointF mMinutePoint = new PointF();
    private float angleOfHour;
    private float angleOfMinute;

    private RectF mRect = new RectF();

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ClockView);
        try {
            hour = a.getInt(R.styleable.ClockView_hour, 0);
            minute = a.getInt(R.styleable.ClockView_minute, 0);
            mBorderColor = a.getColor(R.styleable.ClockView_borderColor, DEFAULT_BORDER_COLOR);
            mTimerColor = a.getColor(R.styleable.ClockView_timerColor, DEFAULT_TIMER_COLOR);
            mBorderPaintWidth = a.getDimension(R.styleable.ClockView_borderWidth, 6);
            mTimerPaintWidth = a.getDimension(R.styleable.ClockView_timerWidth, 6);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        angleOfMinute = minute * 6;
        if (minute >= 60)
            minute -= 60;
        angleOfHour = (hour * 30) + (minute / 2);
        initPaint();
    }

    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        angleOfMinute = minute * 6;
        while (minute >= 60)
            minute -= 60;
        angleOfHour = (hour * 30) + (minute / 2);
        invalidate();
    }

    private void initPaint() {
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderPaintWidth);

        mTimerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimerPaint.setColor(mTimerColor);
        mTimerPaint.setStrokeWidth(mTimerPaintWidth);
        mTimerPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float offset = mBorderPaintWidth / 2;
        mRect.set(0 + offset, 0 + offset, w - offset, h - offset);
        pivotX = w / 2 + offset / 2;
        pivotY = h / 2 + offset / 2;
        float radius = w / 2;
        calPointPosition(mHourPoint, pivotX, pivotY, angleOfHour, radius);
        calPointPosition(mMinutePoint, pivotX, pivotY, angleOfMinute, radius);
        invalidate();
    }

    private void calPointPosition(PointF pointF, float pivotX, float pivotY, float angle, float radius) {
        float x = pivotX + (radius - mTimerPaintWidth) * (float) Math.sin(Math.toRadians(angle));
        float y = pivotY - (radius - mTimerPaintWidth) * (float) Math.cos(Math.toRadians(angle));
        pointF.set(x, y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(pivotX, pivotY, mHourPoint.x, mHourPoint.y, mTimerPaint);
        canvas.drawLine(pivotX, pivotY, mMinutePoint.x, mMinutePoint.y, mTimerPaint);
//        canvas.drawArc(mRect, angleOfHour-5, 5, true, mTimerPaint);
//        canvas.drawArc(mRect, angleOfMinute-5, 5, true, mTimerPaint);
//        canvas.drawArc(mRect, -90, 359, true, mTimerPaint);
        canvas.drawOval(mRect, mBorderPaint);
    }
}
