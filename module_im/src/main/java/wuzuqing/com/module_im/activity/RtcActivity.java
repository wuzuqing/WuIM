package wuzuqing.com.module_im.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wuzuqing.component_base.rxbus.RxManager;
import com.wuzuqing.component_base.util.LogUtils;
import com.wuzuqing.component_data.cache.GlobalVariable;
import com.wuzuqing.component_data.d_arouter.RouterURLS;
import com.wuzuqing.component_im.common.utils.TcpManager;
import com.wuzuqing.component_im.common.utils.VideoCallManager;
import com.yanzhenjie.album.mvp.BaseActivity;

import org.json.JSONException;
import org.webrtc.MediaStream;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;

import cn.wuzuqing.component_web_client.PeerConnectionParameters;
import cn.wuzuqing.component_web_client.WebRtcClient;
import io.reactivex.functions.Consumer;
import wuzuqing.com.module_im.R;
import wuzuqing.com.module_im.util.PermissionChecker;


@Route(path = RouterURLS.IM_VIDEO_CALL)
public class RtcActivity extends BaseActivity implements WebRtcClient.RtcListener {
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
    private GLSurfaceView vsv;
    private Button btnStartCall;
    private Button btnEndCall;
    private boolean isCalled = true;
    private static final String[] RequiredPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    protected PermissionChecker permissionChecker = new PermissionChecker();
    private RxManager rxManager = new RxManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                LayoutParams.FLAG_FULLSCREEN
                        | LayoutParams.FLAG_KEEP_SCREEN_ON
                        | LayoutParams.FLAG_DISMISS_KEYGUARD
                        | LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.im_activity_rtc);
        final Intent intent = getIntent();
        mSocketAddress = "http://120.79.16.93:4000/";
        this.isCalled = intent.getBooleanExtra("called", false);
        vsv = findViewById(R.id.glview_call);
        btnEndCall = findViewById(R.id.im_btn_end_call);
        btnStartCall = findViewById(R.id.im_btn_start_call);
        vsv.setPreserveEGLContextOnPause(true);
        vsv.setKeepScreenOn(true);
        VideoRendererGui.setView(vsv, new Runnable() {
            @Override
            public void run() {
                init();
            }
        });

        // local and remote render
        remoteRender = VideoRendererGui.create(
                REMOTE_X, REMOTE_Y,
                REMOTE_WIDTH, REMOTE_HEIGHT, scalingType, false);
        localRender = VideoRendererGui.create(
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING, scalingType, true);

        btnEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endCall(true);
            }
        });
        if (!isCalled) {
            targetCallId = intent.getStringExtra("callId");
            btnStartCall.setVisibility(View.VISIBLE);
            btnStartCall.setOnClickListener(v -> {
                try {
                    v.setVisibility(View.GONE);
                    answer(targetCallId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

        }
        checkPermissions();
        rxManager.on("endCall", (Consumer<Boolean>) aBoolean -> finish());
        rxManager.on("answer", (Consumer<Boolean>) aBoolean -> {
            start();
            vsv.removeCallbacks(endCall);
        });
    }

    private void checkPermissions() {
        permissionChecker.verifyPermissions(this, RequiredPermissions, new PermissionChecker.VerifyPermissionsCallback() {

            @Override
            public void onPermissionAllGranted() {

            }

            @Override
            public void onPermissionDeny(String[] permissions) {
                Toast.makeText(RtcActivity.this, "Please grant required permissions.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        PeerConnectionParameters params = new PeerConnectionParameters(
                true, false, displaySize.x, displaySize.y, 30, 1, VIDEO_CODEC_VP9, true, 1, AUDIO_CODEC_OPUS, true);

        client = new WebRtcClient(this, mSocketAddress, params, VideoRendererGui.getEGLContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        vsv.onPause();
        if (client != null) {
            client.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        vsv.onResume();
        if (client != null) {
            client.onResume();
        }
    }

    @Override
    public void onDestroy() {
        vsv.removeCallbacks(endCall);
        if (client != null) {
            client.onDestroy();
        }
        super.onDestroy();
    }

    private String targetCallId;

    @Override
    public void onCallReady(String callId) {
        if (isCalled) {
            //创建一条聊天信息
            TcpManager.get().sendMsg(GlobalVariable.Companion.get().getTargetId(), null, 2, 6, "", callId, "0", 0);
            startCam();
        }
    }

    private boolean calling = false;
    private long startTime;

    public void answer(String callerId) throws JSONException {
        VideoCallManager.get().sendNewStatus(false,"1", 0);
        client.sendMessage(callerId, "init", null);
        start();
        startCam();
    }

    private void start() {
        startTime = System.currentTimeMillis();
        calling = true;
    }


    public void startCam() {
        if (PermissionChecker.hasPermissions(this, RequiredPermissions)) {
            client.start("android_test");
            if (isCalled) {
                vsv.postDelayed(endCall, 10000);
            }
        } else {
            finish();
        }
    }

    private Runnable endCall = () ->
    {
        VideoCallManager.get().sendNewStatus(true,"-1", 0);
        finish();
    };

    private void endCall(boolean isCancel) {
        LogUtils.d("calling:" + calling + " isCancel:" + isCancel + " isCalled:" + isCalled);
        if (isCalled) { // 发起者
            if (calling) {
                end();
            } else if (isCancel) {
                VideoCallManager.get().sendNewStatus(true,"-2", 0);
            }
        } else {
            if (calling) {
                end();
            } else if (isCancel) {
                VideoCallManager.get().sendNewStatus(false,"-3", 0);
            }
        }
        finish();
    }

    private void end() {
        int duration = (int) ((System.currentTimeMillis() - startTime) / 1000);
        VideoCallManager.get().sendNewStatus(isCalled,"2", duration);
    }


    @Override
    public void onStatusChanged(final String newStatus) {

    }

    @Override
    public void onLocalStream(MediaStream localStream) {
        LogUtils.d("onLocalStream");
        localStream.videoTracks.get(0).addRenderer(new VideoRenderer(localRender));
        VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING,
                scalingType, false);
    }

    @Override
    public void onAddRemoteStream(MediaStream remoteStream, int endPoint) {
        LogUtils.d("onAddRemoteStream");
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
        LogUtils.d("onRemoveRemoteStream");
        VideoRendererGui.update(localRender,
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING,
                scalingType, false);
    }

}