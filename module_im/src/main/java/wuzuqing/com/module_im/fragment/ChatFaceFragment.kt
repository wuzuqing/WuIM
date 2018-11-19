package wuzuqing.com.module_im.fragment

import android.support.v4.view.PagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.wuzuqing.component_base.base.adapter.CommonRecyclerAdapter
import com.wuzuqing.component_base.base.mvc.BaseVcFragment
import kotlinx.android.synthetic.main.im_fragment_chat_select.*
import wuzuqing.com.module_im.R

class ChatFaceFragment : BaseVcFragment() {


    override fun getLayout(): Int {
        return R.layout.im_fragment_chat_select
    }

    override fun initView() {
        var views = ArrayList<View>()
        val view = RecyclerView(context)
        val itemAdapter = SelectItemAdapter()
        view.layoutManager = GridLayoutManager(context, 4)
        view.adapter = itemAdapter
        views.add(view)
        im_fm_chat_select_vp.adapter = GridViewAdapter(views)
    }


    class SelectItemAdapter : CommonRecyclerAdapter<SelectItems>(R.layout.im_item_select_item) {
        override fun convert(helper: BaseViewHolder, item: SelectItems) {
            helper.setText(R.id.im_item_select_item_tv, item.title)
            helper.setImageResource(R.id.im_item_select_item_iv, item.icon)
        }
    }

    class GridViewAdapter(private val views: List<View>) : PagerAdapter() {
        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view == obj
        }

        override fun getCount(): Int {
            return views.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(views[position])
            return views[position]
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(views[position])
//            super.destroyItem(container, position, `object`)
        }

    }




}
