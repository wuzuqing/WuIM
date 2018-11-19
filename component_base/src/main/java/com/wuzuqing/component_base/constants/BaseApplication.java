package com.wuzuqing.component_base.constants;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.wuzuqing.component_base.BuildConfig;
import com.wuzuqing.component_base.arouter.RouterConfig;
import com.wuzuqing.component_base.util.AppUtils;
import com.wuzuqing.component_base.util.FileUtils;
import com.wuzuqing.component_base.util.LogUtils;
import com.wuzuqing.component_base.util.SPUtils;
import com.wuzuqing.component_base.util.notifyutil.NotifyUtil;
import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * @Created by TOME .
 * @时间 2018/5/14 17:20
 * @描述 ${保存全局变量设计的基本类application}
 */

public class BaseApplication extends Application{

    public static boolean IS_DEBUG = BuildConfig.DEBUG ;
    private static BaseApplication mBaseApplication ;
    //Activity管理
    private ActivityControl mActivityControl;
    private String BUGLY_ID = "a29fb52485" ;

    //SmartRefreshLayout 有三种方式,请参考:https://github.com/scwang90/SmartRefreshLayout
    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
//            layout.setPrimaryColorsId(android.R.color.white, android.R.color.white);//全局设置主题颜色
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }

    public ActivityControl getActivityControl() {
        return mActivityControl;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mBaseApplication = this ;
        //MultiDex分包方法 必须最先初始化
        MultiDex.install(this);
    }

    public static BaseApplication getAppContext(){
        return mBaseApplication;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        mActivityControl = new ActivityControl();
        //arouter路由初始化
        RouterConfig.init(this, BuildConfig.DEBUG);
        //bugly初始化
        initBugly();
        //AutoLayout适配初始化
        AutoLayoutConifg.getInstance().useDeviceSize();
        //Stetho调试工具初始化
        Stetho.initializeWithDefaults(this);

        // 初始化Logger工具
        LogUtils.init(IS_DEBUG);
        LogUtils.getConfig().setGlobalTag("LogUtils");
        SPUtils.init(getApplicationContext());
        FileUtils.initRoot(this.getApplicationContext());
        NotifyUtil.init(getApplicationContext());
    }

    private void initBugly() {
        // 获取当前包名
        String packageName = getApplicationContext().getPackageName();
        // 获取当前进程名
        String processName = AppUtils.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.initCrashReport(getApplicationContext(), BUGLY_ID, false, strategy);
    }


    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        exitApp();
    }
    /**
     * 退出应用
     */
    public void exitApp() {
        mActivityControl.finishiAll();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }


    /**
     * 低内存的时候执行
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    // 程序在内存清理的时候执行
    /**
     * 程序在内存清理的时候执行
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

    }
}
