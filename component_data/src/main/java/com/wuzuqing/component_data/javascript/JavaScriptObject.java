package com.wuzuqing.component_data.javascript;

import android.webkit.JavascriptInterface;
import android.widget.Toast;


/**
 * 安卓端与后台web端的交互
 * Created by Administrator on 2017/3/4.
 */

public class JavaScriptObject {

    @JavascriptInterface
    public void toast(String text) {
    }

}
