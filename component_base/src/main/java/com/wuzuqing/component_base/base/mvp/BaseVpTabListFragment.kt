package com.wuzuqing.component_base.base.mvp

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.wuzuqing.component_base.R
import com.wuzuqing.component_base.base.mvp.inter.IPresenter
import com.wuzuqing.component_base.base.mvp.inter.IView
import com.wuzuqing.component_im.bean.TabListBean
import com.wuzuqing.component_base.util.ColorCampat
import kotlinx.android.synthetic.main.fragment_tab_list.*

/**
 * @Created by TOME .
 * @时间 2018/6/5 15:27
 * @描述 ${Tab列表Activity}
 */

abstract class BaseVpTabListFragment<V : IView, P : IPresenter<V>> : BaseVpFragment<V, P>(), View.OnClickListener {

    //    @BindView(R.id.title_back)
    //    TextView mTitleBack;
    //    @BindView(R.id.title_content_text)
    //    TextView mTitleContentText;
    //    @BindView(R.id.tl_tabs)
    //    TabLayout mTlTabs;
    //    @BindView(R.id.vp_view)
    //    ViewPager mVpView;
    //    @BindView(R.id.layout_frag_tab_list)
//    internal var mLayoutFragTabList: LinearLayout? = null
    private var titleList: List<TabListBean>? = null

    protected val isTabScrollable: Boolean
        get() = false

    override fun getLayout(): Int {
        titleList = tabTitles()
        return R.layout.fragment_tab_list
    }

    override fun initTitle() {
        layout_frag_tab_list!!.setBackgroundColor( ColorCampat.getColor(R.color.windowBg)  )
        vp_view.adapter = TabAdapter(childFragmentManager)
        tl_tabs.tabMode = if (isTabScrollable) TabLayout.MODE_SCROLLABLE else TabLayout.MODE_FIXED
        tl_tabs.setupWithViewPager(vp_view)
//        mTitleContentText.setText(setTitle())

    }

    override fun initView() {
//        mTitleBack.setOnClickListener(this)

    }

    override fun onClick(v: View) {}


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
