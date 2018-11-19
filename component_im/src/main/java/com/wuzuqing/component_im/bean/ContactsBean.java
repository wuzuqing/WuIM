package com.wuzuqing.component_im.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ContactsBean   {
    @Id
    private Long _ID;
    private Integer id;
    private String letter;
    private String account;
    private String password;
    private String status;
    private String nick;
    private String avatar;
    private String sex;
    @Generated(hash = 1828153353)
    public ContactsBean(Long _ID, Integer id, String letter, String account,
            String password, String status, String nick, String avatar,
            String sex) {
        this._ID = _ID;
        this.id = id;
        this.letter = letter;
        this.account = account;
        this.password = password;
        this.status = status;
        this.nick = nick;
        this.avatar = avatar;
        this.sex = sex;
    }
    @Generated(hash = 747317112)
    public ContactsBean() {
    }
    public Long get_ID() {
        return this._ID;
    }
    public void set_ID(Long _ID) {
        this._ID = _ID;
    }
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getLetter() {
        return this.letter;
    }
    public void setLetter(String letter) {
        this.letter = letter;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
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
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "ContactsBean{" +
                "_ID=" + _ID +
                ", id=" + id +
                ", letter='" + letter + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
