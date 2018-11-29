package wuzuqing.com.module_im.arouter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wuzuqing.component_data.d_arouter.RouterURLS;

/**
 * @Created by TOME .
 * @时间 2018/4/26 10:19
 * @描述 ${路由中心}
 */
//ARouter 提供了大量的参数类型 跳转携带 https://blog.csdn.net/zhaoyanjun6/article/details/76165252
public class RouterCenter {

    /**
     * 跳转到图片预览图
     */
    public static void toChat(boolean isPrivate, String groupId,int targetId,String nick,String avatar) {
        ARouter.getInstance().build(RouterURLS.IM_CHAT)
                .withString("groupId", groupId)
                .withInt("targetId", targetId)
                .withString("nick", nick)
                .withString("avatar", avatar)
                .withBoolean("isPrivate", isPrivate).navigation();
    }
    public static void toGroupList() {
        ARouter.getInstance().build(RouterURLS.IM_GROUP_LIST).navigation();
    }

    public static void toVideoCall(boolean called){
        ARouter.getInstance().build(RouterURLS.IM_VIDEO_CALL)
                .withBoolean("called",called)
                .navigation();
    }

}
