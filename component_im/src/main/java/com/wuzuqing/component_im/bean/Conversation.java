package com.wuzuqing.component_im.bean;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class Conversation implements Comparable<Conversation> {
    @Id
    private Long id;
    private Integer userId;
    private Integer chatType;
    private String sessionId; //私聊会话ID
    private Integer targetId;       //对方id
    private String nick;
    private String avatar; //如果是群聊 则用 , 连接返回
    private Boolean isTop;      //是否置顶
    private Boolean isDisturb;  //是否免打扰

    private String lastMessageContent;
    private String lastMessageId;
    private Long lastMessageCreateTime;
    private String lastMessageNick;
    private Integer lastMsgType;


    @Transient
    private int unReadCount;

    @Generated(hash = 1274390372)
    public Conversation(Long id, Integer userId, Integer chatType, String sessionId,
                        Integer targetId, String nick, String avatar, Boolean isTop,
                        Boolean isDisturb, String lastMessageContent, String lastMessageId,
                        Long lastMessageCreateTime, String lastMessageNick,
                        Integer lastMsgType) {
        this.id = id;
        this.userId = userId;
        this.chatType = chatType;
        this.sessionId = sessionId;
        this.targetId = targetId;
        this.nick = nick;
        this.avatar = avatar;
        this.isTop = isTop;
        this.isDisturb = isDisturb;
        this.lastMessageContent = lastMessageContent;
        this.lastMessageId = lastMessageId;
        this.lastMessageCreateTime = lastMessageCreateTime;
        this.lastMessageNick = lastMessageNick;
        this.lastMsgType = lastMsgType;
    }

    @Generated(hash = 1893991898)
    public Conversation() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getChatType() {
        return this.chatType;
    }

    public void setChatType(Integer chatType) {
        this.chatType = chatType;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getTargetId() {
        return this.targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getIsTop() {
        return this.isTop;
    }

    public void setIsTop(Boolean isTop) {
        this.isTop = isTop;
    }

    public Boolean getIsDisturb() {
        return this.isDisturb;
    }

    public void setIsDisturb(Boolean isDisturb) {
        this.isDisturb = isDisturb;
    }

    public String getLastMessageContent() {
        return this.lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public String getLastMessageId() {
        return this.lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public Long getLastMessageCreateTime() {
        return this.lastMessageCreateTime;
    }

    public void setLastMessageCreateTime(Long lastMessageCreateTime) {
        this.lastMessageCreateTime = lastMessageCreateTime;
    }

    public String getLastMessageNick() {
        return this.lastMessageNick;
    }

    public void setLastMessageNick(String lastMessageNick) {
        this.lastMessageNick = lastMessageNick;
    }

    public Integer getLastMsgType() {
        return this.lastMsgType;
    }

    public void setLastMsgType(Integer lastMsgType) {
        this.lastMsgType = lastMsgType;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", userId=" + userId +
                ", chatType=" + chatType +
                ", sessionId='" + sessionId + '\'' +
                ", targetId=" + targetId +
                ", nick='" + nick + '\'' +
                ", isTop=" + isTop +
                ", isDisturb=" + isDisturb +
                ", lastMessageContent='" + lastMessageContent + '\'' +
                ", lastMessageId='" + lastMessageId + '\'' +
                ", lastMessageCreateTime=" + lastMessageCreateTime +
                ", lastMessageNick='" + lastMessageNick + '\'' +
                ", lastMsgType=" + lastMsgType +
                ", unReadCount=" + unReadCount +
                '}';
    }

    @Override
    public int compareTo(@NonNull Conversation o) {
        return (int) (this.lastMessageCreateTime - o.lastMessageCreateTime);
    }
}
