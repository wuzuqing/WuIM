package com.wuzuqing.component_base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;

import com.wuzuqing.component_base.R;

public class CustomEditText extends AppCompatEditText {

    private Drawable leftIcon;
    private Drawable deleteIcon;
    private Drawable showPwdIcon;
    private Drawable hidePwdIcon;
    private int showStyle;
    private boolean isPwdStyle;

    private boolean isShowPwd = true;
    private boolean isShowDelete;
    private int iconSize;
    private int defaultPaddingLeft;

    public CustomEditText(Context context) {
        super(context);
        initEvent();
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initEvent();
    }

    public CustomEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initEvent();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        leftIcon = getResourceId(array, R.styleable.CustomEditText_cet_left_icon, 0);
        deleteIcon = getResourceId(array, R.styleable.CustomEditText_cet_delete_icon, 0);
        showPwdIcon = getResourceId(array, R.styleable.CustomEditText_cet_show_pwd_icon, 0);
        hidePwdIcon = getResourceId(array, R.styleable.CustomEditText_cet_hide_pwd_icon, 0);
        showStyle = array.getInteger(R.styleable.CustomEditText_cet_style, 0);
        defaultPaddingLeft = (int) TypedValue.applyDimension(1, 12f, getResources().getDisplayMetrics());
        iconSize = (int) TypedValue.applyDimension(1, 20, getResources().getDisplayMetrics());
        int paddingRight = 0;
        int paddingLeft = 0;
        if (deleteIcon != null) {
            paddingLeft = defaultPaddingLeft * 2 + iconSize;
            paddingRight = defaultPaddingLeft * 2 + iconSize;
        }
        if (isPwdStyle) {
            paddingRight += iconSize;
        }
        setBackground(null);
        setGravity(Gravity.CENTER_VERTICAL);
        switch (showStyle) {
            case 1:
                isPwdStyle = true;
                setTransformationMethod(PasswordTransformationMethod.getInstance());
                break;
            case 2:
                setInputType(InputType.TYPE_CLASS_PHONE);
                InputFilter[] filters = {new InputFilter.LengthFilter(30)};
                setFilters(filters);
                break;
        }
        setPadding(paddingLeft, getPaddingTop(), paddingRight, getPaddingBottom());
        array.recycle();
    }

    private Drawable getResourceId(TypedArray array, int index, int defaultValue) {
        if (array.hasValue(index)) {
            return array.getDrawable(index);
        }
        return null;
    }

    private void initEvent() {
        addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0 && isShowDelete) {
                isShowDelete = false;
            } else if (s.length() > 0 && !isShowDelete) {
                isShowDelete = true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isRange(event.getX());
                return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    private boolean isRange(float downX) {

        if (isPwdStyle) {
            Rect rect = showPwdIcon.getBounds();
            if (downX > rect.left && downX < rect.right) {
                //切换
                if (isShowPwd) {
                    isShowPwd = false;
                    //显示密码
                    setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                    setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    //隐藏密码
                    isShowPwd = true;
//                    setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                invalidate();
                return true;
            }
        }
        if (deleteIcon != null) {
            Rect rect = deleteIcon.getBounds();
            if (downX > rect.left && downX < rect.right) {
                //切换
                setText("");
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int top = (h - iconSize) / 2;
        int bottom = top + iconSize;
        int left = defaultPaddingLeft;
        int right = left + iconSize;
        if (leftIcon != null) {
            leftIcon.setBounds(left, top, right, bottom);
        }
        right = w - defaultPaddingLeft;
        left = right - iconSize;
        if (isPwdStyle) {
            showPwdIcon.setBounds(left, top, right, bottom);
            hidePwdIcon.setBounds(left, top, right, bottom);
            right = left - defaultPaddingLeft;
            left = right - iconSize;
            deleteIcon.setBounds(left, top, right, bottom);
        } else {
            deleteIcon.setBounds(left, top, right, bottom);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, 0);
        if (leftIcon != null) {
            leftIcon.draw(canvas);
        }
        if (isShowDelete && deleteIcon != null) {
            deleteIcon.draw(canvas);
        }
        if (showPwdIcon != null) {
            if (isShowPwd) {
                showPwdIcon.draw(canvas);
            } else {
                hidePwdIcon.draw(canvas);
            }
        }
    }

}
