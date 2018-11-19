package com.wuzuqing.component_im.common.utils;//package org.jim.aim.common.utils;
//
//
///**
// * @author Administrator
// * @date 2018/5/15/015
// */
//public class ParseUtil {
//    /**
//     *  byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序
//     * @param src
//     * @param offset
//     * @return
//     */
//    public static int getMsgLen(byte[] src,int offset) {
//        int value = 0;
//        value = (int) ( ((src[offset] & 0xFF)<<24)
//                |((src[offset+1] & 0xFF)<<16)
//                |((src[offset+2] & 0xFF)<<8)
//                |(src[offset+3] & 0xFF));
//        return value;
//    }
//}
