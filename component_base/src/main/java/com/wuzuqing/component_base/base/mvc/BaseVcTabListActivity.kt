package com.wuzuqing.component_base.base.mvc

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.wuzuqing.component_base.R
import com.wuzuqing.component_im.bean.TabListBean
import com.wuzuqing.component_base.util.ColorCampat
import kotlinx.android.synthetic.main.fragment_tab_list.*

/**
 * @Created by TOME .
 * @时间 2018/6/5 16:06
 * @描述 ${Tab列表Activity}
 */

abstract class BaseVcTabListActivity : BaseVcActivity() {



    private var titleList: List<TabListBean>? = null

    private val isTabScrollable: Boolean
        get() = false

    override fun getLayout(): Int {

        return R.layout.fragment_tab_list
        // return getTabLayoutId();
    }

    override fun initView() {
        titleList = tabTitles()
        layout_frag_tab_list.setBackgroundColor(  ColorCampat.getColor(R.color.windowBg) )
        if (titleList == null || titleList!!.isEmpty()) {
            return
        }
        val size = titleList!!.size
        for (i in 0 until size) {

            tl_tabs.addTab(tl_tabs.newTab().setText(titleList!![i].title))
        }
        vp_view.offscreenPageLimit = size
        ntb.setTitleText(setTitle())
        vp_view.adapter = TabAdapter(supportFragmentManager)
        tl_tabs.tabMode = if (isTabScrollable) TabLayout.MODE_SCROLLABLE else TabLayout.MODE_FIXED
        tl_tabs.setupWithViewPager(vp_view)
    }


    internal inner class TabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return titleList!![position].fragment
        }

        override fun getCount(): Int {
            return titleList!!.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList!![position].title
        }
    }

    //  protected abstract int getTabLayoutId();
    /**
     * tab标题集合
     *
     * @return
     */
    protected abstract fun tabTitles(): List<TabListBean>

    /**
     * 设置标题
     * @return
     */
    protected abstract fun setTitle(): String
}
