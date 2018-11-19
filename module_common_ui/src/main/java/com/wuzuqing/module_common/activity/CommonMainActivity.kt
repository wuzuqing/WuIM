package com.wuzuqing.module_common.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wuzuqing.component_base.util.JsonUtil
import com.wuzuqing.component_base.widget.SuperDividerItemDecoration
import com.wuzuqing.component_data.d_arouter.RouterURLS
import com.wuzuqing.module_common.R
import com.wuzuqing.module_common.adapter.CommonAdapter
import com.wuzuqing.module_common.bean.CommonBean
import java.util.*

@Route(path = RouterURLS.CUSTOM_CONTROL)
class CommonMainActivity : AppCompatActivity(), BaseQuickAdapter.OnItemClickListener {

    private var mRecyclerView: RecyclerView? = null
    private var mTitleBack: TextView? = null
    private var mTitleText: TextView? = null
    private var mJsonList: List<CommonBean>? = null
    private var mAdapter: CommonAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        initData()
        initView()

    }

    private fun initData() {
        //读取json文件
        val json = JsonUtil.getJson(this, "customControl.json")
        mJsonList = Gson().fromJson<List<CommonBean>>(json, object : TypeToken<ArrayList<CommonBean>>() {

        }.type)
    }

    private fun initView() {
        mRecyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        mTitleBack = findViewById<View>(R.id.title_back) as TextView
        mTitleText = findViewById<View>(R.id.title_content_text) as TextView
        //设置标题
        mTitleText!!.text = "常用自定义控件汇总"
        //mTitleBack.setVisibility(View.VISIBLE);

        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        //分割线
        mRecyclerView!!.addItemDecoration(SuperDividerItemDecoration.Builder(this)
                .setDividerWidth(0)
                .setDividerColor(resources.getColor(R.color.white))
                .build())
        mAdapter = CommonAdapter(R.layout.item_common, mJsonList)
        mAdapter!!.onItemClickListener = this
        mRecyclerView!!.adapter = mAdapter

    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if ("0" == mJsonList!![position].type) {
            startActivity(Intent(this@CommonMainActivity, TagFlowActivity::class.java))
        } else if ("1" == mJsonList!![position].type) {
            startActivity(Intent(this@CommonMainActivity, AlertViewDemoActivity::class.java))
        } else if ("2" == mJsonList!![position].type) {
            startActivity(Intent(this@CommonMainActivity, HoverItemActivity::class.java))
        } else if ("3" == mJsonList!![position].type) {
            startActivity(Intent(this@CommonMainActivity, DragBallActivity::class.java))
        } else if ("4" == mJsonList!![position].type) {
            startActivity(Intent(this@CommonMainActivity, RatingBarActivity::class.java))
        } else if ("5" == mJsonList!![position].type) {
            startActivity(Intent(this@CommonMainActivity, PopupWindowActivity::class.java))
        } else if ("6" == mJsonList!![position].type) {
            startActivity(Intent(this@CommonMainActivity, PickerViewActivity::class.java))
        } else if ("7" == mJsonList!![position].type) {
            startActivity(Intent(this@CommonMainActivity, MyAccountActivity::class.java))
        } else if ("8" == mJsonList!![position].type) {
            //            startActivity(new Intent(CommonMainActivity.this, VideoCompressActivity.class));
        } else if ("9" == mJsonList!![position].type) {
//            startActivity(Intent(this@CommonMainActivity, PictureSelectorActivity::class.java))
        }
    }


    fun jumpTo() {
        if (mRecyclerView != null) {
            mRecyclerView!!.smoothScrollToPosition(0)
        }
    }
}
