package wuzuqing.com.module_im.fragment

import android.support.v4.view.PagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseViewHolder
import com.cjt2325.cameralibrary.CameraActivity
import com.wuzuqing.component_base.base.adapter.CommonRecyclerAdapter
import com.wuzuqing.component_base.base.mvc.BaseVcFragment
import com.wuzuqing.component_data.d_arouter.RxTag
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.api.widget.Widget
import kotlinx.android.synthetic.main.im_fragment_chat_select.*
import wuzuqing.com.module_im.R

class ChatSelectFragment : BaseVcFragment() {


    override fun getLayout(): Int {
        return R.layout.im_fragment_chat_select
    }

    override fun initView() {
        var views = ArrayList<View>()
        val view = RecyclerView(context)
        val itemAdapter = SelectItemAdapter()
        val color = resources.getColor(R.color.title_black)
        view.layoutManager = GridLayoutManager(context, 4)
        view.adapter = itemAdapter
        itemAdapter.addData(getItems(0))
        itemAdapter.setOnItemClickListener { _, _, position ->
            val item = itemAdapter.getItem(position)
            when(item!!){
                SelectItems.PHOTO ->{ //相册

            Album.album(context)
                    .multipleChoice()
                    .selectCount(9)
                    .camera(false)
                    .columnCount(4)
                    .widget(
                            Widget.newDarkBuilder(context)
                                    .title("图片选择")
                                    .toolBarColor(color)
                                    .statusBarColor(color)
                                    .build()
                    )
                    .afterFilterVisibility(false)
                    .onResult {
                       post(RxTag.SEND_ALBUM,it)
                    }
                    .start()
                }
                SelectItems.TAKES ->{//拍摄
                    CameraActivity.startActivity(activity)
                }
            }
        }
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


    private fun getItems(type: Int): List<SelectItems> {
        val result = ArrayList<SelectItems>()
        result.add(SelectItems.PHOTO)
        result.add(SelectItems.TAKES)
        result.add(SelectItems.MEDIA_CALL)
        result.add(SelectItems.LOCATION)
        result.add(SelectItems.PICKET)

        result.add(SelectItems.ID_CARD)
        result.add(SelectItems.COLLECT)
        when (type) {
            0 -> {
                result.add(SelectItems.TRANSFER)
            }
        }
        return result
    }


}
