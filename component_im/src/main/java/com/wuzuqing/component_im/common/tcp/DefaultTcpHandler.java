package com.wuzuqing.component_im.common.tcp;


import com.wuzuqing.component_base.util.LogUtils;
import com.wuzuqing.component_im.common.base.ImPacket;
import com.wuzuqing.component_im.common.base.ImStatus;
import com.wuzuqing.component_im.common.exception.AioDecodeException;
import com.wuzuqing.component_im.common.intel.ImHandler;
import com.wuzuqing.component_im.common.packets.Command;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * 默认的编码解码器，对接jim-server-demo
 *
 * @author Administrator
 * @date 2018/5/17/017
 */
public class DefaultTcpHandler implements ImHandler {

    @Override
    public byte[] encode(ImPacket packet) {
        int bodyLen = 0;
        if (packet.getBody() != null) {
            bodyLen = packet.getBody().length;
        }
        boolean isCompress = true;
        boolean is4ByteLength = true;
        boolean isEncrypt = true;
        boolean isHasSynSeq = packet.getSynSeq() > 0;
        //协议版本号
        byte version = Protocol.VERSION;

        //协议标志位mask
        byte maskByte = ImPacket.encodeEncrypt(version, isEncrypt);
        maskByte = ImPacket.encodeCompress(maskByte, isCompress);
        maskByte = ImPacket.encodeHasSynSeq(maskByte, isHasSynSeq);
        maskByte = ImPacket.encode4ByteLength(maskByte, is4ByteLength);
        int cmdBytes = 0;
        if (packet.getCommand() != null) {
            cmdBytes = packet.getCommand().getNumber();
        }
        LogUtils.d("cmdByte:" + cmdBytes);
        packet.setVersion(version);
        packet.setMask(maskByte);
        //bytebuffer的总长度是 = 1byte协议版本号+1byte消息标志位+4byte同步序列号(如果是同步发送则都4byte同步序列号,否则无4byte序列号)+4byte命令码+4byte消息的长度+消息体
        int allLen = 1 + 1;
        if (isHasSynSeq) {
            allLen += 4;
        }
        allLen += 4 + 4 + bodyLen;
        ByteBuffer buffer = ByteBuffer.allocate(allLen);
        //设置字节序
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(packet.getVersion());
        buffer.put(packet.getMask());
        //同步发送设置4byte，同步序列号;
        if (isHasSynSeq) {
            buffer.putInt(packet.getSynSeq());
        }
        buffer.putInt(cmdBytes);
        buffer.putInt(bodyLen);
        buffer.put(packet.getBody());
        return buffer.array();
    }

    @Override
    public ImPacket decode(ByteBuffer buffer) throws AioDecodeException {
        if (!isCompletePacket(buffer)) {
            return null;
        }
        //获取第一个字节协议版本号;
        byte version = buffer.get();
        if (version != Protocol.VERSION) {
            throw new AioDecodeException(ImStatus.C10013.getText());
        }
        //标志位
        byte maskByte = buffer.get();
        Integer synSeq = 0;
        //同步发送;
        if (ImPacket.decodeHasSynSeq(maskByte)) {
            synSeq = buffer.getInt();
        }
        //cmd命令码
        int cmdByte = buffer.getInt();
        if (Command.forNumber(cmdByte) == null) {
            throw new AioDecodeException(ImStatus.C10014.getText());
        }
        int bodyLen = buffer.getInt();
        int readableLength = buffer.limit() - buffer.position();
        //数据不正确，则抛出AioDecodeException异常
        if (readableLength < bodyLen) {
            throw new AioDecodeException("bodyLength [" + bodyLen + "] is not right");
        }
        byte[] body = new byte[bodyLen];
        buffer.get(body, 0, bodyLen);
        //bytebuffer的总长度是 = 1byte协议版本号+1byte消息标志位+4byte同步序列号(如果是同步发送则多4byte同步序列号,否则无4byte序列号)+1byte命令码+4byte消息的长度+消息体的长度
        TcpPacket tcpPacket = new TcpPacket(Command.forNumber(cmdByte), body);
        tcpPacket.setVersion(version);
        tcpPacket.setMask(maskByte);
        //同步发送设置同步序列号
        if (synSeq > 0) {
            tcpPacket.setSynSeq(synSeq);
        }
        return tcpPacket;
    }

    /**
     * 判断是否完整的包
     *
     * @param buffer
     * @return
     * @throws AioDecodeException
     */
    private boolean isCompletePacket(ByteBuffer buffer) throws AioDecodeException {
        //协议头索引;
        int index = 0;
        //获取第一个字节协议版本号;
        byte version = buffer.get(index);
        if (version != Protocol.VERSION) {
            throw new AioDecodeException(ImStatus.C10013.getText());
        }
        index++;
        //标志位
        byte maskByte = buffer.get(index);
        //同步发送;
        if (ImPacket.decodeHasSynSeq(maskByte)) {
            index += 4;
        }
        index++;
        //cmd命令码
        int cmdByte = buffer.getInt(index);
        if (Command.forNumber(cmdByte) == null) {
            throw new AioDecodeException(ImStatus.C10014.getText());
        }
        index +=4;
        //消息体长度
        int bodyLen = buffer.getInt(index);
        index += 4;
        int readableLength = buffer.limit() - buffer.position();
        if (readableLength < bodyLen + index) {
            return false;
        }
        return true;
    }
}
