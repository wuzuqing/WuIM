package com.wuzuqing.component_base.base.mvc.inter

/**
 * @Created by TOME .
 * @时间 2018/5/2 17:58
 * @描述 ${v接口}
 */

interface BaseView : ILoadingDialogView {

    //void onSuccess();

    fun showError(msg: String, code: String)

    // void showErrorMsg(String errorMsg);


}
