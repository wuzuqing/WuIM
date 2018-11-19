/**
 *
 */
package com.wuzuqing.component_im.common.packets;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wuzuqing.component_im.common.utils.TcpManager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

/**
 * 版本: [1.0]
 * 功能说明:
 * 作者: WChao 创建时间: 2017年7月26日 上午11:34:44
 */

@Entity
public class ChatBody extends Message implements MultiItemEntity {

    @Id
    private Long _ID;
    private Long createTime;
    private String from;//来自channel id;
    private String to;//目标channel id;
    private Integer msgType;//消息类型;(如：0:text、1:image、2:voice、3:vedio、4:music、5:news)
    private Integer chatType;//聊天类型;(如公聊、私聊)
    private String content;//消息内容;
    @Property(nameInDb = "group_id")
    private String group_id;//消息发到哪个群组;
    private String sessionId;//会话ID
    private Boolean isRead;
    private Boolean isListen; //是否听了
    private String url;  //图片
    private String localPath; //本地路径
    private int duration;
    @Transient
    private int itemType = -1;
    @Generated(hash = 992590797)
    public ChatBody(Long _ID, Long createTime, String from, String to,
                    Integer msgType, Integer chatType, String content, String group_id,
                    String sessionId, Boolean isRead, Boolean isListen, String url,
                    String localPath, int duration) {
        this._ID = _ID;
        this.createTime = createTime;
        this.from = from;
        this.to = to;
        this.msgType = msgType;
        this.chatType = chatType;
        this.content = content;
        this.group_id = group_id;
        this.sessionId = sessionId;
        this.isRead = isRead;
        this.isListen = isListen;
        this.url = url;
        this.localPath = localPath;
        this.duration = duration;
    }

    @Generated(hash = 80710669)
    public ChatBody() {
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int getItemType() {
        return itemType == -1?
                TcpManager.get().getItemType(this):itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public Long get_ID() {
        return this._ID;
    }


    public void set_ID(Long _ID) {
        this._ID = _ID;
    }


    public Long getCreateTime() {
        return this.createTime;
    }


    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }


    public String getFrom() {
        return this.from;
    }


    public void setFrom(String from) {
        this.from = from;
    }


    public String getTo() {
        return this.to;
    }


    public void setTo(String to) {
        this.to = to;
    }


    public Integer getMsgType() {
        return this.msgType;
    }


    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }


    public Integer getChatType() {
        return this.chatType;
    }


    public void setChatType(Integer chatType) {
        this.chatType = chatType;
    }


    public String getContent() {
        return this.content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public String getGroup_id() {
        return this.group_id;
    }


    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }


    public String getSessionId() {
        return this.sessionId;
    }


    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    @Override
    public String toString() {
        return "ChatBody{" +
                "_ID=" + _ID +
                ", createTime=" + createTime +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", msgType=" + msgType +
                ", chatType=" + chatType +
                ", content='" + content + '\'' +
                ", group_id='" + group_id + '\'' +
                ", isRead='" + isRead + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", url='" + url + '\'' +
                ", localPath='" + localPath + '\'' +
                '}';
    }

    public Boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getUrl() {
        return this.url;
    }

    public String getLocalPath() {
        return this.localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Boolean getIsListen() {
        return this.isListen;
    }

    public void setIsListen(Boolean isListen) {
        this.isListen = isListen;
    }

}
