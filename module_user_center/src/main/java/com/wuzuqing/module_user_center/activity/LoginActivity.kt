package com.wuzuqing.module_user_center.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.wuzuqing.component_base.base.mvc.BaseVcActivity
import com.wuzuqing.component_base.net.ModelService
import com.wuzuqing.component_base.net.common_callback.INetCallback
import com.wuzuqing.component_base.util.SPUtils
import com.wuzuqing.component_base.util.ServiceUtils
import com.wuzuqing.component_data.bean.UserInfoBean
import com.wuzuqing.component_data.cache.GlobalVariable
import com.wuzuqing.component_data.d_arouter.RouterCenter
import com.wuzuqing.component_data.d_arouter.RouterURLS
import com.wuzuqing.module_user_center.R
import com.wuzuqing.module_user_center.api.ApiService
import kotlinx.android.synthetic.main.uc_act_login.*
import org.jetbrains.anko.sdk25.coroutines.onClick


@Route(path = RouterURLS.USER_CENTER_LOGIN)
class LoginActivity : BaseVcActivity() {
    override fun getLayout(): Int {
        return R.layout.uc_act_login
    }

    override fun initView() {
        uc_act_tv_register.onClick {

        }
        uc_act_tv_change_pwd.onClick {

        }
        uc_act_btn_login.onClick {
            val phone = uc_act_et_phone.text.toString()
            val pwd = uc_act_et_pwd.text.toString()
            uc_act_btn_login.isEnabled = false

            ModelService.getRemoteData(true, mView, ApiService::class.java,
                    ModelService.MethodSelect<UserInfoBean, ApiService>
                    { service -> service!!.login(phone, pwd) }, INetCallback<UserInfoBean> {
                GlobalVariable.get().user = it
                SPUtils.setInt("userId",it.id)
                GlobalVariable.get().user!!.password = pwd
                ServiceUtils.startService("wuzuqing.com.module_im.service.TcpService")
                overridePendingTransition(0,0)
                RouterCenter.toMain()
                finish()
            })
        }
    }
}
