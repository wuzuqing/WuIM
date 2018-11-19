//package com.wuzuqing.component_base.widget;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.util.AttributeSet;
//import android.util.TypedValue;
//import android.view.View;
//
//import com.wuzuqing.component_base.R;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class MultiTextView extends View {
//    //控件宽度
//    private int viewWidth = 0;
//    //控件高度
//    private int viewHeight = 0;
//    //字体大小
//    private float textSize = 40;
//    //文字显示区的宽度
//    private int textWidth;
//    //单行文字的高度
//    private int singleLineHeight;
//
//    //内容填充画笔
//    private Paint contentPaint;
//    private String anchorTip = "【主播】";
//    private int anchorColor = Color.parseColor("#ff6652");
//    private int nameColor = Color.parseColor("#9e9e9e");
//    private int systemColor = Color.parseColor("#ff6652");
//    //默认当前的颜色
//    private int contentNormalColor = Color.parseColor("#333333");
//
//    private List<Bitmap> iconBitmaps;
//
//
//    //内容
//    private String textContent = "测试的文字信息";
//    //行间距
//    private float lineSpace;
//    //文字测量工具
//    private Paint.FontMetricsInt textFm;
//
//
//    private int mPaddingLeft, mPaddingRight, mPaddingTop, mPaddingBottom;
//
//    private boolean isAnchor = true;
//    private boolean isSystemMsg = false;
//
//    private String textName = "我很帅";
//    private int myMargin = 10;
//
//    public MultiTextView(Context context) {
//        this(context, null);
//    }
//
//    public MultiTextView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public MultiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initAttr(context, attrs);
//        init();
//    }
//
//
//    /**
//     * 初始化属性
//     *
//     * @param context
//     * @param attrs
//     */
//    private void initAttr(Context context, AttributeSet attrs) {
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiTextView);
//        mPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MultiTextView_mtv_paddingLeft, 0);
//        mPaddingRight = typedArray.getDimensionPixelSize(R.styleable.MultiTextView_mtv_paddingRight, 0);
//        mPaddingTop = typedArray.getDimensionPixelSize(R.styleable.MultiTextView_mtv_paddingTop, 0);
//        mPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MultiTextView_mtv_paddingBottom, 0);
//
//        textContent = typedArray.getString(R.styleable.MultiTextView_mtv_text);
//        contentNormalColor = typedArray.getColor(R.styleable.MultiTextView_mtv_textColor, contentNormalColor);
//        systemColor = typedArray.getColor(R.styleable.MultiTextView_mtv_textColor, systemColor);
//        anchorColor = typedArray.getColor(R.styleable.MultiTextView_mtv_textColor, anchorColor);
//        nameColor = typedArray.getColor(R.styleable.MultiTextView_mtv_textColor, nameColor);
//
//        textSize = typedArray.getDimension(R.styleable.MultiTextView_mtv_textSize, TypedValue
//                .applyDimension(2, 12, getResources().getDisplayMetrics()));
//        lineSpace = typedArray.getInteger(R.styleable.MultiTextView_mtv_lineSpacing, 0);
//        myMargin = (int) TypedValue.applyDimension(1, 4, getResources().getDisplayMetrics());
//        typedArray.recycle();
//    }
//
//    /**
//     * 初始化
//     */
//    private void init() {
//        contentPaint = new Paint();
//        contentPaint.setTextSize(textSize);
//        contentPaint.setAntiAlias(true);
//        contentPaint.setStrokeWidth(2);
//        contentPaint.setColor(contentNormalColor);
//        contentPaint.setTextAlign(Paint.Align.LEFT);
//        textFm = contentPaint.getFontMetricsInt();
//        singleLineHeight = Math.abs(textFm.top - textFm.bottom);
//    }
//
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        viewWidth = getMeasuredWidth();
//        textWidth = viewWidth - mPaddingLeft - mPaddingRight;
//        viewHeight = (int) getViewHeight();
//        setMeasuredDimension(viewWidth, viewHeight);
//    }
//
//    //计算view的高度
//    private float getViewHeight() {
//        int iconWidth = 0;
//        if (iconBitmaps != null) { //如果有图标则计算图标的宽度
//            for (Bitmap bitmap : iconBitmaps) {
//                iconWidth += (bitmap.getWidth() + myMargin);
//            }
//        }
//        String allText = String.format("%s%s%s", isAnchor ? anchorTip : "", textName,
//                textContent);//组合所以文本的长度
//        char[] textChars = allText.toCharArray();
//        float ww = 0.0f + iconWidth + (isAnchor ? myMargin * 2 : myMargin);
//        int count = 0;
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < textChars.length; i++) {
//            float v = contentPaint.measureText(textChars[i] + "");
//            if (ww + v <= textWidth) {
//                sb.append(textChars[i]);
//                ww += v;
//            } else {     //如果ww长度大于一行的宽度则添加一行
//                count++;
//                sb = new StringBuilder();
//                ww = 0.0f;
//                ww += v;
//                sb.append(textChars[i]);
//            }
//        }
//        if (sb.toString().length() != 0) {
//            count++;
//        }
//        return count * singleLineHeight + lineSpace * (count - 1) + mPaddingBottom + mPaddingTop;
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        drawText(canvas);
//    }
//
//    /**
//     * 循环遍历画文字
//     *
//     * @param canvas
//     */
//    private void drawText(Canvas canvas) {
//
//        char[] textChars = textContent.toCharArray();
//        float ww = 0.0f;
//        float startL = 0.0f;
//        float startT = 0.0f;
//        startL += mPaddingLeft;
//        startT += mPaddingTop + singleLineHeight / 2 + (textFm.bottom - textFm.top) / 2 - textFm.bottom;
//        StringBuilder sb = new StringBuilder();
//
//        if (isEmpty(iconBitmaps)) {   //绘制图标
//            for (Bitmap iconBitmap : iconBitmaps) {
//                canvas.drawBitmap(iconBitmap, startL + ww, singleLineHeight / 2 + mPaddingTop -
//                        iconBitmap.getHeight() / 2, null);
//                ww += iconBitmap.getWidth() + myMargin;
//            }
//        }
//
//        if (isAnchor) {         //绘制主播文字
//            contentPaint.setColor(anchorColor);
//            canvas.drawText(anchorTip, startL + ww, startT, contentPaint);
//            ww += contentPaint.measureText(anchorTip) + myMargin;
//        }
//
//        //绘制名称
//        contentPaint.setColor( nameColor);
//        canvas.drawText(textName, startL + ww, startT, contentPaint);
//        ww += contentPaint.measureText(textName) ;
//        float oneStart = ww;
//        contentPaint.setColor(isSystemMsg ? systemColor : contentNormalColor);
//        //绘制内容
//        for (int i = 0; i < textChars.length; i++) {
//            float v = contentPaint.measureText(textChars[i] + "");
//            if (ww + v <= textWidth) {
//                sb.append(textChars[i]);
//                ww += v;
//            } else {
//                canvas.drawText(sb.toString(), oneStart + startL, startT, contentPaint);
//                startT += singleLineHeight + lineSpace;
//                sb = new StringBuilder();
//                ww = 0.0f;
//                oneStart = 0;
//                ww += v;
//                sb.append(textChars[i]);
//            }
//        }
//
//        if (sb.toString().length() > 0) {
//            canvas.drawText(sb.toString(), oneStart + startL, startT, contentPaint);
//        }
//    }
//
//
//    private boolean isEmpty(List<Bitmap> list) {
//        return list != null && !list.isEmpty();
//    }
//
//
//    public void setText(String ss) {
//        recycledIcon();
//        this.textContent = ss;
//        isSystemMsg = false;
//        isAnchor = false;
//        requestLayout();
//    }
//
//    public void setSystemMsg(String name, String content) {
//        recycledIcon();
//        this.  textName =name;
//        this.textContent = content;
//        isSystemMsg = true;
//        isAnchor = false;
//        requestLayout();
//    }
//
//    public void setParams(String name, String content, boolean isAnchor, Object... icons) {
//        isSystemMsg = false;
//        this.isAnchor = isAnchor;
//        this.textContent = content;
//        this.textName = name;
//        setIcons(Arrays.asList(icons));
//    }
//    public void setParams(String name, String content, boolean isAnchor, boolean isSystemMsg , Object...
//            icons) {
//        this.isSystemMsg = isSystemMsg;
//        if (isSystemMsg){
//            recycledIcon();
//            this.isAnchor = false;
//            textName = "系统消息:";
//            this.textContent = content;
//            requestLayout();
//        }else{
//            this.isAnchor = isAnchor;
//            this.textContent = content;
//            this.textName = name;
//            setIcons(Arrays.asList(icons));
//        }
//    }
//
//
//    private Bitmap createBitmap(Object obj) {
//        Bitmap bitmap = null;
//        if (obj instanceof Integer) {
//            bitmap = BitmapFactory.decodeResource(getResources(), (int) obj);
//        } else if (obj instanceof Bitmap) {
//            bitmap = (Bitmap) obj;
//        }
//
//        int dstWidth = 0;
//        int disHeight = singleLineHeight - 8;
//        if (bitmap != null && bitmap.getHeight() > 0) {
//            dstWidth = disHeight  * bitmap.getWidth() / bitmap.getHeight();
//        }
//        bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, disHeight, false);
//        return bitmap;
//    }
//
//
//    public void setIcons(List<Object> icons) {
//        //释放资源
//        recycledIcon();
//        iconBitmaps = new ArrayList<>();
//        for (Object icon : icons) {
//            iconBitmaps.add(createBitmap(icon));
//        }
//        requestLayout();
//    }
//
//    private void recycledIcon() {
//        if (iconBitmaps != null && !iconBitmaps.isEmpty()) {
//            for (Bitmap bitmap : iconBitmaps) {
//                if (!bitmap.isRecycled()) {
//                    bitmap.recycle();
//                    bitmap = null;
//                }
//            }
//            iconBitmaps = null;
//        }
//    }
//}