package wuzuqing.com.module_im.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.wuzuqing.component_base.base.adapter.CommonRecyclerAdapter
import com.wuzuqing.component_im.bean.GroupBean
import wuzuqing.com.module_im.R
import wuzuqing.com.module_im.util.IMItemUtil
import wuzuqing.com.module_im.util.NiceImageViewUtil

class GroupListAdapter : CommonRecyclerAdapter<GroupBean>(R.layout.im_item_group_list) {

    override fun convert(helper: BaseViewHolder, item: GroupBean) {
        helper.setText(R.id.im_item_list_name, item.groupName)
        NiceImageViewUtil.getRectCache().setAvatars(helper.getView(R.id.im_item_list_img), IMItemUtil.parseAvatar(1, item.avatar))
    }

}
