package com.wuzuqing.component_data.d_arouter;

/**
 * @Created by TOME .
 * @时间 2018/4/26 10:20
 * @描述 ${路由地址}
 */
// 注意事项！！！  /模块/页面名称   或  /模块/子模块../页面名称   至少两级  例子 /商城/商品详情 /shop/shopDetails
    //url 第一次相同会报错??
public interface RouterURLS {

    String IM_MAIN = "/im/home";
    String IM_CHAT = "/im/chat";
    String IM_AUDIL_CALL = "/im/audio_call";
    String IM_VIDEO_CALL = "/im/video_call";
    String IM_GROUP_LIST = "/im/groupList";
    String USER_CENTER_LOGIN = "/user/login";


    String CUSTOM_CONTROL = "/custom/control";


}
