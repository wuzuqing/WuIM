package wuzuqing.com.module_im.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.wuzuqing.component_base.util.TimeUtils;

import wuzuqing.com.module_im.R;

public class ImRoundRectImageView extends AppCompatImageView {
    private Paint paint;
    private Paint textPaint;
    private int style;
    private int viewWidth, viewHeight;
    private int radius;
    private float radio = 5f;
    private int radiusColor;

    public ImRoundRectImageView(Context context) {
        this(context, null);
    }

    public ImRoundRectImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImRoundRectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ImRoundRectImageView);
        style = array.getInteger(R.styleable.ImRoundRectImageView_im_riv_aspect_style, 0);
        radius = array.getInteger(R.styleable.ImRoundRectImageView_im_riv_radius, (int) TypedValue.applyDimension(1, 4, metrics));
        radio = array.getFloat(R.styleable.ImRoundRectImageView_im_riv_radio, radio);
        array.recycle();


        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        rectF = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(radius / 2);
        radiusColor = getResources().getColor(R.color.windowBackgroundColor);
        paint.setColor(radiusColor);
        setAspectStyle(style);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(TypedValue.applyDimension(1, 8, getResources().getDisplayMetrics()));
    }

    int screenWidth, screenHeight;

    private void setAspectStyle(int style) {
        switch (style) {
            case 0:  //纵向
                viewWidth = (int) (screenWidth / radio);
                viewHeight = (int) (screenHeight / radio);
                break;
            case 1:
                viewWidth = screenWidth / 3;
                viewHeight = viewWidth * 2 / 3;
                break;
        }
    }


    public void setWHStyle(Drawable drawable) {
        int style = drawable.getIntrinsicWidth() > drawable.getIntrinsicHeight() ? 1 : 0;
        setAspectStyle(style);
        setImageDrawable(drawable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private RectF rectF;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int half = radius / 4;
        rectF.set(half, half, w - half, h - half);
    }

    /**
     * 绘制圆角矩形图片
     *
     * @author caizhiming
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (radius > 0) {
            int half = radius / 4;
            rectF.set(half, half, viewWidth - half, viewHeight - half);
            canvas.drawRect(rectF, paint);
            canvas.drawRoundRect(rectF, radius, radius, paint);
        }
        if (!TextUtils.isEmpty(duration)) {
            left = (viewWidth - playIcon.getWidth()) / 2;
            top = (viewHeight - playIcon.getHeight()) / 2;
            canvas.drawBitmap(playIcon, left, top, null);
            canvas.drawText(duration, viewWidth - textPaint.measureText(duration) - 20, viewHeight - 40, textPaint);
        }
    }

    private String duration ;
    private Bitmap playIcon;
    private int left, top;

    public void setDuration(long second) {

        if (second <= 0) {
            this.duration = null;
        } else {
            this.duration = TimeUtils.getDuration(second);
            if (playIcon == null) {
                playIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.img_video_play_session);
            }
        }
        invalidate();
    }

    /**
     * 获取圆角矩形图片方法
     *
     * @param bitmap
     * @param roundPx,一般设置成14
     * @return Bitmap
     * @author caizhiming
     */
    private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
