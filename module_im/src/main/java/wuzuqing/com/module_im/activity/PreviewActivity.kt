package wuzuqing.com.module_im.activity

import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wuzuqing.component_base.base.mvc.BaseVcActivity
import com.wuzuqing.component_base.constants.BaseApplication
import com.wuzuqing.component_base.util.FileUtils
import com.wuzuqing.component_base.util.ImageLoadUtils
import com.wuzuqing.component_base.util.LogUtils
import com.wuzuqing.component_base.util.ObjectUtils
import com.wuzuqing.component_base.widget.ProgressTextView
import com.wuzuqing.component_data.constant.BaseHost
import com.wuzuqing.component_im.common.packets.ChatBody
import com.yanzhenjie.album.widget.photoview.PhotoImageView
import fm.jiecao.jcvideoplayer_lib.JCMediaManager
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.im_chat_preview.*
import wuzuqing.com.module_im.R
import wuzuqing.com.module_im.listener.CustomLinearLayoutManager
import wuzuqing.com.module_im.util.TranslationAnimator
import wuzuqing.com.module_im.widget.PreviewVideoView
import java.util.*


class PreviewActivity : BaseVcActivity(), View.OnClickListener {

    override fun getLayout(): Int {
        return R.layout.im_chat_preview
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

    override fun getThemeId(): Int {
        return R.style.Translucent
    }

    private var recyclerView: RecyclerView? = null
    private var mAdapter: PreviewAdapter? = null

    private var ivMore: ImageView? = null
    private var ivSave: ImageView? = null
    private var ivClose: ImageView? = null
    private var ivPlay: ImageView? = null
    private var topContainer: View? = null
    private var bottomContainer: View? = null
    private var albumProgressView: ProgressTextView? = null
    private var layoutManager: CustomLinearLayoutManager? = null

    object Data {

        var models: List<ChatBody>? = null
        var index = 0
        var autoPlayID = -1L
        fun reset() {
            index = 0
            autoPlayID = -1L
        }

        fun getItem(): ChatBody {
            return models!![index]
        }

        fun getUrl(): String {
            return BaseHost.STATIC_HOST + getItem().url
        }

        fun setData(models: List<ChatBody>?, index: Int) {
            this.models = models
            this.index = index
            if (models!![index].msgType == 3) {
                this.autoPlayID = models[index]._ID
            }
        }
    }

    private fun setData(data: List<ChatBody>?) {
        if (ObjectUtils.isNotEmpty(data)) {
            mAdapter!!.setNewData(data)
            PagerSnapHelper().attachToRecyclerView(recyclerView)
            layoutManager!!.scrollToPosition(Data.index)
            showUI(data!![Data.index])
        }
    }

    private val scrollChangeListener = object : RecyclerView.OnScrollListener() {
        var mScrolled = false
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE && mScrolled) {
                val pos = getPosition()
                if (pos != Data.index) {
                    val item = mAdapter!!.getItem(pos)
                    showUI(item)
                    Data.index = pos
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (dx != 0 || dy != 0) {
                mScrolled = true
            }
        }
    }

    private fun showUI(item: ChatBody?) {
        if (item!!.msgType == 3) {
            ivSave!!.visibility = View.GONE
        } else {
            ivSave!!.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.im_preview_close -> {
                finish()
            }
            R.id.im_preview_play -> {
                val position = getPosition()
                if (mAdapter!!.getItem(position)!!.msgType == 3) {
                    val child = layoutManager!!.findViewByPosition(position)
                    if (child != null) {
                        val videoView = child!!.findViewById<PreviewVideoView>(R.id.im_item_video)
                        videoView.startButton.performClick()
                        LogUtils.d("currentState:${videoView.currentState}")
                    }
                }
            }
            R.id.im_preview_more -> {

            }
            R.id.im_preview_save -> {
            }
        }
    }


    override fun initView() {
        recyclerView = findViewById(R.id.im_recycler_preview)
        ivMore = findViewById(R.id.im_preview_more)
        ivSave = findViewById(R.id.im_preview_save)
        ivClose = findViewById(R.id.im_preview_close)
        ivPlay = findViewById(R.id.im_preview_play)
        topContainer = findViewById(R.id.im_preview_top_container)
        bottomContainer = findViewById(R.id.im_preview_bottom_container)
        albumProgressView = findViewById(R.id.im_progress_view)
        layoutManager = CustomLinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        mAdapter = PreviewAdapter(TranslationAnimator.Action.get().getTouchListener())
        recyclerView!!.adapter = mAdapter
        recyclerView!!.addOnScrollListener(scrollChangeListener)
        ivPlay!!.setOnClickListener(this)
        ivClose!!.setOnClickListener(this)
        ivSave!!.setOnClickListener(this)
        ivMore!!.setOnClickListener(this)

        albumProgressView!!.setOnProgressChangeListener(object : ProgressTextView.onProgressChangeListener {
            override fun change(newProgress: Int) {
            }

            override fun end(newProgress: Int) {
                JCMediaManager.instance().mediaPlayer.seekTo(newProgress * 1000)
                showContainers()
            }
        })
        try {
            setData(Data.models)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        on<PreviewVideoView.UpdateProgressBean>(PreviewVideoView.UPDATE_PROGRESS_BEAN, Consumer {
            albumProgressView!!.setProgressAndMaxLong(it.progress, it.max)
        })
        on<Boolean>(PreviewVideoView.CLICK_SCREEN, Consumer {
            if (showContainer) {
                recyclerView!!.removeCallbacks(hideView)
                recyclerView!!.postDelayed(hideView, 0)
            } else {
                showContainer = true
                showContainers()
            }
        })
        TranslationAnimator.Action.get().setTouchCallBack(object : TranslationAnimator.InterceptTouchCallBack {
            override fun reqInterceptTouch(isInterceptTouch: Boolean) {
                layoutManager!!.setScrollHorEnabled(isInterceptTouch)
            }
        })
        TranslationAnimator.Action.get().init(this, im_preview_root, im_act_preview_content, false)
    }

    private fun showContainers() {
        topContainer!!.visibility = View.VISIBLE
        bottomContainer!!.visibility = View.VISIBLE
        recyclerView!!.removeCallbacks(hideView)
        recyclerView!!.postDelayed(hideView, 5000)
    }

    private var showContainer = false
    private val hideView = Runnable {
        showContainer = false
        topContainer!!.visibility = View.GONE
        bottomContainer!!.visibility = View.GONE
    }

    private fun getPosition(): Int {
        return layoutManager!!.findFirstVisibleItemPosition()
    }

    private class PreviewAdapter(private val onTOuchListener: View.OnTouchListener) : BaseMultiItemQuickAdapter<ChatBody, BaseViewHolder>(ArrayList()) {
        init {
            addItemType(1, R.layout.im_item_image)
            addItemType(3, R.layout.im_item_video)
        }

        override fun getDefItemViewType(position: Int): Int {
            val item = mData[position]
            return if (item is ChatBody) {
                item.msgType
            } else -0xff
        }

        override fun convert(helper: BaseViewHolder, item: ChatBody) {
            val url = if (ObjectUtils.isEmpty(item.localPath) ||
                    !FileUtils.isFileExists(item.localPath)) {
                BaseHost.STATIC_HOST + item.url
            } else {
                item.localPath
            }
            if (item.msgType == 3) {
                val videoView = helper.getView<PreviewVideoView>(R.id.im_item_video)
                ImageLoadUtils.display(videoView.ivCover, url)
                videoView.model = item
                if (item._ID == Data.autoPlayID) {
                    videoView.startButton.performClick()
                    Data.autoPlayID = -1
                }
            } else {
                val iv = helper.getView<PhotoImageView>(R.id.im_item_image)
                iv.setTouchListener(onTOuchListener)
                iv.attacher.setOnViewTapListener { _, _, _ ->
                    //退出界面
                    BaseApplication.getAppContext().activityControl.currentActivity.finish()
                }
                ImageLoadUtils.display(helper.getView(R.id.im_item_image), url)
            }
        }
    }

    override fun onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return
        }
        TranslationAnimator.Action.url = BaseHost.STATIC_HOST + Data.models!![getPosition()].url
        TranslationAnimator.Action.get().exitAnimator()
    }

    override fun onPause() {
        super.onPause()
        JCVideoPlayer.releaseAllVideos()
    }

    override fun onDestroy() {
        super.onDestroy()
        PreviewActivity.Data.reset()
    }

}
