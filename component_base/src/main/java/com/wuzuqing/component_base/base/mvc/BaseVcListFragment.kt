package com.wuzuqing.component_base.base.mvc

import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

/**
 * @Created by TOME .
 * @时间 2018/6/4 17:19
 * @描述 ${列表基类,封装刷新和加载更多}
 */

abstract class BaseVcListFragment<D> : BaseVcFragment(), OnRefreshListener, OnLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildLongClickListener, BaseQuickAdapter.OnItemLongClickListener {


    protected var page = 0
    protected var pageSize = 10
    protected var isRefresh = true
    protected var rlRefreshLayout: SmartRefreshLayout? = null
    protected var mAdapter: BaseQuickAdapter<D, BaseViewHolder>? = null
    override fun initView() {
        if (rlRefreshLayout != null) {
            rlRefreshLayout!!.setOnRefreshListener(this)
            rlRefreshLayout!!.setOnLoadMoreListener(this)
        }
    }

    override fun onlyLoadOne() {
        loadListData(rlRefreshLayout, page, pageSize)
    }

    fun setRecyclerView(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager, adapter: BaseQuickAdapter<D, BaseViewHolder>?) {
        recyclerView.layoutManager = layoutManager
        BaseVcListFragment@ this.mAdapter = adapter
        recyclerView.adapter = adapter
    }

    fun setItemClick(item: Boolean, itemLong: Boolean,child: Boolean, childLong: Boolean) {
        if (item) {
            mAdapter!!.onItemClickListener = this
        }
        if (itemLong) {
            mAdapter!!.onItemLongClickListener = this
        }
        if (child) {
            mAdapter!!.onItemChildClickListener = this
        }
        if (childLong) {
            mAdapter!!.onItemChildLongClickListener = this
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

    }
    override fun onItemChildLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int): Boolean {
        return false
    }

    override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int): Boolean {
        return false
    }


    /**
     * 刷新
     * @param refreshLayout
     */
    override fun onRefresh(refreshLayout: RefreshLayout) {
        this.page = 0
        isRefresh = true
        loadListData(rlRefreshLayout, page, pageSize)
    }

    /**
     * 加载更多
     * @param refreshLayout
     */
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page++
        isRefresh = false
        loadListData(rlRefreshLayout, page, pageSize)
    }

    abstract fun loadListData(rlRefreshLayout: SmartRefreshLayout?, page: Int, pageSize: Int)


}
