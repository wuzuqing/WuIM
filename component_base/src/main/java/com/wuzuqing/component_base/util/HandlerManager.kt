package com.wuzuqing.component_base.util

import android.os.Handler
import android.os.Looper

object HandlerManager {
    private val handler = Handler(Looper.getMainLooper())

    fun async(runnable: Runnable) {
        handler.post(runnable)
    }


    fun async(runnable: Runnable, delayMillis: Long) {
        handler.postDelayed(runnable, delayMillis)
    }

    fun runMain(runnable: Runnable){
        handler.post(runnable)
    }
}