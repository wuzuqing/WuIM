package com.wuzuqing.component_im.bean;

import com.wuzuqing.component_im.common.packets.User;

public class UserBean {

    /**
     * user : {"avatar":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1532196265038&di=ebeb0b2f52342d1933738a12adaf0adb&imgtype=0&src=http%3A%2F%2Fimg10.360buyimg.com%2Fimgzone%2Fjfs%2Ft2077%2F249%2F2855394388%2F434290%2Fdae5739d%2F56f4af8eN29dfc2e5.jpg","id":"10","terminal":"tcp"}
     */

    private User user;
    private String friendStr;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFriendStr() {
        return friendStr;
    }

    public void setFriendStr(String friendStr) {
        this.friendStr = friendStr;
    }
}
