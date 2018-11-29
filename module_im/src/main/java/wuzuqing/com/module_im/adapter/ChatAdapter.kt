package wuzuqing.com.module_im.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wuzuqing.component_base.util.FileUtils
import com.wuzuqing.component_base.util.ImageLoadUtils
import com.wuzuqing.component_base.util.TimeUtils
import com.wuzuqing.component_data.bean.UserInfoBean
import com.wuzuqing.component_data.cache.GlobalVariable
import com.wuzuqing.component_data.constant.BaseHost
import com.wuzuqing.component_im.common.packets.ChatBody
import com.wuzuqing.component_im.contract.ChatItem
import com.wuzuqing.component_im.db.DbCore
import org.jetbrains.anko.sdk25.coroutines.onClick
import wuzuqing.com.module_im.R
import wuzuqing.com.module_im.util.DownloadUtil
import wuzuqing.com.module_im.widget.ImRoundRectImageView
import wuzuqing.com.module_im.widget.voiceview.VoicePlayClickListener

class ChatAdapter : BaseMultiItemQuickAdapter<ChatBody, BaseViewHolder> {
    private var toUser: UserInfoBean? = null
    var isPrivate = true
    var isScrolling = false


    constructor(flag: Boolean) : super(ArrayList<ChatBody>()) {
        addItemType(ChatItem.ITEM_TYPE_TEXT_FROM, R.layout.im_item_chat_text_from)
        addItemType(ChatItem.ITEM_TYPE_VIDEO_CALL_FROM, R.layout.im_item_chat_text_from)
        addItemType(ChatItem.ITEM_TYPE_VIDEO_CALL_TO, R.layout.im_item_chat_text_to)
        addItemType(ChatItem.ITEM_TYPE_TEXT_TO, R.layout.im_item_chat_text_to)
        addItemType(ChatItem.ITEM_TYPE_IMG_FROM, R.layout.im_item_chat_img_from)
        addItemType(ChatItem.ITEM_TYPE_IMG_TO, R.layout.im_item_chat_img_to)
        addItemType(ChatItem.ITEM_TYPE_VOICE_FROM, R.layout.im_item_chat_voice_from)
        addItemType(ChatItem.ITEM_TYPE_VOICE_TO, R.layout.im_item_chat_voice_to)
        isPrivate = flag
        if (isPrivate) {
            toUser = UserInfoBean(GlobalVariable.get().targetIdInt, GlobalVariable.get().targetNick, GlobalVariable.get().targetAvatar)
        }
    }


    open class ChatTarget(val iv: ImRoundRectImageView) : SimpleTarget<Drawable>() {

        override fun onLoadStarted(placeholder: Drawable?) {
            iv.setWHStyle(placeholder)
        }

        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            iv.setWHStyle(resource)
        }
    }

    override fun convert(helper: BaseViewHolder, item: ChatBody) {
        helper.addOnClickListener(R.id.im_item_chat_avatar)
        var isFrom = item.from.toInt() == GlobalVariable.get().userId
        var user: UserInfoBean? = if (isFrom) {
            GlobalVariable.get().user
        } else {
            if (isPrivate) {
                toUser
            } else {
                GlobalVariable.get().groupMembers!![item.from.toInt()]
            }
        }

        when (item.msgType) {
            0 -> helper.setText(R.id.im_item_chat_content, item.content)
            1,3 -> {
                val iv = helper.getView<ImRoundRectImageView>(R.id.im_item_chat_img)
                if (isScrolling){
                    iv.setImageResource(R.mipmap.ic_image_loading)
                }else{
                    ImageLoadUtils.displayChatImg(iv, item.url, ChatTarget(iv))
                }
                iv.setDuration((item.duration*1000).toLong())
                helper.addOnClickListener(R.id.im_item_chat_img)
            }
            6->{
                helper.setText(R.id.im_item_chat_content,item.content)
            }
            2 -> {
                val ivVoice = helper.getView<ImageView>(R.id.im_item_chat_voice)
                ivVoice.onClick {
                  val isFile =  FileUtils.isFileExists(item.localPath)
                    VoicePlayClickListener.get().playVoice(ivVoice, if (isFile) {
                        item.localPath
                    } else {
                        (BaseHost.STATIC_HOST + item.url)
                    }, isFrom)
                    if (!isFile){
                      DownloadUtil.download(item.url,item.localPath)
                    }
                    if ( !item.isListen) {
                        item.isListen = true
                        helper.getView<TextView>(R.id.im_item_chat_second).isSelected = true
                        //修改标记数据库
                        DbCore.getDaoSession().chatBodyDao.update(item)
                    }
                }
                helper.setText(R.id.im_item_chat_second, "${item.duration}''")
                helper.getView<TextView>(R.id.im_item_chat_second).isSelected = item.isListen
            }

        }

        if (user != null) {
            helper.setText(R.id.im_item_chat_nick, user!!.nick)
            ImageLoadUtils.display(helper.getView(R.id.im_item_chat_avatar), user!!.avatar)
        }
    }

}
