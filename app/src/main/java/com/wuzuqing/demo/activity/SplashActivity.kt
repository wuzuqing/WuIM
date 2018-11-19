package com.wuzuqing.demo.activity

import android.os.Bundle
import com.wuzuqing.component_base.util.HandlerManager
import com.wuzuqing.component_data.d_arouter.RouterCenter
import com.wuzuqing.component_data.d_arouter.RouterURLS
import com.wuzuqing.demo.R
import com.zhy.autolayout.AutoLayoutActivity


class SplashActivity : AutoLayoutActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_splash)
        initView()
    }

    private fun initView() {
        HandlerManager.async(Runnable {
            RouterCenter.navigation(RouterURLS.USER_CENTER_LOGIN)
            finish()
        }, 2000)

    }
}
