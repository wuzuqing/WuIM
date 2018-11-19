package wuzuqing.com.module_im.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wuzuqing.component_base.util.ImageLoadUtils;
import com.wuzuqing.component_base.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import wuzuqing.com.module_im.R;

public class NiceImageView extends ImageView {

    private Bitmap bitmap;

    public NiceImageView(Context context) {
        super(context);
    }

    public NiceImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NiceImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }


    public void setAvatars(List<String> avatars) {
        setImageResource(R.mipmap.ic_image_loading);
        if (ObjectUtils.isEmpty(avatars)) {
            if (avatars.size() == 1) {
                ImageLoadUtils.display(this, avatars.get(0));
            } else
                new Thread(() -> {
                    DrawTarget drawTarget = new DrawTarget(NiceImageView.this, 100, 100, avatars.size());
                    for (String avatar : avatars) {
                        Glide.with(this).asDrawable().load(avatar).into(drawTarget);
                    }
                }).start();
        }
    }

    class DrawTarget extends SimpleTarget<Drawable> {
        private int maxSize;
        List<Drawable> drawables;
        private NiceImageView niceImageView;

        public DrawTarget(NiceImageView niceImageView, int width, int height, int maxSize) {
            super(width, height);
            drawables = new ArrayList<>();
            this.maxSize = maxSize;
            this.niceImageView = niceImageView;
        }

        @Override
        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
            drawables.add(resource);
            if (maxSize == drawables.size() + 1) {
                //加载结束,开始绘图
                doNewDrawable();
            }
        }

        private void doNewDrawable() {
            int width = niceImageView.getWidth();
            int height = niceImageView.getHeight();
            Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(result);
            canvas.drawColor(Color.GRAY);
            //多少列
            int col = maxSize > 3 ? 3 : maxSize;
            //多少行
            int row = maxSize < 3 ? 1 : maxSize < 5 ? 2 : 3;
            int oneWidth = (width - padding * (col + 1)) / col;
            int oneHeight = (width - padding * (row + 1)) / row;

            int left = 0, right, top = 0, bottom;
            int firstRowCount = 1;
            switch (maxSize) {
                case 3:
                case 5:
                case 7:
                    firstRowCount = 1;
                    break;
                case 4:
                case 6:
                case 8:
                    firstRowCount = 2;
                    break;
                case 9:
                    firstRowCount = 3;
                    break;
            }
            if (row == 1) {
                top = (height - oneHeight) / 2;
                bottom = top + oneHeight;
                left = padding;
                for (int i = 0; i < col; i++) {
                    left = draw(canvas, oneWidth, left, top, bottom, i);
                }
            } else if (row > 1) {
                int index = 0;
                for (int r = 0; r < row; r++) { //行
                    if (r == 0) {
                        if (firstRowCount == 1) {
                            left = (width - oneWidth) / 2;
                        } else if (firstRowCount == 2) {
                            left = oneWidth / 2 + padding;
                        } else if (firstRowCount == 3) {
                            left = padding;
                        }
                        top = padding;
                    } else {
                        left = padding;
                    }
                    bottom = top + oneHeight;
                    for (int c = 0; c < col; c++) {//列
                        left = draw(canvas, oneWidth, left, top, bottom, index);
                        index++;
                    }
                    top = bottom + padding;
                }
            }

            niceImageView.post(() -> niceImageView.setImageBitmap(result));
        }

        private int draw(Canvas canvas, int oneWidth, int left, int top, int bottom, int index) {
            int right;
            Drawable drawable = drawables.get(index);
            right = left + oneWidth;
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(canvas);
            left = right + padding;
            return left;
        }
    }

    public int padding = 2;
}
