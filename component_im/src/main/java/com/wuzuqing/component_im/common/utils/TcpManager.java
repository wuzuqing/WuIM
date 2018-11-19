package com.wuzuqing.component_im.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.wuzuqing.component_base.constants.BaseApplication;
import com.wuzuqing.component_base.luban.Luban;
import com.wuzuqing.component_base.net.ModelService;
import com.wuzuqing.component_base.rxbus.RxManager;
import com.wuzuqing.component_base.util.FileUtils;
import com.wuzuqing.component_base.util.HandlerManager;
import com.wuzuqing.component_base.util.LogUtils;
import com.wuzuqing.component_base.util.ObjectUtils;
import com.wuzuqing.component_base.util.SPUtils;
import com.wuzuqing.component_base.util.notifyutil.NotifyUtil;
import com.wuzuqing.component_data.bean.UserInfoBean;
import com.wuzuqing.component_data.cache.GlobalVariable;
import com.wuzuqing.component_data.d_arouter.RxTag;
import com.wuzuqing.component_im.R;
import com.wuzuqing.component_im.bean.ContactsBean;
import com.wuzuqing.component_im.bean.Conversation;
import com.wuzuqing.component_im.bean.FriendStrBean;
import com.wuzuqing.component_im.bean.GroupBean;
import com.wuzuqing.component_im.bean.UserBean;
import com.wuzuqing.component_im.client.AimClient;
import com.wuzuqing.component_im.client.intel.AimMessageListener;
import com.wuzuqing.component_im.common.base.AimConfig;
import com.wuzuqing.component_im.common.base.ImPacket;
import com.wuzuqing.component_im.common.packets.ChatBody;
import com.wuzuqing.component_im.common.packets.ChatType;
import com.wuzuqing.component_im.common.packets.Command;
import com.wuzuqing.component_im.common.packets.LoginReqBody;
import com.wuzuqing.component_im.common.packets.OffLineMessageBean;
import com.wuzuqing.component_im.common.packets.OffLineMessageReqBody;
import com.wuzuqing.component_im.common.runbable.HeartBeatRunnable;
import com.wuzuqing.component_im.common.tcp.DefaultTcpHandler;
import com.wuzuqing.component_im.common.tcp.TcpPacket;
import com.wuzuqing.component_im.contract.ChatItem;
import com.wuzuqing.component_im.dao.ChatBodyDao;
import com.wuzuqing.component_im.db.DbCore;
import com.wuzuqing.component_im.util.ChatKit;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TcpManager {

    private static final int WHAT_HEART = 99;
    private static final int WHAT_CHAT_CMD = 11;
    private static final int WHAT_USER_INFO_CMD = 6;
    private static final int WHAT_OFFLINE_MESSAGE_CMD = 22;

    private static TcpManager tcpManager = new TcpManager();
    private AimClient client = AimClient.getInstance();
    private String userId;      //当前用户ID
    private String toId;        //私聊对方的ID
    private static final String TAG = "TcpManager";
    private Handler handler;    //处理消息切换
    private String sessionId;   //当前会话的ID
    private ChatType chatType = ChatType.CHAT_TYPE_UNKNOW; //是否在聊天界面
    private boolean isShowConversationFragment;            //是否在会话界面
    private RxManager rxManager;
    private boolean hasNewMsg; //是否有信息需要处理

    private Class chatClz;
    private boolean isFirstConnect = true;
    private long lastMsgTime;

    public static TcpManager get() {
        return tcpManager;
    }


    public void init() {
        init("192.168.1.239", 9091);
//        init(BaseHost.BASE_IP, 9091);
        List<ChatBody> list = DbCore.getDaoSession().getChatBodyDao().queryBuilder()
                .orderDesc(ChatBodyDao.Properties.CreateTime).limit(1).list();
        if (list != null && list.size() > 0) {
            lastMsgTime = list.get(0).getCreateTime();
        } else {
            lastMsgTime = SPUtils.getLong(getKeyToLastTime());
        }
    }

    public void init(String ip, int port) {
        AimConfig config = new AimConfig();
        config.ip = ip;
        config.port = port;
        config.handler = new DefaultTcpHandler();
        client.init(config);
        client.setListener(listener);
        rxManager = new RxManager();
        LogUtils.d("init.ip:"+ip);
    }

    private AimMessageListener listener = new AimMessageListener() {
        @Override
        public void onConnected() {
            initHandler();
            sendLogin();
        }

        @Override
        public void onDisconnect() {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }
        }

        @Override
        public void onReceiver(ImPacket packet) {
            try {
                JSONObject jObj = new JSONObject(new String(packet.getBody(), "utf-8"));
                LogUtils.d("onReceiver: " + jObj.toString());
                int command = jObj.getInt("command");
                String data = "";
                if (jObj.has("data")) {
                    data = jObj.getString("data");
                }
                switch (command) {
                    case WHAT_USER_INFO_CMD:
                        if (isFirstConnect) {
                            isFirstConnect = false;
                            UserBean userBean = JsonKit.toBean(data, UserBean.class);
                            FriendStrBean friendStrBean = JsonKit.toBean(userBean.getFriendStr(), FriendStrBean.class);
                            setUserId(userBean.getUser().getId());
                            updateContacts(friendStrBean.getData());
                        }
                        loopSendHeart();
                        //获取离线消息
//                        long lastTime = SPUtils.getLong(getKeyToLastTime());
                        LogUtils.d("lastMsgTime:" + lastMsgTime);
                        sendOffLineMessage(lastMsgTime);
                        break;
                    case WHAT_OFFLINE_MESSAGE_CMD:
                        doOffLineMessage = true;
                        OffLineMessageBean userBean = JsonKit.toBean(data, OffLineMessageBean.class);
                        setOffLineMessage(userBean.getData());
                        break;
                    case WHAT_CHAT_CMD:
                        ChatBody msgBean = JsonKit.toBean(data, ChatBody.class);
                        doChat(command, msgBean, true);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private int MSG_NOTIFY_ID = 0x5556;

    private void showNotify(ChatBody body, boolean isGroup, int targetId, String nick, String avatar) {
        Map<String, Object> params = new HashMap<>();
        params.put("isPrivate", !isGroup);
        params.put("groupId", body.getGroup_id());
        params.put("targetId", targetId);
        params.put("nick", nick);
        params.put("avatar", avatar);
        HandlerManager.INSTANCE.runMain(() -> NotifyUtil.buildSimple(MSG_NOTIFY_ID,
                R.mipmap.ic_language, nick, body.getContent(), NotifyUtil.buildIntent(chatClz, params)).show());
    }

    private boolean doOffLineMessage;

    //登录聊天服务器返回的用户信息
    private void updateContacts(FriendStrBean.DataBean data) {
        if (ObjectUtils.isNotEmpty(data.getFriends())) {
            FriendStrBean.DataBean.GroupsBean bean = data.getFriends().get(0);
            if (ObjectUtils.isNotEmpty(bean.getUsers())) {
                DbCore.getDaoSession().getContactsBeanDao().deleteAll();
                DbCore.getDaoSession().getContactsBeanDao().insertOrReplaceInTx(bean.getUsers());
            }
        }

        if (ObjectUtils.isNotEmpty(data.getGroups())) {
            List<FriendStrBean.DataBean.GroupsBean> groupsBeans = data.getGroups();
            List<GroupBean> groupBeans = new ArrayList<>();
            GroupBean groupBean;
            for (FriendStrBean.DataBean.GroupsBean bean : groupsBeans) {
                groupBean = new GroupBean();
                groupBean.setId(bean.getId());
                groupBean.setGroupName(bean.getGroupName());
                groupBean.setCreateTime(0);
                groupBean.setGroupManagerId(0);
                groupBean.setAvatar(bean.getAvatar());
                groupBeans.add(groupBean);
            }
            DbCore.getDaoSession().getGroupBeanDao().deleteAll();
            DbCore.getDaoSession().getGroupBeanDao().insertOrReplaceInTx(groupBeans);
        }
    }


    private void initHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_CHAT_CMD:            //如果在聊天界面则
                        disposeMessage.dispose((ChatBody) msg.obj);
                        break;
                    case WHAT_HEART: //心跳包
                        sendHeartBeat();
                        loopSendHeart();
                        break;
                }
            }
        };
    }

    /**
     * @param command
     * @param msgBean
     */
    private void doChat(int command, ChatBody msgBean, boolean unRead) {
        //数据库存储
        hasNewMsg = true;

        if (unRead) {
            switch (chatType) {
                case CHAT_TYPE_PUBLIC: //群聊
                    msgBean.setIsRead(msgBean.getGroup_id().equals(GlobalVariable.Companion.get().getTargetId()));
                    break;
                case CHAT_TYPE_PRIVATE:
                    msgBean.setIsRead(GlobalVariable.Companion.checkChat(Integer.valueOf(msgBean.getFrom()), Integer.valueOf(msgBean.getTo())));
                    break;
                default:
                    msgBean.setIsRead(false);
                    break;
            }
        }
        msgBean.setIsListen(msgBean.getFrom().equals(userId));


        DbCore.getDaoSession().getChatBodyDao().insert(msgBean);
        updateConversation(msgBean);

        if (isShowConversationFragment && !doOffLineMessage) {
            rxManager.post(RxTag.UPDATE_CONVERSATION, msgBean);
        }
        // 如果在聊天界面则刷新列表
        if (disposeMessage != null) {
            handler.obtainMessage(command, msgBean).sendToTarget();
        }

        lastMsgTime = msgBean.getCreateTime();
    }


    // 获取回话ID
    private String getRealSessionId(ChatBody body, boolean isGroup) {
        if (isGroup) {
            return String.valueOf(body.getGroup_id());
        } else {
            return body.getSessionId();
        }
    }

    //获取对方的ID
    private String getTargetId(ChatBody body, boolean isGroup) {
        if (isGroup) {
            return body.getGroup_id();
        } else {
            return userId.equals(body.getFrom()) ? body.getTo() : body.getFrom();
        }
    }

    private void updateConversation(ChatBody msgBean) {
        boolean isGroup = msgBean.getChatType() == 1;
        String sessionId = getRealSessionId(msgBean, isGroup);
        List<Conversation> list = DbCore.getDaoSession().getConversationDao().queryRaw("where SESSION_ID = ? and USER_ID = ?", sessionId, userId);
        Conversation conversation = null;
        if (ObjectUtils.isEmpty(list)) {
            conversation = new Conversation();
            Integer targetId = Integer.valueOf(getTargetId(msgBean, isGroup));

            if (isGroup) {
                List<GroupBean> groupBeans = DbCore.getDaoSession().getGroupBeanDao().queryRaw("where ID = ?", String.valueOf(targetId));
                if (ObjectUtils.isNotEmpty(groupBeans)) {
                    GroupBean bean = groupBeans.get(0);
                    conversation.setNick(bean.getGroupName());
                    conversation.setAvatar(bean.getAvatar());
                }
            } else {
                List<ContactsBean> contactsBeanList = DbCore.getDaoSession().getContactsBeanDao().queryRaw("where ID = ?", String.valueOf(targetId));
                if (ObjectUtils.isNotEmpty(contactsBeanList)) {
                    ContactsBean bean = contactsBeanList.get(0);
                    conversation.setNick(bean.getNick());
                    conversation.setAvatar(bean.getAvatar());
                }
            }

            conversation.setIsTop(false);
            conversation.setUserId(Integer.valueOf(userId));
            conversation.setIsDisturb(false);
            conversation.setSessionId(sessionId);
            conversation.setChatType(msgBean.getChatType());
            conversation.setTargetId(targetId);
            updateLastData(msgBean, conversation, isGroup, true);

        } else {
            updateLastData(msgBean, list.get(0), isGroup, false);
        }

    }

    private void updateLastData(ChatBody msgBean, Conversation conversation, boolean isGroup, boolean insert) {
        conversation.setLastMessageContent(msgBean.getContent());
        conversation.setLastMsgType(msgBean.getMsgType());
        conversation.setLastMessageCreateTime(msgBean.getCreateTime());
        conversation.setLastMessageId(msgBean.getId());
        //发送通知
        if (!msgBean.getIsRead()) {
            showNotify(msgBean, isGroup, conversation.getTargetId(), conversation.getNick(), conversation.getAvatar());
        }
        if (insert) {
            DbCore.getDaoSession().getConversationDao().insertInTx(conversation);
        } else {
            DbCore.getDaoSession().getConversationDao().update(conversation);
        }
    }


    public void setOffLineMessage(@Nullable List<ChatBody> it) {
        if (ObjectUtils.isNotEmpty(it)) {
            for (ChatBody body : it) {
                body.setIsRead(false);
                body.setIsListen(false);
                updateConversation(body);
            }
            DbCore.getDaoSession().getChatBodyDao().insertInTx(it);
        }
        refreshConversation(true);
        doOffLineMessage = false;
    }

    private void loopSendHeart() {
        handler.sendEmptyMessageDelayed(WHAT_HEART, 30000);
    }


    private void setUserId(String userId) {
        this.userId = userId;
    }

    public void setToId(String toId) {
        this.toId = toId;
        if (!(TextUtils.isEmpty(userId) || TextUtils.isEmpty(toId))) {
            sessionId = ChatKit.sessionId(userId, toId);
        }
    }

    private boolean isConnect() {
        return client.isConnect();
    }

    public void sendMsg(final String to, final String groupId, final int chatType, final int msgType,
                        final String content, final String url, String localPath, int duration) {
        if (!isConnect()) return;
        new Thread(() -> {
            ChatBody chat = new ChatBody();
            chat.setFrom(userId);
            chat.setTo(to);
            chat.setMsgType(msgType);
            chat.setChatType(chatType);
            chat.setIsRead(true);
            chat.setUrl(url);
            chat.setLocalPath(localPath);
            chat.setGroup_id(groupId);
            chat.setContent(content);
            chat.setDuration(duration);
            chat.setIsListen(true);
            chat.setCreateTime(System.currentTimeMillis());
            if (chatType == 2) {
                chat.setSessionId(sessionId);
            } else {
                chat.setSessionId(groupId);
            }
            TcpPacket p = new TcpPacket(Command.COMMAND_CHAT_REQ, chat.toByte());
            client.send(p);
            if (chatType == 2) {
                doChat(Command.COMMAND_CHAT_REQ.getNumber(), chat, false);
            }
        }).start();

    }


    public boolean sendPrivateMsg(int msgType, String content, String url, String localPath) {
        if (!isConnect()) return false;
        sendMsg(toId, null, 2, msgType, content, url, localPath, 0);
        return true;
    }

    public boolean sendPublicMsg(int msgType, String content, String url, String localPath) {
        if (!isConnect()) return false;
        sendMsg(null, GlobalVariable.Companion.get().getTargetId(), 1, msgType, content, url, localPath, 0);
        return true;
    }

    public boolean sendMedia(int chatType, int msgType, String content, String url, String localPath, int duration) {
        if (!isConnect()) return false;
        if (chatType == 1) {
            sendMsg(null, GlobalVariable.Companion.get().getTargetId(), 1, msgType, content, url, localPath, duration);
        } else {
            sendMsg(toId, null, 2, msgType, content, url, localPath, duration);
        }

        return true;
    }


    public boolean sendLogin() {
        if (!isConnect()) return false;
        UserInfoBean user = GlobalVariable.Companion.get().getUser();
        if (ObjectUtils.isEmpty(user)) {
            return false;
        }
        byte[] loginBody = new LoginReqBody(user.getToken()).setFirstConnect(isFirstConnect).toByte();
        TcpPacket p = new TcpPacket(Command.COMMAND_LOGIN_REQ, loginBody);
        return client.send(p);
    }

    public boolean sendOffLineMessage(long lastTime) {
        if (!isConnect()) return false;
        byte[] bytes = new OffLineMessageReqBody(GlobalVariable.Companion.get().getUserId(), lastTime).toByte();
        TcpPacket p = new TcpPacket(Command.COMMAND_GET_OFFLINE_MESSAGE_REQ, bytes);
        return client.send(p);
    }

    private HeartBeatRunnable heartBeatRunnable = new HeartBeatRunnable();

    public boolean sendHeartBeat() {
        if (!isConnect()) return false;
        new Thread(heartBeatRunnable).start();
        return true;
    }


    public AimClient getClient() {
        return client;
    }

    public void connect() {
        getClient().connect();
    }

    private static String LAST_CREATE_TIME_KEY = "LAST_CREATE_TIME_KEY";

    public void disconnect(boolean isReconnect) {
        if (client.isConnect()) {
            client.disconnect();
        }
        List<ChatBody> list = DbCore.getDaoSession().getChatBodyDao().queryBuilder()
                .orderDesc(ChatBodyDao.Properties.CreateTime).limit(1).list();
        if (list != null && list.size() > 0) {
            lastMsgTime = list.get(0).getCreateTime();
        }
        SPUtils.setLong(getKeyToLastTime(), lastMsgTime);
        DbCore.onExit();
        isFirstConnect = isReconnect;
    }

    private String getKeyToLastTime() {
        return String.format("%s_%d", LAST_CREATE_TIME_KEY, SPUtils.getInt("userId"));
    }


    public String getUserId() {
        return userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getItemType(ChatBody body) {
        boolean isSelf = body.getFrom().equals(TcpManager.get().getUserId());
        switch (body.getMsgType()) {
            case 0:
                return isSelf ? ChatItem.ITEM_TYPE_TEXT_FROM : ChatItem.ITEM_TYPE_TEXT_TO;
            case 1:
            case 3:
                return isSelf ? ChatItem.ITEM_TYPE_IMG_FROM : ChatItem.ITEM_TYPE_IMG_TO;
            case 2:
                return isSelf ? ChatItem.ITEM_TYPE_VOICE_FROM : ChatItem.ITEM_TYPE_VOICE_TO;
//            case 3:
//
//                return isSelf ? ChatItem.ITEM_TYPE_VIDEO_FROM : ChatItem.ITEM_TYPE_VIDEO_TO;
        }
        return 555;
    }


    /**
     * 离开聊天也没
     */
    public void levelChat() {
        setChatType(ChatType.CHAT_TYPE_UNKNOW);
        setDisposeMessage(null);
        refreshConversation();
    }

    public void setChatType(@NotNull ChatType chatType) {
        this.chatType = chatType;
    }

    public void setShowConversationFragment(boolean visible) {
        this.isShowConversationFragment = visible;
    }


    private DisposeMessage disposeMessage;
    int i = 0;

    public void sendImage(boolean isPrivate, @Nullable List<String> paths) {
        if (ObjectUtils.isEmpty(paths)) return;
        i = 0;
        for (String localPath : paths) {
            Luban.with(BaseApplication.getAppContext())
                    .load(localPath)
                    .ignoreBy(80)
                    .setFocusAlpha(false)
                    .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
                    .setRenameListener(filePath -> {
                        i++;
                        return String.format("%s%d%d.jpg", userId, i, System.currentTimeMillis());
                    })
                    .setCompressListener(new com.wuzuqing.component_base.luban.OnCompressListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(File file) {
                            try {
                                ModelService.uploadFile(null, file, TcpManager.get().userId, result -> {
                                    if (isPrivate) {
                                        sendPrivateMsg(1, MSG_IMAGE_CT, result, localPath);
                                    } else {
                                        sendPublicMsg(1, MSG_IMAGE_CT, result, localPath);
                                    }
                                    FileUtils.deleteFile(file);
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }
                    }).launch();
        }
    }

    private static final String MSG_VOICE_CT = "[语音]";
    private static final String MSG_VIDEO_CT = "[视频]";
    private static final String MSG_IMAGE_CT = "[图片]";

    public void sendVoice(boolean isPrivate, @Nullable String voiceFilePath, int duration) {
        try {
            ModelService.uploadFile(null, new File(voiceFilePath), TcpManager.get().userId, result -> {
                sendMedia(isPrivate ? 2 : 1, 2, MSG_VOICE_CT, result, voiceFilePath, duration);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendVideo(boolean isPrivate, @Nullable String videoFilePath, int duration) {
        try {
            ModelService.uploadFile(null, new File(videoFilePath), TcpManager.get().userId, result ->
                    sendMedia(isPrivate ? 2 : 1, 3, MSG_VIDEO_CT, result, videoFilePath, duration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setChatClass(@NotNull Class clz) {
        chatClz = clz;
    }

    /**
     * 聊天界面注册事件
     */
    public interface DisposeMessage {
        void dispose(ChatBody message);
    }

    public void setDisposeMessage(DisposeMessage disposeMessage) {
        this.disposeMessage = disposeMessage;
    }


    /**
     * 是否刷新会话列表及未读消息数量
     */
    public interface OnRefreshConversationListener {
        void refresh();
    }

    private OnRefreshConversationListener refreshConversation;

    public void setRefreshConversationListener(OnRefreshConversationListener refreshConversation) {
        this.refreshConversation = refreshConversation;
    }

    public void refreshConversation() {
        if (refreshConversation != null && hasNewMsg) {
            hasNewMsg = false;
            refreshConversation.refresh();
        }
    }

    public void refreshConversation(boolean need) {
        if (refreshConversation != null) {
            hasNewMsg = false;
            HandlerManager.INSTANCE.runMain(() -> refreshConversation.refresh());
        }
    }

    public boolean isHasNewMsg() {
        return hasNewMsg;
    }
}
