package com.example.medievalapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class LinedEditText extends AppCompatEditText {
    private final Rect mRect;
    private final Paint mPaint;

    private static final int LINE_COLOR = Color.parseColor("#D7CCC8"); 

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(LINE_COLOR);
        mPaint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int lineHeight = getLineHeight();
        int count = height / lineHeight;
        
        if (getLineCount() > count) {
            count = getLineCount();
        }

        int baseline = getPaddingTop() + lineHeight;

        for (int i = 0; i < count; i++) {
            canvas.drawLine(getLeft(), baseline + 2, getRight(), baseline + 2, mPaint);
            baseline += lineHeight;
        }

        super.onDraw(canvas);
    }
}
