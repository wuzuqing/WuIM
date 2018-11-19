package com.wuzuqing.component_im.common.runbable;

import android.util.Log;

import com.wuzuqing.component_im.client.intel.AimMessageListener;
import com.wuzuqing.component_im.common.base.AimConfig;
import com.wuzuqing.component_im.common.base.ImPacket;
import com.wuzuqing.component_im.common.exception.AioDecodeException;
import com.wuzuqing.component_im.common.exception.LengthOverflowException;
import com.wuzuqing.component_im.common.intel.ImHandler;
import com.wuzuqing.component_im.common.utils.ByteBufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * @author Administrator
 * @date 2018/5/17/017
 */
public class SocketRunnable implements Runnable {
    private final AimConfig config;
    private final AimMessageListener listener;
    private ImHandler handler;
    private static final String TAG = "SocketRunnable";

    private boolean isLive = true;
    /**
     * 新收到的数据
     */
    private ByteBuffer newByteBuffer = null;
    private Socket socket;
    private Integer READ_BUFFER_SIZE = 2048;

    public SocketRunnable(AimConfig config, AimMessageListener listener) {
        this.config = config;
        this.handler = config.handler;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(config.ip, config.port), config.timeout);
            if (socket != null && socket.isConnected()) {
                if (listener != null) {
                    listener.onConnected();
                }
            }
            InputStream inputStream = socket.getInputStream();
            byte[] buff = new byte[READ_BUFFER_SIZE];
            newByteBuffer = ByteBuffer.allocate(READ_BUFFER_SIZE);
            newByteBuffer.position(0);
            newByteBuffer.limit(0);
            int len;
            while (isLive) {
                len = inputStream.read(buff);
                newByteBuffer = ByteBufferUtils.composite(newByteBuffer, buff, len);
                ImPacket packet;
                int remainingLength = newByteBuffer.limit() - newByteBuffer.position();
                while (remainingLength > 0) {
                    //截掉上次的数据
                    newByteBuffer = ByteBufferUtils.cut(newByteBuffer);
                    if ((packet = config.handler.decode(newByteBuffer)) == null) {
                        len = inputStream.read(buff);
                        newByteBuffer = ByteBufferUtils.composite(newByteBuffer, buff, len);
                    } else {
                        if (listener != null) {
                            // System.out.println("解析完："+new String(packet.getBody(),"utf-8"));
                            listener.onReceiver(packet);
                        }
                    }
                    remainingLength = newByteBuffer.limit() - newByteBuffer.position();
                }
            }
        } catch (IOException | AioDecodeException | LengthOverflowException e) {
//            LogUtils.e(e.getMessage());
            if (isLive)
                close();
        }
    }

    private void close() {
        try {
            if (socket != null && socket.isConnected()) {
                socket.close();
                socket = null;
            }
        } catch (IOException e1) {
            Log.e(TAG, e1.getMessage());
        }
    }

    public boolean send(ImPacket packet) {
        try {
            if (!isLive || socket==null) return false;
            socket.getOutputStream().write(handler.encode(packet));
        } catch (IOException e) {
            Log.e(TAG, "send error ", e);
            return false;
        }
        return true;
    }

    public void disconnect() {
        isLive = false;
        close();
        if (listener != null) {
            listener.onDisconnect();
        }

    }
}
