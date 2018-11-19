package com.wuzuqing.component_base.constants;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;

import com.wuzuqing.component_base.util.LogUtils;

import java.util.List;
import java.util.Stack;


/**
 * 类描述    :管理所有Activity
 * 包名      : com.fecmobile.jiubeirobot.base
 * 类名称    : ActivityControl
 * 创建人    : ghy
 * 创建时间  : 2017/2/21 19:54
 * 修改人    :
 * 修改时间  :
 * 修改备注  :
 */
public class ActivityControl {
    private Stack<Activity> allActivities = new Stack<>();

    /**
     * 描述      :  获取当前运行的Activity,有可能返回null
     * 方法名    :  getCurrentActivity
     * param    : 无
     * 返回类型  : BaseActivity
     * 创建人    : ghy
     * 创建时间  : 2017/2/21 19:56
     * 修改人    :
     * 修改时间
     * 修改备注
     * throws
     */
    public Activity getCurrentActivity() {
        try {
            return allActivities.lastElement();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取倒数第二个 Activity
     *
     * @return
     */
    @Nullable
    public Activity getPenultimateActivity() {
        Activity activity = null;
        try {
            if (allActivities.size() > 1) {
                activity = allActivities.get(allActivities.size() - 2);
            }
        } catch (Exception e) {
        }
        return activity;
    }

    public void onPanelSlide(float slideOffset) {
        try {
            Activity activity = getPenultimateActivity();
            if (activity != null) {
                View decorView = activity.getWindow().getDecorView();
                ViewCompat.setTranslationX(decorView, -(decorView.getMeasuredWidth() / 3.0f) * (1 - slideOffset));
            }
        } catch (Exception e) {
        }
    }


    public void onPanelClosed() {
        try {
            Activity activity = getPenultimateActivity();
            if (activity != null) {
                View decorView = activity.getWindow().getDecorView();
                ViewCompat.setTranslationX(decorView, 0);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 描述      : 添加Activity到管理
     * 方法名    :  addActivity
     * param    :   act Activity
     * 返回类型  : void
     * 创建人    : ghy
     * 创建时间  : 2017/2/21 19:57
     * 修改人    :
     * 修改时间
     * 修改备注
     * throws
     */
    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new Stack<>();
        }
        allActivities.add(act);
    }

    /**
     * 描述      :从管理器移除Activity，一般在Ondestroy移除，防止强引用内存泄漏
     * 方法名    :  removeActivity
     * param    :   act Activity
     * 返回类型  : void
     * 创建人    : ghy
     * 创建时间  : 2017/2/21 19:57
     * 修改人    :
     * 修改时间
     * 修改备注
     * throws
     */
    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            allActivities.remove(activity);
            activity.finish();
        }
    }


    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        try {
            for (Activity activity : allActivities) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 结束指定类名的Activity
     */
    public Activity getActivityByClassName(String className) {
        try {
            LogUtils.d("className = " + className + "\n" + allActivities.toString());
            for (Activity activity : allActivities) {
                if (activity.getClass().getSimpleName().equals(className)) {
                    return activity;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 描述      :关闭所有Activity
     * 方法名    :  finishiAll
     * param    :无
     * 返回类型  :void
     * 创建人    : ghy
     * 创建时间  : 2017/2/21 19:58
     * 修改人    :
     * 修改时间
     * 修改备注
     * throws
     */
    public void finishiAll() {
        if (allActivities != null) {
            for (Activity act : allActivities) {
                act.finish();
            }
        }
    }


    /**
     * 描述      :关闭所有Activity 除了对应的activity
     * 方法名    :  finishiAll
     * param    :无
     * 返回类型  :void
     * 创建人    : ghy
     * 创建时间  : 2017/2/21 19:58
     * 修改人    :
     * 修改时间
     * 修改备注
     * throws
     */
    public void finishiAllExcept(Activity activity) {
        if (allActivities != null) {
            for (Activity act : allActivities) {
                if (act != activity) {
                    act.finish();
                }
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishiAll();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出应用程序
     */
    public void logout(Class toClz) {
        try {
            finishiAll();
            // 杀死该应用进程
            Context context = BaseApplication.getAppContext().getApplicationContext();
            Intent login = new Intent(context, toClz);
            login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 判断某个界面是否在前台
     *
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    public boolean isForeground() {
        return isForeground(getCurrentActivity());
    }
    /*
     * 判断某个界面是否在前台
     *
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */

    public boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) return true;
        }
        return false;
    }


}
