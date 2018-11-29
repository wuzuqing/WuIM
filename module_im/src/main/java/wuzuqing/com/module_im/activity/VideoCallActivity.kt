//package wuzuqing.com.module_im.activity
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.WindowManager
//import com.alibaba.android.arouter.facade.annotation.Route
//import com.wuzuqing.component_base.base.mvc.BaseVcActivity
//import com.wuzuqing.component_data.d_arouter.RouterURLS
//import kotlinx.android.synthetic.main.im_activity_video_call.*
//import wuzuqing.com.module_im.R
//import wuzuqing.com.module_im.util.VideoCallManager
//
//
//@Route(path = RouterURLS.IM_VIDEO_CALL)
//class VideoCallActivity : BaseVcActivity() {
//
//    lateinit var videoCallManager: VideoCallManager
//    var isCalled = true
//    override fun onCreate(savedInstanceState: Bundle?) {
//        window.addFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//                        or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                        or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//                        or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun getLayout(): Int {
//        return R.layout.im_activity_video_call
//    }
//
//    override fun initExtra(extra: Intent) {
//        this.isCalled = extra.getBooleanExtra("called", false)
//    }
//
//
//    override fun initView() {
//        videoCallManager = VideoCallManager(this, glview_call,isCalled)
//
//    }
//
//    override fun onPause() {
//        super.onPause()
//        videoCallManager.onPause()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        videoCallManager.onResume()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        videoCallManager.onDestroy()
//    }
//}