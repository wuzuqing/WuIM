package wuzuqing.com.module_im.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wuzuqing.component_base.base.mvc.BaseVcListFragment
import com.wuzuqing.component_base.net.ModelService
import com.wuzuqing.component_base.net.common_callback.INetCallback
import com.wuzuqing.component_base.util.HandlerManager
import com.wuzuqing.component_base.util.ObjectUtils
import com.wuzuqing.component_data.cache.GlobalVariable
import com.wuzuqing.component_im.bean.ContactsBean
import com.wuzuqing.component_im.db.DbCore
import kotlinx.android.synthetic.main.im_fragment_contacts.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import wuzuqing.com.module_im.R
import wuzuqing.com.module_im.adapter.ContactsAdapter
import wuzuqing.com.module_im.api.ApiService
import wuzuqing.com.module_im.arouter.RouterCenter

class ContactsFragment : BaseVcListFragment<ContactsBean>() {


    override fun getLayout(): Int {
        return R.layout.im_fragment_contacts
    }

    var headerView: View? = null
    override fun initView() {
        super.rlRefreshLayout = im_fm_srf
        super.initView()
        setRecyclerView(im_fm_rv, LinearLayoutManager(context), ContactsAdapter(R.layout.im_item_contacts))
        setItemClick(true, true, false, false)
        im_fm_srf.isEnableRefresh = false
        im_fm_srf.isEnableLoadMore = false
    }

    override fun loadListData(rlRefreshLayout: SmartRefreshLayout?, page: Int, pageSize: Int) {
        val list = DbCore.getDaoSession().contactsBeanDao.loadAll()
        if (ObjectUtils.isNotEmpty(list)) {
            setData(list)
        } else {
            ModelService.getRemoteListData(mView, rlRefreshLayout, ApiService::class.java,
                    ModelService.MethodSelect<List<ContactsBean>, ApiService>
                    { service -> service!!.getFriend(GlobalVariable.get().userId) }, INetCallback<List<ContactsBean>> {
                setData(it)
                HandlerManager.async(Runnable {
                    DbCore.getDaoSession().contactsBeanDao.deleteAll()
                    DbCore.getDaoSession().contactsBeanDao.insertOrReplaceInTx(it)
                })
            })
        }
    }

    private fun setData(it: List<ContactsBean>?) {
        initHeaderView()
        mAdapter!!.setHeaderView(headerView)
        mAdapter!!.setNewData(it)
    }

    private fun initHeaderView() {
        if (headerView == null) {
            headerView = LayoutInflater.from(context).inflate(R.layout.im_inflate_contacts_header, null)
            headerView!!.find<TextView>(R.id.im_inflate_contacts_header_new_friend).onClick {

            }
            headerView!!.find<TextView>(R.id.im_inflate_contacts_header_group).onClick {
                RouterCenter.toGroupList()
            }
            headerView!!.find<TextView>(R.id.im_inflate_contacts_header_tag).onClick {

            }
        }
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val item = mAdapter!!.getItem(position)
        RouterCenter.toChat(true, "-1",item!!.id, item!!.nick, item!!.avatar)
    }
}
