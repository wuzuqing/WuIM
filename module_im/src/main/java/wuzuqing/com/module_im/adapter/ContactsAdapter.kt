package wuzuqing.com.module_im.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.wuzuqing.component_base.base.adapter.CommonRecyclerAdapter
import com.wuzuqing.component_base.util.ImageLoadUtils
import com.wuzuqing.component_im.bean.ContactsBean
import wuzuqing.com.module_im.R

class ContactsAdapter(layoutResId: Int) : CommonRecyclerAdapter<ContactsBean>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item: ContactsBean) {
        helper.setText(R.id.im_item_contacts_tv_nick, item.nick)
        ImageLoadUtils.display(helper.getView(R.id.im_item_contacts_iv_avatar),item.avatar)
//        helper.setText(R.id.im_item_contacts_tv_letter,)
    }
}
