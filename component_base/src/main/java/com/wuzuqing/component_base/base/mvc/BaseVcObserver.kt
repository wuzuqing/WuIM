package com.wuzuqing.component_base.base.mvc


import android.text.TextUtils
import android.widget.Toast
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wuzuqing.component_base.base.mvp.inter.IView
import com.wuzuqing.component_base.constants.BaseApplication
import com.wuzuqing.component_base.util.LogUtils
import com.wuzuqing.component_base.util.NetworkUtils
import com.wuzuqing.component_data.ServerException.ServerException
import io.reactivex.observers.ResourceObserver
import retrofit2.HttpException

/**
 * @author quchao
 * @date 2017/11/27
 * 观察者base
 * @param <T>
</T> */

abstract class BaseVcObserver<T> : ResourceObserver<T> {

    private var mView: IView? = null
    private var mErrorMsg: String=""
    protected var mDialogView: IView? = null
    protected var msg = "正在加载中..."
    private var isShowError = true
    private  var rlRefreshLayout: SmartRefreshLayout?=null


    protected constructor(view: IView?) {
        this.mView = view
        this.mDialogView = view
    }

    protected constructor(view: IView?, isShowHUD: Boolean) {
        this.mView = view
        if (isShowHUD) {
            this.mDialogView = view
        }
    }

    protected constructor(view: IView, rlRefresh: SmartRefreshLayout, isShowHUD: Boolean) {
        this.mView = view
        this.rlRefreshLayout = rlRefresh
        if (isShowHUD) {
            this.mDialogView = view
        }

    }




    protected constructor(view: IView?, msg1: String) {
        mView = view
        mDialogView = view
        msg = msg1
    }

    protected constructor(view: IView, dialogView: IView, msg1: String) {
        mView = view
        mDialogView = dialogView
        msg = msg1
    }

    protected constructor(view: IView, errorMsg: String, isShowError: Boolean) {
        this.mView = view
        this.mErrorMsg = errorMsg
        this.isShowError = isShowError
    }

    /**
     * 执行开始（可选）
     * 它会在subscribe(订阅)刚开始，而事件还未发送之前被调用，可以用于做一些准备工作
     * 它总是在subscribe(订阅)所发生的线程被调用(不合适在主线程加载进度条)
     */
    override fun onStart() {
        super.onStart()

        if (mDialogView != null) {
            mDialogView!!.showHUD(msg)
        }

        val currentActivity = BaseApplication.getAppContext().activityControl.currentActivity
        if (currentActivity != null && !NetworkUtils.isNetConnected(currentActivity)) {
            Toast.makeText(currentActivity, "当前无网络", Toast.LENGTH_SHORT).show()
            onComplete()
        }

    }


    /**
     * 执行结果
     */
    override fun onComplete() {
//        LogUtils.d("执行结果")
        if (mDialogView != null) {
            mDialogView!!.dismissHUD()
        } else if (mDialogView == null && rlRefreshLayout!=null) {
            rlRefreshLayout!!.finishRefresh()
            rlRefreshLayout!!.finishLoadMore()
        }
    }

    /**
     * 执行错误
     * @param e
     */
    override fun onError(e: Throwable) {
        LogUtils.d("网络异常")
        if (mView == null) {
            return
        }
        if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)) {
            mView!!.showError(mErrorMsg, "-1")
        } else if (e is ServerException) {
            mView!!.showError(e.toString(), "-1")
        } else if (e is HttpException) {
            mView!!.showError("网络异常", "-1")
        } else {
            mView!!.showError("未知错误", "-1")

        }
        // if (isShowError) {
        //     mView.showError("","-1");
        // }
        onComplete()
    }
}
