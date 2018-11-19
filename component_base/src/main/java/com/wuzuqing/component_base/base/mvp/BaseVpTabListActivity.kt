package com.wuzuqing.component_base.base.mvp

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.wuzuqing.component_base.R
import com.wuzuqing.component_base.base.mvp.inter.IPresenter
import com.wuzuqing.component_base.base.mvp.inter.IView
import com.wuzuqing.component_im.bean.TabListBean
import com.wuzuqing.component_base.util.ColorCampat
import kotlinx.android.synthetic.main.fragment_tab_list.*

/**
 * @Created by TOME .
 * @时间 2018/6/5 16:06
 * @描述 ${Tab列表Activity}
 */

abstract class BaseVpTabListActivity<V : IView, P : IPresenter<V>> : BaseVpActivity<V, P>() {


    //    TextView mTitleBack;
    //    @BindView(R.id.title_content_text)
    //    TextView mTitleContentText;
    //    @BindView(R.id.tl_tabs)
    //    TabLayout mTlTabs;
    //    @BindView(R.id.vp_view)
    //    ViewPager mVpView;
    //    @BindView(R.id.layout_frag_tab_list)
    //    LinearLayout mLayoutFragTabList;

    private var titleList: List<TabListBean>? = null

    protected val isTabScrollable: Boolean
        get() = false

    override fun getLayoutId(): Int {
        titleList = tabTitles()
        return R.layout.fragment_tab_list
    }

    override fun initTitle() {
        layout_frag_tab_list.setBackgroundColor( ColorCampat.getColor(R.color.windowBg)  )
        if (titleList == null || titleList!!.isEmpty()) {
            return
        }
        val size = titleList!!.size
        for (i in 0 until size) {
            tl_tabs.addTab(tl_tabs.newTab().setText(titleList!![i].title))
        }
        vp_view.offscreenPageLimit = size
//        mTitleContentText.setText(setTitle())
//        mTitleBack.setVisibility(View.GONE)
    }

    override fun initView() {
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
