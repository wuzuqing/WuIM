package com.wuzuqing.component_base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.wuzuqing.component_base.R;

public class BadgeView extends View {

    private Paint paint;
    private int textColor;
    private float textSize;
    private int padding;
    private String text = "99+";
    private int smallSize;

    //    private int contentSize;
    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BadgeView);

        text = array.getString(R.styleable.BadgeView_bdv_num_text);
        padding = (int) array.getDimension(R.styleable.BadgeView_bdv_padding, TypedValue.applyDimension(1, 3f, getResources().getDisplayMetrics()));
        textSize = TypedValue.applyDimension(2, array.getFloat(R.styleable.BadgeView_bdv_num_text_size, 8f), getResources().getDisplayMetrics());
        smallSize = (int) array.getDimension(R.styleable.BadgeView_bdv_small_size,
                TypedValue.applyDimension(1, 8f, getResources().getDisplayMetrics()));

        array.recycle();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textColor = Color.WHITE;

    }

    private int textWidth;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (TextUtils.isEmpty(text)) {
            setMeasuredDimension(smallSize, smallSize);
        } else {
            paint.setTextSize(textSize);
            textWidth = (int) paint.measureText("99+");
            setMeasuredDimension((textWidth + padding), (textWidth + padding));
        }
    }

    private int radius;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = w / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.RED);
        canvas.drawCircle(radius, radius, radius, paint);
        if (!TextUtils.isEmpty(text)) {
            paint.setColor(textColor);
            paint.getFontMetrics();
            float cx = radius - paint.measureText(text) / 2;
            canvas.drawText(text, cx, radius - getBaseLine(paint), paint);
        }
    }

    public float getBaseLine(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
//        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
//        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        return (fontMetrics.top + fontMetrics.bottom) / 2;//基线中间点的y轴计算公式
    }

    private int number;

    public void setNumber(int number) {
        this.number = number;
        if (number == 0) {
            setVisibility(GONE);
        } else {
            text = number > 99 ? "99+" : String.valueOf(number);
            setVisibility(VISIBLE);
            requestLayout();
        }
    }
}
