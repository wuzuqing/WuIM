package com.wuzuqing.component_data.d_arouter

import com.alibaba.android.arouter.launcher.ARouter

object RouterCenter {
    fun toMain() {
        navigation(RouterURLS.IM_MAIN)
    }

    fun navigation(path: String) {
        ARouter.getInstance().build(path).navigation()
    }

}