package com.wuzuqing.component_base.base.mvc

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wuzuqing.component_base.base.mvp.BaseVpActivity
import com.wuzuqing.component_base.base.mvp.inter.IView
import com.wuzuqing.component_base.rxbus.RxManager
import com.wuzuqing.component_base.util.ToastUtils
import io.reactivex.functions.Consumer

/**
 * @Created by TOME .
 * @时间 2018/5/30 17:01
 * @描述 ${MVC模式的Base fragment}
 */

abstract class BaseVcFragment : Fragment(), IView {

    protected var mContext: Context? = null

    var mView: IView = this
    var rootView: View ? =null
    protected var regEvent: Boolean = true
    var mRxManager: RxManager? = null
    /**
     * rootView是否初始化标志，防止回调函数在rootView为空的时候触发
     */
    protected var hasCreateView: Boolean = false

    /**
     * 当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
     */
    private var isFragmentVisible: Boolean = false

    private var isFirstLoad = true
    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract fun getLayout(): Int

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVariable()
    }

    private fun initVariable() {
        hasCreateView = false
        isFragmentVisible = false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView==null){
            rootView = inflater.inflate(getLayout(), container, false)
        }
        if (!hasCreateView && userVisibleHint) {
            onFragmentVisibleChange(true)
            isFragmentVisible = true
        }
        if (regEvent) {
            mRxManager = RxManager()
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTitle()
        initView()
    }


   override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (rootView == null) {
            return
        }
        hasCreateView = true
        if (isVisibleToUser) {
            onFragmentVisibleChange(true)
            isFragmentVisible = true
            return
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(false)
            isFragmentVisible = false
        }
    }

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 [.setUserVisibleHint]一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作，因为配合fragment的view复用机制，你不用担心在对控件操作中会报 null 异常
     *
     * @param isVisible true  不可见 -> 可见
     * false 可见  -> 不可见
     */
    open fun onFragmentVisibleChange(isVisible: Boolean) {
        if (isVisible && isFirstLoad) {
            isFirstLoad = false
            onlyLoadOne()
        }
    }

    open fun onlyLoadOne() {

    }


    override fun showHUD(msg: String) {
        if (activity != null) {
            val baseActivity = activity as BaseVpActivity<*, *>?
            baseActivity!!.showHUD(msg)
        }
    }

    override fun dismissHUD() {
        if (activity != null) {
            val baseActivity = activity as BaseVpActivity<*, *>?
            baseActivity!!.dismissHUD()
        }
    }
    protected var isPause = false
    override fun onPause() {
        super.onPause()
        isPause = true
    }
    override fun onResume() {
        super.onResume()
        isPause = false
//        LogUtils.i("当前运行的fragment:" + javaClass.name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (regEvent && mRxManager != null) {
            mRxManager!!.clear()
        }
    }

    /**
     * 提示网络请求错误信息
     * @param msg
     * @param code
     */
    override fun showError(msg: String, code: String) {
        val mCode = "-1"
        if (mCode == code) {
            ToastUtils.showShort(mContext, msg)
        }
    }
    open fun <T> on(tag: String, c: Consumer<T>) {
        if (regEvent)
            mRxManager!!.on(tag, c)
    }

    open fun post(tag: String, c: Any) {
        if (regEvent)
            mRxManager!!.post(tag, c)
    }
    /**
     * 初始化标题
     */
    protected open fun initTitle(){}

    /**
     * 初始化数据
     */
    protected abstract fun initView()

}
