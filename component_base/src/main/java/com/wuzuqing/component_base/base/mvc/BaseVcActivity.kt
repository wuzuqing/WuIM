package com.wuzuqing.component_base.base.mvc


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import com.kaopiz.kprogresshud.KProgressHUD
import com.wuzuqing.component_base.R
import com.wuzuqing.component_base.base.mvp.inter.IView
import com.wuzuqing.component_base.constants.BaseApplication
import com.wuzuqing.component_base.helper.HUDFactory
import com.wuzuqing.component_base.rxbus.RxManager
import com.wuzuqing.component_base.util.ToastUtils
import com.wuzuqing.component_base.widget.swipebacklayout.BGASwipeBackHelper
import io.reactivex.functions.Consumer
import java.util.*

/**
 * @Created by TOME .
 * @时间 2018/6/26 17:40
 * @描述 ${MVC模式的Base Activity}
 */

abstract class BaseVcActivity : AppCompatActivity(), IView, BGASwipeBackHelper.Delegate {

    val PERMISSION_TAKE_PICTURE = arrayOf("android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")
    val PERMISSION_TAKE_VIDEO = arrayOf("android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")

    lateinit var mActivity: BaseVcActivity
    var kProgressHUD: KProgressHUD? = null
    protected var isDestory = false
    //管理事件流订阅的生命周期CompositeDisposable
//    private var compositeDisposable: CompositeDisposable? = null

    var mView: IView = this
    var mRxManager: RxManager? = null
    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract fun getLayout(): Int


    override fun onCreate(savedInstanceState: Bundle?) {
        if (isSupportSwipeBack)
            initSwipeBackFinish()
        if (intent != null) {
            initExtra(intent)
        }
        //加入activity管理
        BaseApplication.getAppContext().activityControl.addActivity(this)

        super.onCreate(savedInstanceState)
        doBeforeSetContentView()
        setContentView(getLayout())
        if (regEvent()) {
            mRxManager = RxManager()
        }

        mActivity = this

        initView()
    }

    protected fun regEvent(): Boolean {
        return true
    }

    open fun initExtra(extra: Intent) {}

    protected var mSwipeBackHelper: BGASwipeBackHelper? = null
    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private fun initSwipeBackFinish() {

        mSwipeBackHelper = BGASwipeBackHelper(this, BaseVcActivity@ this)

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper!!.setSwipeBackEnable(true)
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper!!.setIsOnlyTrackingLeftEdge(true)
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper!!.setIsWeChatStyle(true)
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper!!.setShadowResId(R.drawable.bga_sbl_shadow)
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper!!.setIsNeedShowShadow(true)
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper!!.setIsShadowAlphaGradient(true)
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper!!.setSwipeBackThreshold(0.3f)
    }

    open fun <T> on(tag: String, c: Consumer<T>) {
        if (regEvent() && mRxManager != null)
            mRxManager!!.on(tag, c)
    }

    open fun post(tag: String, c: Any) {
        if (regEvent())
            mRxManager!!.post(tag, c)
    }

    override fun getContext(): Context {
        return this
    }

    override fun showHUD(msg: String) {
        if (isDestory) {
            return
        }
        kProgressHUD = HUDFactory.getInstance().creatHUD(this)
        kProgressHUD!!.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.loading))
                .setLabel(msg)
                // .setLabel(null)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.3f).show()
    }

    override fun dismissHUD() {
        if (null != kProgressHUD && kProgressHUD!!.isShowing) {
            kProgressHUD!!.dismiss()
        }
    }

    /**
     * 提示网络请求错误信息
     *
     * @param msg
     * @param code
     */
    override fun showError(msg: String, code: String) {
        val mCode = "-1"
        if (mCode == code) {
            ToastUtils.showShort(mActivity, msg)
        }

    }

    override fun onResume() {
        super.onResume()
//        LogUtils.i("当前运行的activity:" + javaClass.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        //解除订阅关系
        if (regEvent() && mRxManager != null) {
            mRxManager!!.clear()
        }
        isDestory = true
        dismissHUD()
        //移除类
        BaseApplication.getAppContext().activityControl.removeActivity(this)

    }


    /**
     * 初始化数据
     */
    protected abstract fun initView()


    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    override fun onSwipeBackLayoutSlide(slideOffset: Float) {}

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    override fun onSwipeBackLayoutCancel() {}

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    override fun onSwipeBackLayoutExecuted() {
        if (isSupportSwipeBack) {
            mSwipeBackHelper!!.swipeBackward()
        }
    }

    override fun isSupportSwipeBack(): Boolean {
        return true
    }

    override fun onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (isSupportSwipeBack && mSwipeBackHelper!!.isSliding) {
            return
        }
        super.onBackPressed()
    }


    /**
     * 设置layout前配置
     */
    private fun doBeforeSetContentView() {
        //设置主题
        if (isSupportSwipeBack) {
            setTheme(R.style.JK_SwipeBack_Transparent_Theme)
        } else {
            setTheme(getThemeId())
        }
    }

    protected open fun getThemeId(): Int {
        return R.style.AppTheme
    }


    private fun getDeniedPermissions(context: Context, permissions: Array<String>): List<String> {
        val deniedList = ArrayList<String>(2)
        for (permission in permissions) {
            if (PermissionChecker.checkSelfPermission(context, permission!!) != PermissionChecker.PERMISSION_GRANTED) {
                deniedList.add(permission)
            }
        }
        return deniedList
    }

    /**
     * Request permission.
     */
    fun requestPermission(permissions: Array<String>, code: Int) {
        var permissions = permissions
        if (Build.VERSION.SDK_INT >= 23) {
            val deniedPermissions = getDeniedPermissions(this, permissions)
            if (deniedPermissions.isEmpty()) {
                onPermissionGranted(code)
            } else {
                ActivityCompat.requestPermissions(this, deniedPermissions.toTypedArray(), code)
            }
        } else {
            onPermissionGranted(code)
        }
    }

    private fun isGrantedResult(vararg grantResults: Int): Boolean {
        for (result in grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isGrantedResult(requestCode))
            onPermissionGranted(requestCode)
        else
            onPermissionDenied(requestCode)
    }

    protected fun onPermissionGranted(code: Int) {}

    protected fun onPermissionDenied(code: Int) {}
}
