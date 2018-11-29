package wuzuqing.com.module_im.util;

import android.Manifest;
import android.app.Activity;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.widget.Toast;

import com.cjt2325.cameralibrary.util.LogUtil;

import org.json.JSONException;
import org.webrtc.MediaStream;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;

import java.lang.ref.WeakReference;

import cn.wuzuqing.component_web_client.PeerConnectionParameters;
import cn.wuzuqing.component_web_client.WebRtcClient;

public class VideoCallManager implements WebRtcClient.RtcListener {
    private final static int VIDEO_CALL_SENT = 666;
    private static final String VIDEO_CODEC_VP9 = "VP9";
    private static final String AUDIO_CODEC_OPUS = "opus";
    // Local preview screen position before call is connected.
    private static final int LOCAL_X_CONNECTING = 0;
    private static final int LOCAL_Y_CONNECTING = 0;
    private static final int LOCAL_WIDTH_CONNECTING = 100;
    private static final int LOCAL_HEIGHT_CONNECTING = 100;
    // Local preview screen position after call is connected.
    private static final int LOCAL_X_CONNECTED = 72;
    private static final int LOCAL_Y_CONNECTED = 72;
    private static final int LOCAL_WIDTH_CONNECTED = 25;
    private static final int LOCAL_HEIGHT_CONNECTED = 25;
    // Remote video screen position
    private static final int REMOTE_X = 0;
    private static final int REMOTE_Y = 0;
    private static final int REMOTE_WIDTH = 100;
    private static final int REMOTE_HEIGHT = 100;
    private VideoRendererGui.ScalingType scalingType = VideoRendererGui.ScalingType.SCALE_ASPECT_FILL;
    private VideoRenderer.Callbacks localRender;
    private VideoRenderer.Callbacks remoteRender;
    private WebRtcClient client;
    private String mSocketAddress;
    private static final String[] RequiredPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

    private WeakReference<Activity> mAct;
    private GLSurfaceView vsv;
    private boolean isCalled;

    public VideoCallManager(Activity activity, GLSurfaceView vsv, boolean isCalled) {
        mAct = new WeakReference<>(activity);
        this.vsv = vsv;
        this.isCalled = isCalled;
        mSocketAddress = "http://120.79.16.93:4000/";
        vsv.setPreserveEGLContextOnPause(true);
        vsv.setKeepScreenOn(true);
        VideoRendererGui.setView(vsv, () -> init());
        // local and remote render
        remoteRender = VideoRendererGui.create(
                REMOTE_X, REMOTE_Y,
                REMOTE_WIDTH, REMOTE_HEIGHT, scalingType, false);
        localRender = VideoRendererGui.create(
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING, scalingType, true);
    }

    public void init() {

        Point displaySize = new Point();
        mAct.get().getWindowManager().getDefaultDisplay().getSize(displaySize);
        PeerConnectionParameters params = new PeerConnectionParameters(
                true, false, displaySize.x, displaySize.y, 30, 1, VIDEO_CODEC_VP9, true, 1, AUDIO_CODEC_OPUS, true);
        client = new WebRtcClient(this, mSocketAddress, params, VideoRendererGui.getEGLContext());
    }


    public void onPause() {
        if (vsv != null) {
            vsv.onPause();
        }
        if (client != null) {
            client.onPause();
        }
    }

    public void onResume() {
        if (vsv != null) {
            vsv.onResume();
        }
        if (client != null) {
            client.onResume();
        }
    }

    public void onDestroy() {
        if (client != null) {
            client.onDestroy();
        }
        if (mAct != null) {
            mAct.clear();
            mAct = null;
        }
    }


    @Override
    public void onCallReady(String callId) {
        //被叫
        LogUtil.d("callId:"+callId);
        if (!isCalled) {
            try {
                answer(callId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
//            startCam();
        }
    }

    public void answer(String callerId) throws JSONException {
        client.sendMessage(callerId, "init", null);
        startCam();
    }

    public void startCam() {
        // Camera settings
        if (PermissionChecker.hasPermissions(mAct.get().getApplicationContext(), RequiredPermissions)) {
            client.start("android_test");
//            client.start("SessionId_" + TcpManager.get().getSessionId());
        }
    }


    @Override
    public void onStatusChanged(String newStatus) {
        mAct.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mAct.get(), newStatus, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onLocalStream(MediaStream localStream) {
        localStream.videoTracks.get(0).addRenderer(new VideoRenderer(localRender));
        VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING,
                scalingType, false);
    }

    @Override
    public void onAddRemoteStream(MediaStream remoteStream, int endPoint) {
        remoteStream.videoTracks.get(0).addRenderer(new VideoRenderer(remoteRender));
        VideoRendererGui.update(remoteRender,
                REMOTE_X, REMOTE_Y,
                REMOTE_WIDTH, REMOTE_HEIGHT, scalingType, false);
        VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTED, LOCAL_Y_CONNECTED,
                LOCAL_WIDTH_CONNECTED, LOCAL_HEIGHT_CONNECTED,
                scalingType, false);
    }

    @Override
    public void onRemoveRemoteStream(int endPoint) {
        VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING,
                scalingType, false);
    }
}
