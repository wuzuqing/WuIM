package com.wuzuqing.component_data.bean;


public class UserInfoBean {

    /**
     * id : 2
     * account : 13229981761
     * password : 123456
     * status : online
     * nick : 张飞
     * avatar : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1532196265038&di=ebeb0b2f52342d1933738a12adaf0adb&imgtype=0&src=http%3A%2F%2Fimg10.360buyimg.com%2Fimgzone%2Fjfs%2Ft2077%2F249%2F2855394388%2F434290%2Fdae5739d%2F56f4af8eN29dfc2e5.jpg
     * sex : 男
     */

    private int id;
    private String account;
    private String password;
    private String status;
    private String nick;
    private String avatar;
    private String sex;
    private String token;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }

    public UserInfoBean() {
    }

    public UserInfoBean(int id, String nick, String avatar) {
        this.id = id;
        this.nick = nick;
        this.avatar = avatar;
    }
}
