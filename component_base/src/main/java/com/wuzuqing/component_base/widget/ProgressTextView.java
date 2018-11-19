package com.wuzuqing.component_base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.wuzuqing.component_base.R;


public class ProgressTextView extends View {
    private int progress = 10;
    private int max = 100;
    private int finishColor;
    private int unFinishColor;
    private int textColor;
    private float textSize;
    private Paint textPaint;

    public ProgressTextView(Context context) {
        this(context, null);
    }

    public ProgressTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressTextView);
        textColor = finishColor = Color.WHITE;
        unFinishColor = Color.parseColor("#70FFFFFF");
        textColor = array.getColor(R.styleable.ProgressTextView_ptv_text_color, textColor);
        finishColor = array.getColor(R.styleable.ProgressTextView_ptv_finish_color, finishColor);
        unFinishColor = array.getColor(R.styleable.ProgressTextView_ptv_unFinish_color, unFinishColor);
        textSize = TypedValue.applyDimension(1, array.getFloat(R.styleable.ProgressTextView_ptv_text_size, 12f), getResources().getDisplayMetrics());
        array.recycle();

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        maxText = formatText(max);
    }

    private float cy, cx;
    private int viewWidth;
    private int halfHeight;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        viewWidth = w;
        halfHeight = h / 2;
        Paint.FontMetrics textFm = textPaint.getFontMetrics();
        cy = h / 2 - textFm.descent + (textFm.bottom - textFm.top) / 2;
        startX = (int) (getPaddingLeft() + margin + textPaint.measureText(maxText));
        endX = (int) (viewWidth - getPaddingRight() - textPaint.measureText(maxText) - margin);
        progressWidth = endX - startX;
    }

    private String maxText = "00:00";

    private RectF rectF = new RectF();
    private float progressWidth;
    private int startX, endX;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        textPaint.setColor(textColor);
        cx = getPaddingLeft();
        canvas.drawText(formatText(progress), cx, cy, textPaint);
        cx = endX + margin;
        canvas.drawText(maxText, cx, cy, textPaint);

        rectF.top = halfHeight - 2;
        rectF.bottom = halfHeight + 2;
        rectF.left = startX;
        rectF.right = endX;
        textPaint.setColor(unFinishColor);
        canvas.drawRoundRect(rectF, 2, 2, textPaint);
        progressWidth = rectF.width();

        textPaint.setColor(finishColor);
        rectF.right = rectF.left + progressWidth * progress / max;
        canvas.drawRoundRect(rectF, 2, 2, textPaint);

        canvas.drawCircle(rectF.right, halfHeight, isTouch ? radius * 2 : radius, textPaint);
    }

    private int radius = 8;
    private int margin = 20;

    private String formatText(int progress) {
        int min = progress / 60;
        int sec = progress % 60;
        return String.format("%02d:%02d", min, sec);
    }

    private boolean isTouch;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                updateProgress((int) event.getX(), true);
                break;
            case MotionEvent.ACTION_MOVE:
                updateProgress((int) event.getX(), true);
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                updateProgress((int) event.getX(), false);
                if (onProgressChangeListener != null) {
                    onProgressChangeListener.end(progress);
                }
                break;
        }
        return true;
    }

    private static final String TAG = "ProgressTextView";

    private void updateProgress(int downX, boolean change) {
        if (downX >= startX && downX <= endX) {
            progress = (int) (Math.ceil((downX - startX)*max / progressWidth));
            if (onProgressChangeListener != null && change) {
                onProgressChangeListener.change(progress);
            }
        }else if (downX<startX){
            progress = 0;
        }else if (downX>endX){
            progress = max;
        }
        if (change){
            invalidate();
        }
    }

    public void setMax(int max) {
        if (this.max != max) {
            this.max = max;
            maxText = formatText(max);
            invalidate();
        }
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void setProgressAndMax(int progress, int max) {
        setMax(max);
        this.progress = progress;
        invalidate();
    }

    public void setProgressAndMaxLong(int progress, int max) {
        setMax(max / 1000);
        this.progress = progress / 1000;
        invalidate();
    }

    private onProgressChangeListener onProgressChangeListener;

    public void setOnProgressChangeListener(ProgressTextView.onProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    public interface onProgressChangeListener {
        void change(int newProgress);

        void end(int newProgress);
    }
}
