package com.wuzuqing.component_base.listener;

import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wuzuqing.component_base.constants.BaseApplication;


/**
 * 适合全部是图片且固定位置的RecyclerView监听
 */
public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                //当屏幕停止滚动，加载图片
                try {
                    Glide.with(BaseApplication.getAppContext()).resumeRequests();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
                //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
            case RecyclerView.SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to a final position while not under outside control.
                //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                try {
                    Glide.with(BaseApplication.getAppContext()).pauseRequests();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
