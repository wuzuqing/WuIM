package wuzuqing.com.module_im.adapter

import com.chad.library.adapter.base.BaseViewHolder
import com.wuzuqing.component_base.base.adapter.CommonRecyclerAdapter
import com.wuzuqing.component_base.util.TimeUtils
import com.wuzuqing.component_base.widget.BadgeView
import com.wuzuqing.component_im.bean.Conversation
import wuzuqing.com.module_im.R
import wuzuqing.com.module_im.util.IMItemUtil
import wuzuqing.com.module_im.util.NiceImageViewUtil

class ConversationAdapter(layoutResId: Int) : CommonRecyclerAdapter<Conversation>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item: Conversation) {
        helper.setText(R.id.im_item_conversation_tv_nick, item.nick)
        helper.setText(R.id.im_item_conversation_tv_time, TimeUtils.getFriendlyTime(item.lastMessageCreateTime))
        helper.setText(R.id.im_item_conversation_tv_content, IMItemUtil.parseContent(item.lastMsgType, item.lastMessageContent))

        helper.setVisible(R.id.im_item_conversation_iv_disturb, item.isDisturb)
        helper.getView<BadgeView>(R.id.im_item_conversation_badge).setNumber(item.unReadCount)
        NiceImageViewUtil.getRectCache().setAvatars(helper.getView(R.id.im_item_conversation_iv_avatar),
                IMItemUtil.parseAvatar(item.chatType, item.avatar))
    }


}
