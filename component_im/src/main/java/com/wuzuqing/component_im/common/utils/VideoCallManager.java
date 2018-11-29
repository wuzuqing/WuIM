package com.wuzuqing.component_im.common.utils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wuzuqing.component_base.rxbus.RxManagerUtil;
import com.wuzuqing.component_base.util.HandlerManager;
import com.wuzuqing.component_base.util.LogUtils;
import com.wuzuqing.component_base.util.TimeUtils;
import com.wuzuqing.component_data.d_arouter.RouterURLS;
import com.wuzuqing.component_im.common.packets.ChatBody;
import com.wuzuqing.component_im.db.DbCore;

public class VideoCallManager {
    private static VideoCallManager instance = new VideoCallManager();

    public static VideoCallManager get() {
        return instance;
    }

    private boolean isVideoCall;
    private ChatBody currentChatBody;

    private void toVideoCall(ChatBody chatBody) {
        currentChatBody = chatBody;
        HandlerManager.INSTANCE.runMain(() -> {
            isVideoCall = true;
            ARouter.getInstance().build(RouterURLS.IM_VIDEO_CALL)
                    .withString("from", chatBody.getFrom())
                    .withString("callId", chatBody.getUrl()).navigation();
        });
    }

    public void setCurrentChatBody(ChatBody chatBody) {
        currentChatBody = chatBody;
    }

    public boolean call(ChatBody msgBean) {
        // 0 开始拨号  1开始通话  -1 call_auto_end  -2 call_cancel  -3 target_cancel -4 target_calling
        String status = msgBean.getLocalPath();
        LogUtils.d("call:"+msgBean);
        if ("0".equals(status)) {
            if (!isVideoCall) {
                toVideoCall(msgBean);
            } else {
                msgBean.setLocalPath("-4");
                sendNewStatus(false,"-4", 0);
            }
            return true;
        } else if ("-1".equals(status) || "-2".equals(status) || "-3".equals(status) || "2".equals(status) || "-4".equals(status)) {
            isVideoCall = false;
            RxManagerUtil.getInstance().post("endCall", true);
            currentChatBody.setDuration(msgBean.getDuration());
            currentChatBody.setLocalPath(msgBean.getLocalPath());
            currentChatBody.setContent(getMsg(currentChatBody,false));
            DbCore.getDaoSession().getChatBodyDao().update(currentChatBody);
            RxManagerUtil.getInstance().post("updateCall", currentChatBody);

        } else if ("1".equals(status)) {
            RxManagerUtil.getInstance().post("answer", true);
        }
        return false;
    }

    public static String getMsg(ChatBody msg,boolean isFrom) {
        String content = "";
        //-1 call_auto_end  -2 call_cancel  -3 target_cancel -4 target_calling
        switch (msg.getLocalPath()) {
            case "2":
                content = "聊天时长 " + TimeUtils.getDuration(msg.getDuration() * 1000);
                break;
            case "1":
                content = "通话中";
                break;
            case "-1":
                content = isFrom?"已取消": "连接失败";
                break;
            case "-2":
            case "-3":
                content = isFrom?"已取消": "对方已取消";
                break;
            case "-4":
                content = isFrom?"忙线中": "对方忙线中";
                break;
        }
        return content;
    }

    public void sendNewStatus(boolean isCalled, String status, int duration) {
        isVideoCall = "1".equals(status);
        TcpManager.get().sendVideoCallStatus(currentChatBody,isCalled, status, duration);
    }


}
