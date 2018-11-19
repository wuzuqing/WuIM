package com.wuzuqing.component_im.common.runbable;

import com.wuzuqing.component_im.client.AimClient;
import com.wuzuqing.component_im.common.packets.Command;
import com.wuzuqing.component_im.common.packets.HeartbeatBody;
import com.wuzuqing.component_im.common.packets.RespBody;
import com.wuzuqing.component_im.common.tcp.Protocol;
import com.wuzuqing.component_im.common.tcp.TcpPacket;

/**
 *  心跳包任务
 */
public class HeartBeatRunnable implements Runnable {
    @Override
    public void run() {
        RespBody heartbeatBody = new RespBody(Command.COMMAND_HEARTBEAT_REQ).setData(new HeartbeatBody(Protocol.HEARTBEAT_BYTE));
        TcpPacket p = new TcpPacket(Command.COMMAND_HEARTBEAT_REQ, heartbeatBody.toByte());
        AimClient.getInstance().send(p);
    }
}
