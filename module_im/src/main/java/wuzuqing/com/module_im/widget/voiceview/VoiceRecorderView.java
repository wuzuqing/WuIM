package wuzuqing.com.module_im.widget.voiceview;

/**
 * Copyright (C) 2017 ilikeshatang. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wuzuqing.component_base.util.FileUtils;

import wuzuqing.com.module_im.R;


/**
 * desc:   Voice recorder view
 * author: wangshanhai
 * email: ilikeshatang@gmail.com
 * date: 2017/10/31 15:27
 */
public class VoiceRecorderView extends RelativeLayout {
    protected Context context;
    protected Drawable[] micImages;
    protected boolean isImagesCustom = false;
    protected VoiceRecorder voiceRecorder;

    protected PowerManager.WakeLock wakeLock;
    protected ImageView micImage;
    protected TextView recordingHint;

    protected String release_to_cancel = "";
    protected String move_up_to_cancel = "";
    protected String default_value = "按住 说话";

    protected Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // change image
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };

    public VoiceRecorderView(Context context) {
        super(context);
        init(context);
    }

    public VoiceRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoiceRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.im_ease_widget_voice_recorder, this);

        micImage = findViewById(R.id.mic_image);
        recordingHint = findViewById(R.id.recording_hint);

        voiceRecorder = new VoiceRecorder(micImageHandler);

        // animation resources, used for recording
        micImages = new Drawable[]{
                getResources().getDrawable(R.drawable.im_ease_record_animate_01),
                getResources().getDrawable(R.drawable.im_ease_record_animate_02),
                getResources().getDrawable(R.drawable.im_ease_record_animate_03),
                getResources().getDrawable(R.drawable.im_ease_record_animate_04),
                getResources().getDrawable(R.drawable.im_ease_record_animate_05),
                getResources().getDrawable(R.drawable.im_ease_record_animate_06),
                getResources().getDrawable(R.drawable.im_ease_record_animate_07),
                getResources().getDrawable(R.drawable.im_ease_record_animate_08),
                getResources().getDrawable(R.drawable.im_ease_record_animate_09),
                getResources().getDrawable(R.drawable.im_ease_record_animate_10),
                getResources().getDrawable(R.drawable.im_ease_record_animate_11),
                getResources().getDrawable(R.drawable.im_ease_record_animate_12),
                getResources().getDrawable(R.drawable.im_ease_record_animate_13),
                getResources().getDrawable(R.drawable.im_ease_record_animate_14),};

        wakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "voice");

        //默认提示语
        release_to_cancel = context.getString(R.string.im_release_to_cancel);
        move_up_to_cancel = context.getString(R.string.im_move_up_to_cancel);

    }


   private boolean inRangeOfView;

    /**
     * on speak button touched
     *
     * @param v
     * @param event
     */
    public boolean onPressToSpeakBtnTouch(TextView v, MotionEvent event, EaseVoiceRecorderCallback recorderCallback) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    if (VoicePlayClickListener.get().isPlaying)
                        VoicePlayClickListener.get().stopPlayVoice();
                    v.setPressed(true);
                    v.setText("松开 结束");
                    startRecording();
                } catch (Exception e) {
                    v.setPressed(false);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!inRangeOfView(v, event) && !inRangeOfView) { //
                    inRangeOfView = true;
                    showReleaseToCancelHint();
                } else if (inRangeOfView(v, event) && inRangeOfView) {
                    inRangeOfView = false;
                    showMoveUpToCancelHint();
                }
                return true;
            case MotionEvent.ACTION_UP:
                v.setPressed(false);
                v.setText(default_value);
                if (!inRangeOfView(v, event)) {
                    // discard the recorded audio.
                    discardRecording();
                } else {
                    // stop recording and send voice file
                    try {
                        int length = stopRecoding();
                        if (length > 0) {
                            if (recorderCallback != null) {
                                recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                            }
                        } else if (length == EMError.FILE_INVALID) {
                            Toast.makeText(context, R.string.im_Recording_without_permission, Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(context, R.string.im_The_recording_time_is_too_short, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Toast.makeText(context, R.string.send_failure_please, Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            default:
                discardRecording();
                return false;
        }
    }

    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }

    public interface EaseVoiceRecorderCallback {
        /**
         * on voice record complete
         *
         * @param voiceFilePath   录音完毕后的文件路径
         * @param voiceTimeLength 录音时长
         */
        void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);
    }

    public void startRecording() {
        if (!FileUtils.isExistSDCard()) {
            Toast.makeText(context, R.string.im_Send_voice_need_sdcard_support, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            wakeLock.acquire();
            this.setVisibility(View.VISIBLE);
            recordingHint.setText(context.getString(R.string.im_move_up_to_cancel));
            voiceRecorder.startRecording(context);
        } catch (Exception e) {
            e.printStackTrace();
            if (wakeLock.isHeld())
                wakeLock.release();
            if (voiceRecorder != null)
                voiceRecorder.discardRecording();
            this.setVisibility(View.INVISIBLE);
            Toast.makeText(context, R.string.im_recoding_fail, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void showReleaseToCancelHint() {
        recordingHint.setText(release_to_cancel);
        recordingHint.setSelected(true);
    }

    public void showMoveUpToCancelHint() {
        recordingHint.setText(move_up_to_cancel);
        recordingHint.setSelected(false);
    }

    public void discardRecording() {
        if (wakeLock.isHeld())
            wakeLock.release();
        try {
            // stop recording
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                this.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    public int stopRecoding() {
        this.setVisibility(View.INVISIBLE);
        if (wakeLock.isHeld())
            wakeLock.release();
        return voiceRecorder.stopRecoding();
    }

    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }

    public String getVoiceFileName() {
        return voiceRecorder.getVoiceFileName();
    }

    public boolean isRecording() {
        return voiceRecorder.isRecording();
    }

    /**
     * 自定义语音命名
     *
     * @param isTrue true为自定义，false为默认命名（时间戳）
     * @param name
     */
    public void setCustomNamingFile(boolean isTrue, String name) {
        voiceRecorder.isCustomNamingFile(isTrue, name);
    }

    /**
     * 目前需要传入15张帧动画png
     *
     * @param animationDrawable
     */
    public void setDrawableAnimation(Drawable[] animationDrawable) {
        micImages = null;
        this.micImages = animationDrawable;
    }


    /**
     * 设置按下显示的提示
     *
     * @param releaseToCancelHint
     */
    public void setShowReleaseToCancelHint(String releaseToCancelHint) {
        this.release_to_cancel = releaseToCancelHint;
    }

    /**
     * 设置手指向上移动显示的提示语
     *
     * @param moveUpToCancelHint
     */
    public void setShowMoveUpToCancelHint(String moveUpToCancelHint) {
        this.move_up_to_cancel = moveUpToCancelHint;
    }

}
