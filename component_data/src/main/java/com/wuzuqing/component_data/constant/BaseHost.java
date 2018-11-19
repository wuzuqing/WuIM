package com.wuzuqing.component_data.constant;

/**
 * @Created by TOME .
 * @时间 2018/5/15 17:35
 * @描述 ${TODO}
 */

public class BaseHost {

    public static final String NETEAST_HOST = "http://c.m.163.com/";
    public static final String BASE_IP = "120.79.16.93";

    public static String BASE_HOST;
    public static String STATIC_HOST;
//    public static final String BASE_HOST = "http://192.168.1.15:9090/";

    //干货集中营 API
    public static final String SINA_PHOTO_HOST = "http://gank.io/api/";

    static {
        BASE_HOST = "http://" + BASE_IP + ":9090/";
        STATIC_HOST = BASE_HOST + "example";
    }

    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {
            case HostType.NETEASE_NEWS_VIDEO:
                // host = NETEAST_HOST; String BASE_URL = "http://www.wanandroid.com/";
                host = "http://www.wanandroid.com/";
                break;
            case HostType.GANK_GIRL_PHOTO:
                host = BASE_HOST;
                break;
            case HostType.NEWS_DETAIL_HTML_PHOTO:
                host = "http://kaku.com/";
                break;
            default:
                host = "";
                break;
        }
        return host;
    }
}
