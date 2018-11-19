package com.wuzuqing.component_im.client;


import com.wuzuqing.component_im.client.intel.AimMessageListener;
import com.wuzuqing.component_im.common.base.AimConfig;
import com.wuzuqing.component_im.common.base.ImPacket;
import com.wuzuqing.component_im.common.runbable.SocketRunnable;

public class AimClient {
    private static AimClient instance;
    private AimConfig config;
    private AimMessageListener listener;
    private Thread networkThread;
    private SocketRunnable runnable;
    private boolean isConnect;

    public static AimClient getInstance() {
        if (instance == null) {
            instance = new AimClient();
        }
        return instance;
    }

    private AimClient() {

    }

    public void init(AimConfig config) {
        this.config = config;
    }

    public void setListener(AimMessageListener listener) {
        this.listener = listener;
    }

    public void connect() {
        if (isConnect) return;
        if (config == null) {
            new RuntimeException("please init");
        }
        if (networkThread == null || !networkThread.isAlive()) {
            runnable = new SocketRunnable(config, listener);
            networkThread = new Thread(runnable);
            networkThread.start();
            isConnect = true;
        } else if (!isConnect) {
            networkThread.start();
        }
    }


    public void disconnect() {
        if (isConnect) {
            isConnect = false;
            if (runnable != null) {
                runnable.disconnect();
            }
        }
    }


    public boolean send(ImPacket packet) {
        if (!isConnect) return false;
        return runnable.send(packet);
    }

    public AimConfig getConfig() {
        return config;
    }

    public boolean isConnect() {
        return isConnect;
    }


}
