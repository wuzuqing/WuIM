package wuzuqing.com.module_im.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wuzuqing.component_base.base.mvc.BaseVcListActivity
import com.wuzuqing.component_base.net.ModelService
import com.wuzuqing.component_base.net.common_callback.INetCallback
import com.wuzuqing.component_base.util.HandlerManager
import com.wuzuqing.component_base.util.LogUtils
import com.wuzuqing.component_base.util.ObjectUtils
import com.wuzuqing.component_data.cache.GlobalVariable
import com.wuzuqing.component_data.d_arouter.RouterURLS
import com.wuzuqing.component_im.bean.GroupBean
import com.wuzuqing.component_im.db.DbCore
import kotlinx.android.synthetic.main.im_activity_group_list.*
import wuzuqing.com.module_im.R
import wuzuqing.com.module_im.adapter.GroupListAdapter
import wuzuqing.com.module_im.api.ApiService
import wuzuqing.com.module_im.arouter.RouterCenter


@Route(path = RouterURLS.IM_GROUP_LIST)
class GroupListActivity : BaseVcListActivity<GroupBean>() {


    override fun getLayout(): Int {
        return R.layout.im_activity_group_list
    }


    override fun initView() {
        im_act_group_list_recycler.layoutManager = LinearLayoutManager(this)
        mAdapter = GroupListAdapter()
        mAdapter!!.onItemClickListener = this
        im_act_group_list_recycler.adapter = mAdapter
        loadListData(null, 0, 0)
    }

    override fun loadListData(rlRefreshLayout: SmartRefreshLayout?, page: Int, pageSize: Int) {
        val list = DbCore.getDaoSession().groupBeanDao.loadAll()
        if (ObjectUtils.isNotEmpty(list)) {
            LogUtils.d("loadListData:$list")
            mAdapter!!.setNewData(list)
        } else {
            ModelService.getRemoteData(true, this, ApiService::class.java,
                    { service -> service.getGroupList(GlobalVariable.get().userId) },
                    {
                        if (it!=null && it.isNotEmpty()){
                            mAdapter!!.setNewData(it)
                            HandlerManager.async(Runnable {
                                DbCore.getDaoSession().groupBeanDao.deleteAll()
                                DbCore.getDaoSession().groupBeanDao.insertOrReplaceInTx(it)
                            })
                        }
                    })
        }

    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val item = mAdapter!!.getItem(position)
        RouterCenter.toChat(false, item!!.id.toString(),item!!.id, item!!.groupName, item!!.avatar)
    }

}