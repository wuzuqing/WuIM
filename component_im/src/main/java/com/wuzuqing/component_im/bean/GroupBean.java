package com.wuzuqing.component_im.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class GroupBean {
    @Id
    private Long _ID;
    private int id;
    private String groupName;
    private String avatar;
    private long createTime;
    private int groupManagerId;
    @Generated(hash = 508958198)
    public GroupBean(Long _ID, int id, String groupName, String avatar,
            long createTime, int groupManagerId) {
        this._ID = _ID;
        this.id = id;
        this.groupName = groupName;
        this.avatar = avatar;
        this.createTime = createTime;
        this.groupManagerId = groupManagerId;
    }
    @Generated(hash = 405578774)
    public GroupBean() {
    }
    public Long get_ID() {
        return this._ID;
    }
    public void set_ID(Long _ID) {
        this._ID = _ID;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public int getGroupManagerId() {
        return this.groupManagerId;
    }
    public void setGroupManagerId(int groupManagerId) {
        this.groupManagerId = groupManagerId;
    }

    @Override
    public String toString() {
        return "GroupBean{" +
                "_ID=" + _ID +
                ", id=" + id +
                ", groupName='" + groupName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime=" + createTime +
                ", groupManagerId=" + groupManagerId +
                '}';
    }
}
