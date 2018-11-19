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
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.ImageView;

import com.wuzuqing.component_base.constants.BaseApplication;

import wuzuqing.com.module_im.R;

/**
 * desc:   voice play click listener
 * author: wangshanhai
 * email: ilikeshatang@gmail.com
 * date: 2017/10/31 15:29
 */
public class VoicePlayClickListener {
    private static final String TAG = "VoicePlayClickListener";

    private static VoicePlayClickListener voicePlayClickListener;


    public static VoicePlayClickListener get() {
        if (voicePlayClickListener == null) {
            synchronized (TAG) {
                if (voicePlayClickListener == null) {
                    voicePlayClickListener = new VoicePlayClickListener();
                }
            }
        }
        return voicePlayClickListener;
    }

    private ImageView voiceIconView;
    private boolean isSettingPlayingIcon = false;
    private int fromStopPlayingDrawableId = R.drawable.im_ease_chatto_voice_playing_f;
    private int toStopPlayingDrawableId = R.drawable.im_ease_chatto_voice_playing_t;
    private int fromDrawableResourceId = R.drawable.im_voice_from_icon;
    private int toDrawableResourceId = R.drawable.im_voice_to_icon;

    private AnimationDrawable voiceAnimation = null;
    private MediaPlayer mediaPlayer = null;
    private Context context;

    public boolean isPlaying = false;
    public VoicePlayClickListener currentPlayListener = null;
    public String playMsgId = "";
    public String getLocalUrl = "";
    AudioManager audioManager;

    private VoicePlayClickListener() {
        this.context = BaseApplication.getAppContext().getApplicationContext();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * stop play voice
     */
    public void stopPlayVoice() {
        if (voiceIconView != null) {
            voiceAnimation.stop();
            if (fromStopPlayingDrawableId != 0) {
                voiceIconView.setImageResource(isFrom ? fromStopPlayingDrawableId : toStopPlayingDrawableId);
            } else {
                voiceIconView.setImageResource(R.drawable.im_ease_chatto_voice_playing_f);
            }
        }
        // stop play voice
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        isPlaying = false;
        playMsgId = null;
    }


    public void onDestory() {
        stopPlayVoice();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private boolean isFrom;

    /**
     * play voice for path
     *
     * @param filePath
     */
    public void playVoice(ImageView iv, String filePath, boolean isFrom) {
        doPrePlay(iv, filePath);
        this.isFrom = isFrom;
        playNew(filePath);
    }

    private void doPrePlay(ImageView iv, String filePath) {
        this.getLocalUrl = filePath;
        if (isPlaying) {
            if (playMsgId != null) {
                currentPlayListener.stopPlayVoice();
            }
        }
        voiceIconView = iv;
    }

    private void playNew(String filePath) {
        playMsgId = filePath;
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(mp ->
                    {
                        mp.start();
                        isPlaying = true;
                        showAnimation();
                    }
            );
            mediaPlayer.setOnCompletionListener(mp -> {
                // TODO Auto-generated method stub
                stopPlayVoice(); // stop animation
            });
        }

        if (VoiceProvider.getInstance().getSettingsProvider().isSpeakerOpened()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepareAsync();
            currentPlayListener = this;

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }


    // show the voice playing animation
    private void showAnimation() {
        if (voiceIconView == null) return;
        // play voice, and start animation
        /**
         * 使用外部设置进来的播放语音的动画
         */
        if (!isSettingPlayingIcon) {
            voiceIconView.setImageResource(isFrom ? fromDrawableResourceId : toDrawableResourceId);
        }
        voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
        voiceAnimation.start();
    }

    /**
     * 设置播放语音的动画
     *
     * @param drawableResoureId
     */
    public void setPlayingIconDrawableResoure(int drawableResoureId) {
        isSettingPlayingIcon = true;
        this.fromDrawableResourceId = drawableResoureId;
    }

    /**
     * 设置停止播放语音时，显示的静态icon
     *
     * @param drawableResoureId
     */
    public void setStopPlayIcon(int drawableResoureId) {
        this.fromStopPlayingDrawableId = drawableResoureId;
    }


}
