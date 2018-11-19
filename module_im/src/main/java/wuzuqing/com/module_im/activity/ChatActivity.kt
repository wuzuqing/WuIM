package wuzuqing.com.module_im.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.wuzuqing.component_base.base.mvc.BaseVcListActivity
import com.wuzuqing.component_base.net.ModelService
import com.wuzuqing.component_base.net.common_callback.INetCallback
import com.wuzuqing.component_base.util.LogUtils
import com.wuzuqing.component_base.util.ObjectUtils
import com.wuzuqing.component_data.bean.GroupMemberBean
import com.wuzuqing.component_data.cache.GlobalVariable
import com.wuzuqing.component_data.d_arouter.RouterURLS
import com.wuzuqing.component_data.d_arouter.RxTag
import com.wuzuqing.component_im.common.packets.ChatBody
import com.wuzuqing.component_im.common.packets.ChatType
import com.wuzuqing.component_im.common.utils.TcpManager
import com.wuzuqing.component_im.dao.ChatBodyDao
import com.wuzuqing.component_im.db.DbCore
import com.yanzhenjie.album.AlbumFile
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.im_activity_chat.*
import org.greenrobot.greendao.query.WhereCondition
import wuzuqing.com.module_im.R
import wuzuqing.com.module_im.adapter.ChatAdapter
import wuzuqing.com.module_im.api.ApiService
import wuzuqing.com.module_im.listener.MediaTextScrollListener
import wuzuqing.com.module_im.util.ChatPanelHelper
import wuzuqing.com.module_im.util.TranslationAnimator
import wuzuqing.com.module_im.widget.voiceview.VoicePlayClickListener
import java.util.*
import kotlin.collections.ArrayList


@Route(path = RouterURLS.IM_CHAT)
class ChatActivity : BaseVcListActivity<ChatBody>() {

    private var isPrivate = true
    var name: String? = null
    private var groupId: String? = null
    override fun getLayout(): Int {
        return R.layout.im_activity_chat
    }

    override fun initExtra(extra: Intent) {
        isPrivate = extra.getBooleanExtra("isPrivate", true)
        if (extra.hasExtra("targetId")) {
            GlobalVariable.toChatParams(extra.getIntExtra("targetId", 0),
                    extra.getStringExtra("nick"), extra.getStringExtra("avatar"))
        }
        if (isPrivate) {
            TcpManager.get().setChatType(ChatType.CHAT_TYPE_PRIVATE)
            name = GlobalVariable.get().targetNick
            TcpManager.get().setToId(GlobalVariable.get().targetId)
        } else {
            TcpManager.get().setChatType(ChatType.CHAT_TYPE_PUBLIC)
            name = GlobalVariable.get().targetNick
            groupId = GlobalVariable.get().targetId
        }
    }


    private var chatPanelHelper: ChatPanelHelper? = null


    override fun initView() {
        im_act_chat_ntb.setTitleText(name)
        super.rlRefreshLayout = im_act_chat_srf
        autoRefresh = false
        super.initView()
        rlRefreshLayout!!.isEnableLoadMore = false
        val adapter = ChatAdapter(isPrivate)
        setRecyclerView(im_act_chat_recycler, LinearLayoutManager(context), adapter)
        setItemClick(false, false, true, true)
        chatPanelHelper = ChatPanelHelper(this, im_act_chat_bottom_container, im_act_chat_input_panel,
                im_act_chat_et_content, im_act_chat_iv_face, im_act_chat_iv_recorder)

        chatPanelHelper!!.init(im_act_chat_btn_recorder, im_act_chat_vrv, im_act_chat_btn_img, im_act_chat_btn_send, isPrivate)

        //添加滑动监听
        MediaTextScrollListener(im_act_chat_recycler, adapter).setOnClickListener {
            chatPanelHelper!!.clickList()
        }

        if (!isPrivate) {
            ModelService.getRemoteData(false, this, ApiService::class.java,
                    ModelService.MethodSelect<List<GroupMemberBean>, ApiService> { service ->
                        service!!.getGroupMemberList(groupId, GlobalVariable.get().userId)
                    }, INetCallback<List<GroupMemberBean>> {
                val groupMembers = HashMap<Int, GroupMemberBean>()
                it.forEach {
                    groupMembers[it.uId] = it
                }
                GlobalVariable.get().groupMembers = groupMembers
                listenMsg()
            })
        } else {
            listenMsg()
        }
        on<ArrayList<AlbumFile>>(RxTag.SEND_ALBUM, Consumer {
            val images = ArrayList<String>()
            it.forEach {
                if (it.mediaType == AlbumFile.TYPE_IMAGE) {
                    images.add(it.path)
                } else if (it.mediaType == AlbumFile.TYPE_VIDEO) {
                    LogUtils.d("SEND_ALBUM:$it")
                    TcpManager.get().sendVideo(isPrivate, it.path, (it.duration / 1000).toInt())
                }
            }
            if (ObjectUtils.isNotEmpty(images)) {
                TcpManager.get().sendImage(isPrivate, images)
            }
        })
    }


