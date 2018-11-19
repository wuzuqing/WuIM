package wuzuqing.com.module_im.listener

import android.content.Context
import android.support.v7.widget.LinearLayoutManager

class CustomLinearLayoutManager(context: Context, or: Int, res: Boolean) : LinearLayoutManager(context, or, res) {

    constructor(context: Context) : this(context, HORIZONTAL, false)

    private var isScrollVerEnabled = true
    private var isScrollHorEnabled = true

    fun setScrollVerEnabled(flag: Boolean) {
        this.isScrollVerEnabled = flag
    }

    fun setScrollHorEnabled(flag: Boolean) {
        this.isScrollHorEnabled = flag
    }

    override fun canScrollHorizontally(): Boolean {
        return isScrollHorEnabled && super.canScrollHorizontally()
    }

    override fun canScrollVertically(): Boolean {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollVerEnabled && super.canScrollVertically()
    }
}