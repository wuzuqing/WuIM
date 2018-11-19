package com.wuzuqing.component_base.rxbus;

public class RxManagerUtil {
    private static RxManagerUtil instance;
    private RxManager rxManager;

    private RxManagerUtil() {
        rxManager = new RxManager();
    }

    public static RxManagerUtil getInstance() {
        if (instance == null) {
            instance = new RxManagerUtil();
        }
        return instance;
    }


    public void post(String tag, Object object) {
        rxManager.post(tag, object);
    }
}