package wuzuqing.com.module_im.activity

import android.content.Intent
import android.support.v4.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.wuzuqing.component_base.base.adapter.BaseFragmentAdapter
import com.wuzuqing.component_base.base.mvc.BaseVcActivity
import com.wuzuqing.component_base.util.ServiceUtils
import com.wuzuqing.component_data.d_arouter.RouterURLS
import kotlinx.android.synthetic.main.im_activity_main.*
import wuzuqing.com.module_im.R
import wuzuqing.com.module_im.fragment.ContactsFragment
import wuzuqing.com.module_im.fragment.ConversationFragment
import wuzuqing.com.module_im.fragment.MyFragment
import wuzuqing.com.module_im.service.TcpService
import wuzuqing.com.module_im.util.NiceImageViewUtil
import wuzuqing.com.module_im.util.UnReadCountManager
import wuzuqing.com.module_im.widget.voiceview.VoicePlayClickListener


@Route(path = RouterURLS.IM_MAIN)
class MainActivity : BaseVcActivity() {

    override fun getLayout(): Int {
        return R.layout.im_activity_main
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun initView() {
        val size = resources.getDimensionPixelSize(R.dimen.im_chat_img_size)
        NiceImageViewUtil.getRectCache().init(size, size, 10)
        var fragments = ArrayList<Fragment>()
        fragments.add(ConversationFragment())
        fragments.add(ContactsFragment())
        fragments.add(MyFragment())
        im_act_main_vp.adapter = BaseFragmentAdapter(supportFragmentManager, fragments)
        im_act_main_vp.offscreenPageLimit = fragments.size
        im_act_main_alphaIndicator.setViewPager(im_act_main_vp)

        //初始数据库
        UnReadCountManager.getInstance().registerUnReadCount(im_act_main_alphaIndicator)

        requestPermission(PERMISSION_TAKE_VIDEO,100)
    }

    override fun onDestroy() {
        super.onDestroy()
        UnReadCountManager.getInstance().registerUnReadCount(null)
        ServiceUtils.stopService(TcpService::class.java)
        VoicePlayClickListener.get().onDestory()
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }


}