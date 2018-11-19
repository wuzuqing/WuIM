package com.wuzuqing.component_im.util;

import com.wuzuqing.component_base.util.Md5;

public class ChatKit {
    public static final String CHARSET = "utf-8";
    /**
     * 获取双方会话ID(算法,from与to相与的值通过MD5加密得出)
     * @param from
     * @param to
     * @return
     */
    public static String sessionId(String from , String to){
        String sessionId = "";
        try{
            byte[] fbytes = from.getBytes(CHARSET);
            byte[] tbytes = to.getBytes(CHARSET);
            boolean isfmax = fbytes.length > tbytes.length;
            boolean isequal = fbytes.length == tbytes.length;
            if(isfmax){
                for(int i = 0 ; i < fbytes.length ; i++){
                    for(int j = 0 ; j < tbytes.length ; j++){
                        fbytes[i] = (byte) (fbytes[i]^tbytes[j]);
                    }
                }
                sessionId = new String(fbytes);
            }else if(isequal){
                for(int i = 0 ; i < fbytes.length ; i++){
                    fbytes[i] = (byte) (fbytes[i]^tbytes[i]);
                }
                sessionId = new String(fbytes);
            }else{
                for(int i = 0 ; i < tbytes.length ; i++){
                    for(int j = 0 ; j < fbytes.length ; j++){
                        tbytes[i] = (byte) (tbytes[i]^fbytes[j]);
                    }
                }
                sessionId = new String(tbytes);
            }
        }catch (Exception e) {
//            log.error(e.toString(),e);
        }
        return Md5.MD5Encode(sessionId,CHARSET);
    }
}
