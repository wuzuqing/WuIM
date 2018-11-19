package wuzuqing.com.module_im.listener;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.wuzuqing.component_base.util.LogUtils;

import wuzuqing.com.module_im.adapter.ChatAdapter;

public class MediaTextScrollListener extends RecyclerView.OnScrollListener implements GestureDetector.OnGestureListener, View.OnTouchListener {

    private ChatAdapter adapter;
    private GestureDetector gestureDetector;
    private boolean scrolled;//是否滚动了,优化用
    private RecyclerView recyclerView;

    public MediaTextScrollListener(RecyclerView recyclerView, ChatAdapter adapter) {
        this.adapter = adapter;
        this.recyclerView = recyclerView;
        gestureDetector = new GestureDetector(recyclerView.getContext(), this);
        recyclerView.setOnTouchListener(this);
        recyclerView.addOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                if (adapter.isScrolling() && scrolled) {
                    //对于滚动不加载图片的尝试
                    adapter.setScrolling(false);
                    adapter.notifyDataSetChanged();
                    LogUtils.d("onScrollStateChanged");
                }
                scrolled = false;
                break;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy != 0 && !scrolled) {
            scrolled = true;
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (onClickListener != null) {
            onClickListener.onClick(recyclerView);
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityY) > 4000) {
            adapter.setScrolling(true);
        }

        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return false;
    }

    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
