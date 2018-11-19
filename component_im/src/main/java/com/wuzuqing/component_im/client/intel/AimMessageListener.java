package com.wuzuqing.component_im.client.intel;


import com.wuzuqing.component_im.common.base.ImPacket;

public interface AimMessageListener {
    void onConnected();

    void onDisconnect();

    void onReceiver(ImPacket packet);
}