    //监听收到的消息
    private fun listenMsg() {
        TcpManager.get().setDisposeMessage {
            //            LogUtils.d(it)
            if (GlobalVariable.checkChat(it.from, it.to, it.chatType, groupId, it.group_id, isPrivate)) {
                mAdapter!!.addData(it)
                im_act_chat_recycler.scrollToPosition(mAdapter!!.itemCount - 1)
            }
        }
        //第一次刷新数据
        onRefresh(im_act_chat_srf)
        im_act_chat_recycler.scrollToPosition(mAdapter!!.itemCount - 1)
        //处理已读标记
        seId = if (isPrivate) {
            TcpManager.get().sessionId
        } else {
            groupId
        }
        DbCore.updateList("CHAT_BODY", "IS_READ=1", "SESSION_ID = '$seId' and IS_READ = 0")
        TcpManager.get().refreshConversation(false)
    }

    var seId: String? = null
    //加载历史消息
    override fun onRefresh(refreshLayout: RefreshLayout) {
        loadListData(rlRefreshLayout, page, pageSize)
        page++
    }

    override fun loadListData(rlRefreshLayout: SmartRefreshLayout?, page: Int, pageSize: Int) {
        if (isPrivate) {
            LogUtils.d("loadListData:${TcpManager.get().sessionId}")
            loadFromDb(ChatBodyDao.Properties.SessionId.eq(TcpManager.get().sessionId), page, pageSize)
        } else {
            loadFromDb(ChatBodyDao.Properties.Group_id.eq(groupId), page, pageSize)
        }
    }

    private fun loadFromDb(where: WhereCondition, page: Int, pageSize: Int) {
        try {
            val list = DbCore.getDaoSession().chatBodyDao.queryBuilder().where(where)
                    .orderDesc(ChatBodyDao.Properties.CreateTime)
                    .offset(pageSize * page).limit(pageSize).list()
            list.reverse()
            if (list.isEmpty()) {
                im_act_chat_srf.finishRefresh()
                im_act_chat_srf.isEnableRefresh = false
            } else {
                mAdapter!!.addData(0, list)
                im_act_chat_srf.finishRefresh(100)
                if (list.size < pageSize) {
                    im_act_chat_srf.isEnableRefresh = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val item = mAdapter!!.getItem(position)
        when (item!!.msgType) {
            1, 3 -> {
                val list = DbCore.getDaoSession().chatBodyDao.queryRaw("where SESSION_ID = ? and MSG_TYPE in (1,3)", seId)
                var nowIndex = 0
                if (ObjectUtils.isNotEmpty(list)) {
                    list.forEachIndexed { index, it ->
                        if (it._ID == item._ID) {
                            nowIndex = index
                        }
                    }
                }
                PreviewActivity.Data.setData(list, nowIndex)
                TranslationAnimator.Action.startAction(this, PreviewActivity::class.java, view!!,item!!.url)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data != null) {
            val path = data!!.getStringExtra("path")
            if (101 == resultCode) { //图片
                TcpManager.get().sendImage(isPrivate, Arrays.asList(path))
            } else if (102 == resultCode) {//视频
                TcpManager.get().sendVideo(isPrivate, path, data!!.getIntExtra("duration", 0))
            }
        }
    }

    override fun onBackPressed() {
        if (chatPanelHelper!!.canBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        VoicePlayClickListener.get().stopPlayVoice()
        TcpManager.get().levelChat()
    }
}