package com.wuzuqing.component_base.base.mvp;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.wuzuqing.component_base.R;
import com.wuzuqing.component_base.base.mvp.inter.IPresenter;
import com.wuzuqing.component_base.base.mvp.inter.IView;
import com.wuzuqing.component_base.base.mvp.inter.MvpCallback;
import com.wuzuqing.component_base.constants.BaseApplication;
import com.wuzuqing.component_base.helper.HUDFactory;
import com.wuzuqing.component_base.util.LogUtils;
import com.wuzuqing.component_base.util.StatuBarCompat;
import com.wuzuqing.component_base.util.ToastUtils;
import com.wuzuqing.component_base.widget.swipebacklayout.BGASwipeBackHelper;


import io.reactivex.disposables.CompositeDisposable;

/**
 * @Created by TOME .
 * @时间 2018/5/2 17:40
 * @描述 ${MVP模式的Base Activity}
 */

public abstract class BaseVpActivity<V extends IView, P extends IPresenter<V>> extends AppCompatActivity implements
        MvpCallback<V, P>, IView,BGASwipeBackHelper.Delegate   {

    protected P mPresenter ;
    protected V mView;
    protected boolean regEvent;
    public BaseVpActivity mActivity ;
    public KProgressHUD kProgressHUD;
    protected boolean isDestory = false;

    //管理事件流订阅的生命周期CompositeDisposable
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (isSupportSwipeBack())
            initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        doBeforeSetContentView();
        setContentView(getLayoutId());
        //加入activity管理
        BaseApplication.getAppContext().getActivityControl().addActivity(this);
        //沉浸式状态栏
        //setImmeriveStatuBar();
        mActivity = this ;
        onViewCreated();
        initTitle();
        initView();
        if (regEvent){
        }
        initListener();
    }


    protected   BGASwipeBackHelper mSwipeBackHelper ;
    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {

        mSwipeBackHelper =new  BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    /**
     * 初始化presenter
     */
    public void onViewCreated() {
        mView = createView();
        if (getPresenter()==null) {
            mPresenter = createPresenter();
            getLifecycle().addObserver(mPresenter);
        }
        mPresenter = getPresenter();
        mPresenter.attachView(getMvpView());
    }

    @CallSuper
    protected void initListener() {
        mPresenter.attachView(getMvpView());
    }



    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showHUD(String msg) {
        if (isDestory){
            return;
        }
        if (kProgressHUD == null){
            kProgressHUD = HUDFactory.getInstance().creatHUD(this);
        }
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
               // .setLabel(getString(R.string.loading))
                .setLabel(TextUtils.isEmpty(msg) ? "读取中" : msg)
               // .setLabel(null)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.3f).show();
    }

    @Override
    public void dismissHUD() {
        if (null != kProgressHUD && kProgressHUD.isShowing()) {
            kProgressHUD.dismiss();
        }
    }

    /**
     * 提示网络请求错误信息
     * @param msg
     * @param code
     */
    @Override
    public void showError(String msg, String code) {
        String mCode ="-1";
        if (mCode.equals(code)){
            ToastUtils.showShort(mActivity, msg);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i("当前运行的activity:" + getClass().getName());
    }

    public void back(View v) {
        finish();
    }



    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();

        setPresenter(null);
        setMvpView(null);
        if (regEvent) {
        }
        isDestory = true;
        dismissHUD();
        //移除类
        BaseApplication.getAppContext().getActivityControl().removeActivity(this);

    }

    /**
     * 沉浸式状态栏
     */
    protected void setImmeriveStatuBar() {
        StatuBarCompat.setImmersiveStatusBar(true, Color.WHITE, this);

    }

    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(P presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setMvpView(V view) {
        this.mView = view;
    }

    @Override
    public V getMvpView() {
        return this.mView;
    }

    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化标题
     */
    protected abstract void initTitle();

    /**
     * 初始化数据
     */
    protected abstract void initView();

    /**
     * 加载数据
     */
    protected  void loadData(){}

    /**
     * 设置layout前配置
     */
    protected void doBeforeSetContentView() {
        //设置主题
        if (isSupportSwipeBack()) {
            setTheme(R.style.JK_SwipeBack_Transparent_Theme);
        }
    }
    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        if (isSupportSwipeBack()) {
            mSwipeBackHelper.swipeBackward();
        }
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (isSupportSwipeBack() && mSwipeBackHelper!=null && mSwipeBackHelper.isSliding()) {
            return;
        }
        super.onBackPressed();
//        mSwipeBackHelper.backward();
    }
}
