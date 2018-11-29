package wuzuqing.com.module_im.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.wuzuqing.component_base.util.KeyboardUtils
import com.wuzuqing.component_base.util.LogUtils
import com.wuzuqing.component_base.util.SPUtils
import com.wuzuqing.component_im.common.utils.TcpManager
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onTouch
import wuzuqing.com.module_im.R
import wuzuqing.com.module_im.fragment.ChatFaceFragment
import wuzuqing.com.module_im.fragment.ChatSelectFragment
import wuzuqing.com.module_im.widget.voiceview.VoiceRecorderView


/**
 *   activity 当前界面
 *   bottomContainer: 功能框容器
 *   inputContainer : 文本框容器
 *   etContent:       文本编辑框
 *   ivFace:          表情按钮
 *   ivRecorder       语音按钮
 */
class ChatPanelHelper(private val activity: FragmentActivity, private val bottomContainer: View, private val inputContainer: View,
                      private val etContent: EditText, private val ivFace: ImageView, private val ivRecorder: ImageView) {
    private var hasContent = false
    private var inputState = InputPanelState.NORMAL

    private var keyBoardHeight = 0

    private var selectFragment: ChatSelectFragment? = null
    private var faceFragment: ChatFaceFragment? = null

    enum class InputPanelState {
        NORMAL, SELECT, FACE, INPUT, RECORD
    }

    /**
     *  btnRecorder:语音录制按钮
     *  vr : 语音录制提示框
     *  ivSelect: 功能选择按钮
     *  btnSend : 文本发送按钮
     *  isPrivate : 是否是单聊
     */
    fun init(btnRecorder: Button, vr: VoiceRecorderView, ivSelect: ImageView, btnSend: View, isPrivate: Boolean) {
        keyBoardHeight = SPUtils.getInt("keyBoardHeight", 775)
        //语音按钮监听
        ivRecorder.onClick {
            if (btnRecorder.isShown) {
                btnRecorder.visibility = View.GONE
                etContent.visibility = View.VISIBLE
                ivRecorder.setImageResource(R.mipmap.ic_cheat_voice)
                KeyboardUtils.showSoftInput(etContent)
            } else {
                ivRecorder.setImageResource(R.mipmap.ic_cheat_keyboard)
                ivFace.setImageResource(R.mipmap.ic_cheat_emo)

                btnRecorder.visibility = View.VISIBLE
                etContent.visibility = View.GONE
                bottomContainer.visibility = View.GONE
                if (inputState==InputPanelState.INPUT){
                    inputState = InputPanelState.RECORD
                    KeyboardUtils.hideSoftInput(etContent)
                }else{
                    inputState = InputPanelState.RECORD
                    setMargin(0)
                }
            }
        }
        // 文本编辑框焦点监听
        etContent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                setMargin(keyBoardHeight)
                //滑动数据
            }
        }
        //文本编辑框内容发生变化监听
        etContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.toString().isEmpty() && hasContent) {
                    hasContent = false
                    btnSend.visibility = View.GONE
                    ivSelect.visibility = View.VISIBLE
                } else if (!hasContent && p0!!.toString().isNotEmpty()) {
                    hasContent = true
                    ivSelect.visibility = View.GONE
                    btnSend.visibility = View.VISIBLE
                }
            }
        })

        //表情按钮监听
        ivFace.onClick {
            if (inputState == InputPanelState.FACE) {
                KeyboardUtils.showSoftInput(etContent)

                return@onClick
            }
            if (faceFragment == null) {
                faceFragment = ChatFaceFragment()
            }
            setMargin(keyBoardHeight)

            setPanelHeight()
            ivFace.setImageResource(R.mipmap.ic_cheat_keyboard)
            when (inputState) {
                InputPanelState.INPUT -> {
                    KeyboardUtils.hideSoftInput(etContent)
                    // 显示 表情fragment
                }
                InputPanelState.RECORD -> {
                    btnRecorder.visibility = View.GONE
                    etContent.visibility = View.VISIBLE
                    ivRecorder.setImageResource(R.mipmap.ic_cheat_voice)
                    //获取焦点
                }
            }
            etContent.requestFocus()
            bottomContainer.visibility = View.VISIBLE
            showFragment(faceFragment)
            inputState = InputPanelState.FACE
        }
        //功能选择监听
        ivSelect.onClick {
            if (inputState == InputPanelState.SELECT) {
                KeyboardUtils.showSoftInput(etContent)
                return@onClick
            }
            setMargin(keyBoardHeight)
            setPanelHeight()
            if (selectFragment == null) {
                selectFragment = ChatSelectFragment()
            }
            when (inputState) {
                InputPanelState.INPUT -> {
                    KeyboardUtils.hideSoftInput(etContent)
                }
                InputPanelState.RECORD -> {
                    bottomContainer.visibility = View.VISIBLE
                    btnRecorder.visibility = View.GONE
                    etContent.visibility = View.VISIBLE
                    ivRecorder.setImageResource(R.mipmap.ic_cheat_voice)
                }
                InputPanelState.FACE -> {
                    ivFace.setImageResource(R.mipmap.ic_cheat_emo)
                }
            }
            etContent.clearFocus()
            bottomContainer.visibility = View.VISIBLE
            showFragment(selectFragment)
            inputState = InputPanelState.SELECT


        }
        //发送文本
        btnSend.onClick {
            val content = etContent.text.toString()
            if (content.isEmpty()) {
                return@onClick
            }
            if (isPrivate) {
                TcpManager.get().sendPrivateMsg(0, content, null, null)
            } else {
                TcpManager.get().sendPublicMsg(0, content, null, null)
            }
            etContent.text.clear()
        }

        //录制语音
        btnRecorder.onTouch { v, event ->
            vr.onPressToSpeakBtnTouch(v as TextView, event) { voiceFilePath, voiceTimeLength ->
                // 为录音文件存放在sd的路径 voiceTimeLength 录音文件的时长
                TcpManager.get().sendVoice(isPrivate, voiceFilePath, voiceTimeLength)
            }
        }
        //输入法对话框监听
        KeyboardUtils.registerSoftInputChangedListener(activity, object : KeyboardUtils.OnSoftInputChangedListener {
            override fun keyBoardShow(height: Int) {
                keyBoardShow()
                SPUtils.setInt("keyBoardHeight", height)
            }

            override fun keyBoardHide(height: Int) {
                keyBoardHide()
            }
        })

    }

    fun  scroll(){

    }

    private fun keyBoardShow() {
        bottomContainer.visibility = View.GONE
        inputState = InputPanelState.INPUT
        ivFace.setImageResource(R.mipmap.ic_cheat_emo)
    }

    private fun keyBoardHide() {
        if (inputState == InputPanelState.INPUT) {
            inputState = InputPanelState.NORMAL
            etContent.clearFocus()
            setMargin(0)
        }else if (inputState == InputPanelState.RECORD){
            setMargin(0)
        }
    }

    //设置底部边距
    private fun setMargin(marginBottom: Int) {
        val params = inputContainer.layoutParams as ViewGroup.MarginLayoutParams
        if (params.bottomMargin != marginBottom) {
            params.bottomMargin = marginBottom
            inputContainer.layoutParams = params
        }
    }

    //显示fragment
    private fun showFragment(fragment: Fragment?) {
        if (fragment != null) {
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.im_act_chat_bottom_container, fragment, fragment.javaClass.simpleName).commit()
        }
    }

    fun clickList() {
        when (inputState) {
            InputPanelState.INPUT -> {
                etContent.clearFocus()
                KeyboardUtils.hideSoftInput(etContent)
            }
            InputPanelState.SELECT, InputPanelState.FACE -> {
                resetState()
            }
        }
    }



    //重置状态
    private fun resetState() {
        inputState = InputPanelState.NORMAL
        ivFace.setImageResource(R.mipmap.ic_cheat_emo)
        setMargin(0)
        bottomContainer.visibility = View.GONE
    }

    private var setPanelHeight = false
    //初始化面板的高度
    private fun setPanelHeight() {
        if (!setPanelHeight) {
            setPanelHeight = true
            bottomContainer.layoutParams.height = keyBoardHeight
            bottomContainer.requestLayout()
        }
    }

    fun canBackPressed(): Boolean {
        when (inputState) {
            InputPanelState.SELECT, InputPanelState.FACE -> {
                resetState()
                return false
            }
        }
        KeyboardUtils.unregisterSoftInputChangedListener(activity)
        return true
    }

}