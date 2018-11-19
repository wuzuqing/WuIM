package com.yanzhenjie.album.widget.photoview;

import android.content.Context;
import android.util.AttributeSet;

public class PhotoImageView extends AttacherImageView {
    public PhotoImageView(Context context) {
        super(context);
        initView();
    }


    public PhotoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PhotoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setAttacher(new PhotoViewAttacher(this));
    }

    public void setTouchListener(OnTouchListener touchListener) {
        getAttacher().setTouchListener(touchListener);
    }

}
