package wuzuqing.com.module_im.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wuzuqing.component_base.base.mvc.BaseVcListFragment
import com.wuzuqing.component_base.util.LogUtils
import com.wuzuqing.component_base.util.ObjectUtils
import com.wuzuqing.component_data.d_arouter.RxTag
import com.wuzuqing.component_im.bean.Conversation
import com.wuzuqing.component_im.common.packets.ChatBody
import com.wuzuqing.component_im.common.utils.TcpManager
import com.wuzuqing.component_im.db.DbCore
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.im_fragment_conversation.*
import wuzuqing.com.module_im.R
import wuzuqing.com.module_im.adapter.ConversationAdapter
import wuzuqing.com.module_im.arouter.RouterCenter
import wuzuqing.com.module_im.util.UnReadCountManager


class ConversationFragment : BaseVcListFragment<Conversation>(), BaseQuickAdapter.OnItemLongClickListener {

    override fun getLayout(): Int {
        return R.layout.im_fragment_conversation
    }

    override fun initView() {
        super.rlRefreshLayout = im_fm_srf
        super.initView()
        setRecyclerView(im_fm_rv, LinearLayoutManager(context), ConversationAdapter(R.layout.im_item_conversation))
        setItemClick(true, true, false, false)
        im_fm_srf.isEnableRefresh = false
        im_fm_srf.isEnableLoadMore = false

        im_fm_rv.itemAnimator = null
        loadListData(im_fm_srf, page, pageSize)
        TcpManager.get().setRefreshConversationListener {

            loadListData(im_fm_srf, page, pageSize)
        }

        on<ChatBody>(RxTag.UPDATE_CONVERSATION, Consumer {
            val list = mAdapter!!.data
            if (list.isNotEmpty()) {
                list.forEachIndexed { index, ct ->
                    if (it.sessionId == ct.sessionId) {
                        val newCt = DbCore.getDaoSession().conversationDao.load(ct.id)
                        newCt.unReadCount = ct.unReadCount +1
                        mAdapter!!.remove(index)
                        mAdapter!!.addData(0, newCt)
                        UnReadCountManager.getInstance().addCount(1, true)
                        return@Consumer
                    }
                }
            }
            loadListData(im_fm_srf, page, pageSize)
        })
    }

    override fun onPause() {
        super.onPause()
        if (isVisible) {
            TcpManager.get().setShowConversationFragment(false)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isVisible) {
            TcpManager.get().setShowConversationFragment(true)
        }
    }

    override fun onFragmentVisibleChange(isVisible: Boolean) {
        TcpManager.get().setShowConversationFragment(isVisible)
        if (isVisible && mAdapter != null && TcpManager.get().isHasNewMsg) {
            loadListData(im_fm_srf, page, pageSize)
        }
    }

    override fun loadListData(rlRefreshLayout: SmartRefreshLayout?, page: Int, pageSize: Int) {
        val list = DbCore.getDaoSession().conversationDao.loadAll()
        if (ObjectUtils.isNotEmpty(list)) {
            UnReadCountManager.getInstance().reset()
            var result: List<ChatBody>?
            list.map {
                result = DbCore.getDaoSession().chatBodyDao.queryRaw("where SESSION_ID = ? and IS_READ = ?", it.sessionId, "0")
                if (ObjectUtils.isNotEmpty(result)) {
                    it.unReadCount = result!!.size
                    UnReadCountManager.getInstance().addCount(it.unReadCount, false)
                }
            }
            UnReadCountManager.getInstance().refresh()
            list.reverse()
        }
        mAdapter!!.setNewData(list)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val item = mAdapter!!.getItem(position)

        when (item!!.chatType) {
            1 -> {
                RouterCenter.toChat(false, item!!.sessionId,item!!.targetId, item!!.nick, item!!.avatar)
            }
            2 -> {
                RouterCenter.toChat(true, item!!.sessionId,item!!.targetId, item!!.nick, item!!.avatar)
            }
        }
        UnReadCountManager.getInstance().removeCount(item!!.unReadCount)
        item.unReadCount = 0
        adapter!!.notifyItemChanged(position)
    }

    override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int): Boolean {
        val  item =  mAdapter!!.getItem(position)
        DbCore.getDaoSession().conversationDao.deleteByKey(item!!.id)
        mAdapter!!.remove(position)
        return true
    }

}
