package com.wuzuqing.component_base.base.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public abstract class CommonRecyclerAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    public CommonRecyclerAdapter(int layoutResId) {
        super(layoutResId);
    }

    public CommonRecyclerAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public CommonRecyclerAdapter(@Nullable List<T> data) {
        super(data);
    }


}
