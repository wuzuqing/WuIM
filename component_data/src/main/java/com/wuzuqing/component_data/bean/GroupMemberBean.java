package com.wuzuqing.component_data.bean;

public class GroupMemberBean extends UserInfoBean {

    /**
     * showGroupNickName : false
     * groupName : 篮球队
     * uId : 1
     * gId : 1
     * groupManagerId : 1
     * top : false
     * disturb : false
     */

    private boolean showGroupNickName;
    private String groupName;
    private int uId;
    private int gId;
    private int groupManagerId;
    private boolean top;
    private boolean disturb;

    public boolean isShowGroupNickName() {
        return showGroupNickName;
    }

    public void setShowGroupNickName(boolean showGroupNickName) {
        this.showGroupNickName = showGroupNickName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getUId() {
        return uId;
    }

    public void setUId(int uId) {
        this.uId = uId;
    }

    public int getGId() {
        return gId;
    }

    public void setGId(int gId) {
        this.gId = gId;
    }

    public int getGroupManagerId() {
        return groupManagerId;
    }

    public void setGroupManagerId(int groupManagerId) {
        this.groupManagerId = groupManagerId;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public boolean isDisturb() {
        return disturb;
    }

    public void setDisturb(boolean disturb) {
        this.disturb = disturb;
    }

    @Override
    public String toString() {
        return "GroupMemberBean{" +
                "showGroupNickName=" + showGroupNickName +
                ", groupName='" + groupName + '\'' +
                ", uId=" + uId +
                ", gId=" + gId +
                ", groupManagerId=" + groupManagerId +
                ", top=" + top +
                ", disturb=" + disturb +
                '}';
    }
}
