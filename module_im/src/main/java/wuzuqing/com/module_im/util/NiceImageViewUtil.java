package wuzuqing.com.module_im.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wuzuqing.component_base.util.ImageLoadUtils;
import com.wuzuqing.component_base.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import wuzuqing.com.module_im.R;

public class NiceImageViewUtil {

    private static final String TAG = "RectCache";

    private SparseArray<List<Rect>> sparseArray;
    private static NiceImageViewUtil rectCache;
    private int padding;
    private int width;
    private int height;
    private int bgColor;
    private NiceImageViewUtil() {

    }

    //单例 返回当前对象
    public static NiceImageViewUtil getRectCache() {
        if (rectCache == null) {
            synchronized (TAG) {
                if (rectCache == null) {
                    rectCache = new NiceImageViewUtil();
                }
            }
        }
        return rectCache;
    }

    // 初始化生成图片的宽高
    public void init(int width, int height, int padding) {
        if (sparseArray == null) {
            sparseArray = new SparseArray<>();
            this.padding = padding;
            this.width = width;
            this.height = height;
            this.bgColor = Color.parseColor("#f1f1f1");
        }
    }

    // 添加位置
    private int createRect(List<Rect> rectS, int oneWidth, int left, int top, int bottom) {
        int right;
        right = left + oneWidth;
        rectS.add(new Rect(left, top, right, bottom));
        left = right + padding;
        return left;
    }

    //根据头像的数量生成矩形
    private List<Rect> initRect(int width, int height, int maxSize) {
        //多少列
        int col = getColCount(maxSize);
        //多少行
        int row = getRowCount(maxSize);
        int temp = Math.max(row, col);
        int oneWidth = (width - padding * (temp + 1)) / temp;
        int oneHeight = oneWidth;
        int left = 0, top = 0, bottom;
        int firstRowCount = getFirstRowCount(maxSize);
        List<Rect> rects = new ArrayList<>();
        if (row == 1) {
            top = (height - oneHeight) / 2;
            bottom = top + oneHeight;
            left = padding;
            for (int i = 0; i < col; i++) {
                left = createRect(rects, oneWidth, left, top, bottom);
            }
        } else if (row > 1) {
            int index = 0;
            int realCol;
            for (int r = 0; r < row; r++) { //行
                if (r == 0) {
                    if (firstRowCount == 1) {
                        left = (width - oneWidth) / 2;
                    } else if (firstRowCount == 2) {
                        left = oneWidth / 2 + padding;
                    } else {
                        left = padding;
                    }
                    if (maxSize == 4) {
                        left = padding;
                    }
                    realCol = firstRowCount;
                    if (maxSize == 5 || maxSize == 6) {
                        top = padding + oneHeight / 2;
                    } else {
                        top = padding;
                    }
                } else {
                    realCol = col;
                    left = padding;
                }
                bottom = top + oneHeight;
                for (int c = 0; c < realCol; c++) {//列
                    left = createRect(rects, oneWidth, left, top, bottom);
                    index++;
                }
                if (index == maxSize) break;
                top = bottom + padding;
            }
        }
        return rects;
    }

    //获取第一行的数量
    private int getFirstRowCount(int maxSize) {
        switch (maxSize) {
            case 3:
            case 7:
                return 1;
            case 8:
            case 4:
            case 5:
                return 2;
            case 6:
            case 9:
                return 3;
        }
        return 0;
    }

    //获取需要绘制的行数
    private int getRowCount(int maxSize) {
        switch (maxSize) {
            case 2:
                return 1;
            case 3:
            case 4:
            case 6:
            case 5:
                return 2;
            case 7:
            case 8:
            case 9:
                return 3;
        }
        return 0;
    }

    //获取需要绘制的列数
    private int getColCount(int maxSize) {
        switch (maxSize) {
            case 2:
            case 3:
            case 4:
                return 2;
            case 6:
            case 7:
            case 8:
            case 9:
            case 5:
                return 3;
        }
        return 0;
    }

    //根据头像的数量获取对应的矩形
    public List<Rect> get(int count) {
        List<Rect> rects = sparseArray.get(count);
        if (rects == null) {
            rects = initRect(width, height, count);
            sparseArray.put(count, rects);
        }
        return rects;
    }

    // 根据drawable生成bitmap
    public Bitmap getBitmap(List<Drawable> src) {
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(result);
        canvas.drawColor(bgColor);
        List<Rect> rects = get(src.size());
        for (int i = 0; i < src.size(); i++) {
            Drawable drawable = src.get(i);
            drawable.setBounds(rects.get(i));
            drawable.draw(canvas);
        }
        return result;
    }


    public void setAvatars(ImageView iv, List<String> avatars) {
        long startTime = System.currentTimeMillis();
        iv.setImageResource(R.mipmap.ic_image_loading);
        if (ObjectUtils.isNotEmpty(avatars)) {
            if (avatars.size() == 1) {
                ImageLoadUtils.display(iv, avatars.get(0));
            } else {
                List<Drawable> drawables = new ArrayList<>();
                for (String avatar : avatars) {
                    Glide.with(iv).asDrawable().load(avatar).into(new SimpleTarget<Drawable>(width, height) {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            drawables.add(resource);
                            if (avatars.size() == drawables.size()) {
                                //加载结束,开始绘图
                                try {
                                    iv.setImageBitmap(getBitmap(drawables));
//                                    LogUtils.d("doNewDrawable: usedTime:" + (System.currentTimeMillis() - startTime));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        }
    }
}