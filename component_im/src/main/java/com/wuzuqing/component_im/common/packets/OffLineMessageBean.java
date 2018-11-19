/**
 *
 */
package com.wuzuqing.component_im.common.packets;

import java.util.List;

/**
 * 版本: [1.0]
 * 功能说明:
 * 作者: WChao 创建时间: 2017年7月26日 上午11:34:44
 */


public class OffLineMessageBean  {

 private List<ChatBody> data;

    public List<ChatBody> getData() {
        return data;
    }

    public void setData(List<ChatBody> data) {
        this.data = data;
    }
}
