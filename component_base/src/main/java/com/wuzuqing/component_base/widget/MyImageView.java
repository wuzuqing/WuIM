//package com.example.tome.component_base.widget;
//
//
//import android.content.Context;
//import android.graphics.ColorMatrixColorFilter;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//
//public class MyImageView extends ImageView {
//    public MyImageView(Context context) {
//        super(context);
//        this.setOnTouchListener(VIEW_TOUCH_DARK);
//    }
//
//    public MyImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.setOnTouchListener(VIEW_TOUCH_DARK);
//    }
//
//    public OnTouchListener VIEW_TOUCH_DARK = new OnTouchListener() {
//        //变暗(三个-50，值越大则效果越深)
//        public final float[] BT_SELECTED_DARK = new float[]{1, 0, 0, 0, -50, 0, 1,
//                0, 0, -50, 0, 0, 1, 0, -50, 0, 0, 0, 1, 0};
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                ImageView iv = (ImageView) v;
//                iv.setColorFilter(new ColorMatrixColorFilter(BT_SELECTED_DARK));
//            } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                ImageView iv = (ImageView) v;
//                iv.clearColorFilter();
//                mPerformClick();
//            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
//                ImageView iv = (ImageView) v;
//                iv.clearColorFilter();
//            }
//            return true;  //如为false，执行ACTION_DOWN后不再往下执行
//        }
//    };
//
//    private void mPerformClick() {
//        MyImageView.this.performClick();
//    }
//
//}