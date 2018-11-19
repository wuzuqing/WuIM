package wuzuqing.com.module_im.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wuzuqing.component_base.rxbus.RxManagerUtil;
import com.wuzuqing.component_base.util.FileUtils;
import com.wuzuqing.component_im.common.packets.ChatBody;

import java.util.Timer;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple;
import wuzuqing.com.module_im.R;
import wuzuqing.com.module_im.util.DownloadUtil;

public class PreviewVideoView extends JCVideoPlayer {

    private ImageView ivCover;
    private ImageView ivPlayState;
    private ProgressBar loadingView;
    private ChatBody model;

    public PreviewVideoView(Context context) {
        super(context);
    }

    public PreviewVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        loadingView = findViewById(R.id.loading);
        ivCover = findViewById(R.id.im_cover);
        ivPlayState = findViewById(R.id.iv_play_state);
        ivPlayState.setOnClickListener(this);
        fullscreenButton.setVisibility(View.GONE);
        bottomContainer.setVisibility(GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.im_preview_video;
    }


    private void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            ivPlayState.setImageResource(R.drawable.jc_click_pause_selector);
        } else if (currentState == CURRENT_STATE_ERROR) {
            ivPlayState.setImageResource(R.drawable.jc_click_error_selector);
        } else {
            ivPlayState.setImageResource(R.drawable.jc_click_play_selector);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start || v.getId() == R.id.iv_play_state) {
            if (!FileUtils.isFileExists(model.getLocalPath())) {
                //下载
                loadingView.setVisibility(VISIBLE);
                DownloadUtil.download(model.getUrl(), model.getLocalPath(), result -> {
                    loadingView.setVisibility(GONE);
                    clickPlayBtn();
                });
            } else {
                clickPlayBtn();
            }
            return;
        } else if (v.getId() == R.id.surface_container) {
            RxManagerUtil.getInstance().post(CLICK_SCREEN, true);
            return;
        }
        super.onClick(v);
    }

    public void clickPlayBtn() {
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(getContext(), getResources().getString(fm.jiecao.jcvideoplayer_lib.R.string.no_url), Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentState == CURRENT_STATE_NORMAL || currentState == CURRENT_STATE_ERROR) {
            if (!url.startsWith("file") && !url.startsWith("/") &&
                    !JCUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                showWifiDialog(JCUserActionStandard.ON_CLICK_START_ICON);
                return;
            }
            startVideo();
            onEvent(currentState != CURRENT_STATE_ERROR ? JCUserAction.ON_CLICK_START_ICON : JCUserAction.ON_CLICK_START_ERROR);
        } else if (currentState == CURRENT_STATE_PLAYING) {
            onEvent(JCUserAction.ON_CLICK_PAUSE);
            JCMediaManager.instance().mediaPlayer.pause();
            onStatePause();
        } else if (currentState == CURRENT_STATE_PAUSE) {
            onEvent(JCUserAction.ON_CLICK_RESUME);
            JCMediaManager.instance().mediaPlayer.start();
            onStatePlaying();
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            startVideo();
            onEvent(JCUserAction.ON_CLICK_START_AUTO_COMPLETE);
        }
        updateStartImage();
    }


    @Override
    public void addTextureView() {
        super.addTextureView();
        setUIState(GONE);
    }

    @Override
    public void removeTextureView() {
        super.removeTextureView();
        setUIState(VISIBLE);
    }

    @Override
    public void onStatePause() {
        Log.i(TAG, "onStatePause " + " [" + this.hashCode() + "] ");
        currentState = CURRENT_STATE_PAUSE;
        cancelProgressTimer();
    }

    public void startProgressTimer() {
        cancelProgressTimer();
        UPDATE_PROGRESS_TIMER = new Timer();
        mProgressTimerTask = new ProgressTimerTask();
        UPDATE_PROGRESS_TIMER.schedule(mProgressTimerTask, 0, 960);
    }

    @Override
    public void onAutoCompletion() {
        setUIState(VISIBLE);
        updateProgressBean.setProgress(0);
        RxManagerUtil.getInstance().post(UPDATE_PROGRESS_BEAN, updateProgressBean);
        super.onAutoCompletion();
    }

    @Override
    public void onCompletion() {
        setUIState(VISIBLE);
        super.onCompletion();
    }

    public void setUIState(int state) {
        ivCover.setVisibility(state);
        startButton.setVisibility(state);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (touchListener != null) touchListener.onTouch(v, event);
        return super.onTouch(v, event);
    }

    private OnTouchListener touchListener;

    public void setTouchListener(OnTouchListener touchListener) {
        this.touchListener = touchListener;
    }

    private UpdateProgressBean updateProgressBean;

    @Override
    public void setProgressAndText(int progress, int position, int duration) {
        try {
            if (updateProgressBean == null) {
                updateProgressBean = new UpdateProgressBean(position, duration);
            } else {
                updateProgressBean.setParams(position, duration);
            }
            RxManagerUtil.getInstance().post(UPDATE_PROGRESS_BEAN, updateProgressBean);
//            if (progress != 0) {
//                progressBar.setProgress(progress);
//                currentTimeTextView.setText(JCUtils.stringForTime(position));
//            }
//            totalTimeTextView.setText(JCUtils.stringForTime(duration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String UPDATE_PROGRESS_BEAN = "UPDATE_PROGRESS_BEAN";
    public static final String CLICK_SCREEN = "CLICK_SCREEN";


    public ImageView getIvCover() {
        return ivCover;
    }

    public ChatBody getModel() {
        return model;
    }

    public void setModel(ChatBody model) {
        this.model = model;
        setUp(model.getLocalPath(), JCVideoPlayerSimple.SCREEN_LAYOUT_NORMAL);
    }

    public static class UpdateProgressBean {
        private int progress;
        private int max;


        public UpdateProgressBean() {
        }

        public UpdateProgressBean(int progress, int max) {
            this.progress = progress;
            this.max = max;
        }

        public void setParams(int progress, int max) {
            this.max = max;
            this.progress = progress;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        @Override
        public String toString() {
            return "UpdateProgressBean{" +
                    "progress=" + progress +
                    ", max=" + max +
                    '}';
        }
    }
}
