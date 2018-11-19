/*
 * Copyright (c) 18-2-1 下午1:58. XQ Yang
 */

package com.wuzuqing.component_base.base.mvc.inter

/**
 * @author XQ Yang
 * @date 2017/11/15  11:40
 */
interface ILoadingDialogView {

    /**
     * 显示Dialog
     */
    fun showHUD(msg: String)

    /**
     * 关闭Dialog
     */
    fun dismissHUD()


}
