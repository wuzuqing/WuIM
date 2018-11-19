package com.wuzuqing.component_base.net;


import com.wuzuqing.component_base.util.JsonUtil;
import com.wuzuqing.component_base.util.LogUtils;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @Created by TOME .
 * @时间 2018/5/18 14:32
 * @描述 ${一个网络请求和响应的所有信息合并成一条日志的逻辑处理}
 */

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    private StringBuilder mMessage = new StringBuilder();
    private boolean isBytes;
    @Override
    public void log(String message) {
        // 请求或者响应开始
//        mMessage
        if (message.startsWith("--> POST") || message.startsWith("--> GET")) {
            mMessage.setLength(0);
            isBytes = false;
        }

        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
        if ((message.startsWith("{") && message.endsWith("}"))
                || (message.startsWith("[") && message.endsWith("]")) ) {
            String decodeUnicode = JsonUtil.decodeUnicode(message);
            message = JsonUtil.formatJson(decodeUnicode);
            mMessage.append(message.concat("\n"));
        }else if (!isBytes){
            mMessage.append(message.concat("\n"));
        }
        if (message.contains("multipart/form-data")){
            isBytes = true;
        }
        // 响应结束，打印整条日志
        if (message.startsWith("<-- END HTTP")) {
            LogUtils.d(mMessage.toString());
        }

    }

}
