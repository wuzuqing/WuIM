package wuzuqing.com.module_im.fragment

import com.wuzuqing.component_base.base.mvc.BaseVcFragment
import com.wuzuqing.component_base.constants.BaseApplication
import com.wuzuqing.component_base.util.ImageLoadUtils
import com.wuzuqing.component_data.cache.GlobalVariable
import com.wuzuqing.component_data.d_arouter.RouterCenter
import com.wuzuqing.component_data.d_arouter.RouterURLS
import kotlinx.android.synthetic.main.im_fragment_me.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import wuzuqing.com.module_im.R

class MyFragment : BaseVcFragment() {
    override fun getLayout(): Int {
        return R.layout.im_fragment_me
    }

    override fun initTitle() {

    }

    override fun initView() {
        if (GlobalVariable.get().user!=null){
            im_fm_me_tv_nick.text = GlobalVariable.get().user!!.nick
            ImageLoadUtils.displayAvatar(im_fm_me_iv_avatar, GlobalVariable.get().user!!.avatar)
        }
        im_fm_me_btn_logout.onClick {
            BaseApplication.getAppContext().activityControl.finishiAll()
            RouterCenter.navigation(RouterURLS.USER_CENTER_LOGIN)
        }
    }


}
