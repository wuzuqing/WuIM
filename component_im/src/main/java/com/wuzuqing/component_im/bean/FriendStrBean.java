package com.wuzuqing.component_im.bean;

import java.util.List;

public class FriendStrBean {


    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {


        private List<GroupsBean> friends;
        private List<GroupsBean> groups;

        public static class GroupsBean {
            private int id; //组ID
            private String groupName;
            private String avatar;
            private List<ContactsBean> users;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getGroupName() {
                return groupName;
            }

            public void setGroupName(String groupName) {
                this.groupName = groupName;
            }

            public List<ContactsBean> getUsers() {
                return users;
            }

            public void setUsers(List<ContactsBean> users) {
                this.users = users;
            }

            @Override
            public String toString() {
                return "GroupsBean{" +
                        "id=" + id +
                        ", groupName='" + groupName + '\'' +
                        ", avatar='" + avatar + '\'' +
                        ", users=" + users +
                        '}';
            }
        }

        public List<GroupsBean> getFriends() {
            return friends;
        }

        public void setFriends(List<GroupsBean> friends) {
            this.friends = friends;
        }

        public List<GroupsBean> getGroups() {
            return groups;
        }

        public void setGroups(List<GroupsBean> groups) {
            this.groups = groups;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "friends=" + friends +
                    ", groups=" + groups +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FriendStrBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
