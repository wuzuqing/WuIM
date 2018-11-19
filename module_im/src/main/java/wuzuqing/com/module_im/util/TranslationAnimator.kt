package wuzuqing.com.module_im.util

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.wuzuqing.component_base.util.ImageLoadUtils
import com.wuzuqing.component_base.util.ObjectUtils
import wuzuqing.com.module_im.activity.PreviewActivity
import java.util.*


class TranslationAnimator {


    object Action {
        @SuppressLint("StaticFieldLeak")
        private val instance = TranslationAnimator()
        var viewWidth: Int = 0
        var viewHeight: Int = 0
        var scaleX: Float = 0F
        var scaleY: Float = 0F
        var drawable: Drawable? = null
        var locationOnScreen = IntArray(2)
        var url: String? = null
        fun get(): TranslationAnimator {
            return instance
        }

        fun getData(size: Int): List<String> {
            val result = ArrayList<String>()
            for (i in 0 until size) {
                result.add(i.toString())
            }
            return result
        }

        private fun initScale(context: Context, width: Int, height: Int) {
            val screenSize = getScreenWidth(context)
            viewWidth = width
            viewHeight = height
            scaleX = screenSize[0] * 1f / viewWidth
            scaleY = screenSize[1] * 1f / viewHeight
        }

        /**
         * 获取状态栏高度
         *
         * @param context context
         * @return 状态栏高度
         */
        fun getStatusBarHeight(context: Context): Int {
            // 获得状态栏高度
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            return context.resources.getDimensionPixelSize(resourceId)
        }

        fun getScreenWidth(context: Context): IntArray {
            val outMetrics = context.resources.displayMetrics
            return intArrayOf(outMetrics.widthPixels, outMetrics.heightPixels)
        }

        fun startAction(context: Activity, clz: Class<*>, view: View, url: String?) {
            view.getLocationOnScreen(locationOnScreen)
            initScale(context, view.width, view.height)
            Action.url = url
            context.startActivity(Intent(context, clz))
            context.overridePendingTransition(0, 0)
        }

        fun startAction(context: Activity, clz: Class<*>, view: View, db: Drawable?) {
            view.getLocationOnScreen(locationOnScreen)
            initScale(context, view.width, view.height)
            Action.drawable = db
            context.startActivity(Intent(context, clz))
            context.overridePendingTransition(0, 0)
        }

        fun createImageView(context: Context, parent: ViewGroup,
                            left: Float, top: Float, width: Int, height: Int, loadImg: Boolean): ImageView {
            val iv = ImageView(context)
            iv.scaleType = ImageView.ScaleType.CENTER_CROP
            val params = ViewGroup.LayoutParams(width, height)
            iv.x = left
            iv.y = top
            if (loadImg) {
                if (drawable != null) {
                    iv.setImageDrawable(drawable)
                    drawable = null
                } else if (ObjectUtils.isNotEmpty(Action.url)) {
                    ImageLoadUtils.display(iv, Action.url)
                }
            }
            parent.addView(iv, params)
            return iv
        }

        fun sortBitmap(root: View): Bitmap {
            val bm = Bitmap.createBitmap(root.width, root.height,
                    Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bm)
            root.draw(canvas)
            return bm
        }

    }

    private var context: Activity? = null    //当前活动的对象
    private var parent: ViewGroup? = null   //父容器
    private var contentView: View? = null //呈现内容的view


    fun init(activity: Activity, parent: ViewGroup, contentView: View, isSetTouch: Boolean) {
        this.context = activity
        this.parent = parent
        this.contentView = contentView
        if (isSetTouch) {
            parent.setOnTouchListener(onTouchListener)
        }
        parent.viewTreeObserver.addOnGlobalLayoutListener(listener)
    }


    private var scView: ImageView? = null //缩放的view
    private val maxDic = 300
    private val maxScale = 0.4f
    private var currentScale = 1f
    private var distanceY = 0F
    private var downY = 0f
    private var isScale = false
    private var alpha = 255

    private val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            scView = Action.createImageView(context!!, parent!!, Action.locationOnScreen[0].toFloat(),
                    Action.locationOnScreen[1].toFloat(), Action.viewWidth, Action.viewHeight, false)
            parent!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
            ImageLoadUtils.displayChatImg(scView!!, Action.url, object : SimpleTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    scView!!.setImageDrawable(resource)
                    enterAnimator()
                }
            })
        }
    }

    private val onTouchListener = View.OnTouchListener { _, event ->

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downY = event.y
            }
            MotionEvent.ACTION_UP -> {
                if (isScale) {
                    if (touchCallBack != null) touchCallBack!!.reqInterceptTouch(true)
                    if (currentScale < 1) {
                        if (currentScale > 1 - maxScale) { //恢复
                            scaleAnimator(currentScale, 1f)
                        } else { //退出
                            Action.url = PreviewActivity.Data.getUrl()
                            exitAnimator()
                        }
                    }
                }
                isScale = false
            }
            MotionEvent.ACTION_MOVE -> {
                distanceY = event.y - downY
                if (Math.abs(distanceY) > 10 && !isScale) {
                    isScale = true
                    if (touchCallBack != null) touchCallBack!!.reqInterceptTouch(false)
                }
                if (isScale) {
                    currentScale = when {
                        downY > event.y -> 1f
                        distanceY <= maxDic -> 1 - distanceY * maxScale / maxDic
                        else -> 1 - maxScale
                    }
                    contentView!!.scaleX = currentScale
                    contentView!!.scaleY = currentScale
                    alpha = (255 * currentScale).toInt()
                    setBg()
                }
            }
        }
        true
    }

    fun getTouchListener(): View.OnTouchListener {
        return onTouchListener
    }

    private var touchCallBack: InterceptTouchCallBack? = null

    fun setTouchCallBack(touchCallBack: InterceptTouchCallBack) {
        this.touchCallBack = touchCallBack
    }

    interface InterceptTouchCallBack {
        fun reqInterceptTouch(isInterceptTouch: Boolean)
    }

    private val onUpdateListener = ValueAnimator.AnimatorUpdateListener {
        alpha = (255 * (it.animatedValue as Float)).toInt()
        setBg()
    }

    private fun setBg() {
        parent!!.setBackgroundColor(Color.argb(alpha, 0, 0, 0))
    }


    private fun scaleAnimator(startScale: Float, endScale: Float) {
        val animatorSet = AnimatorSet()
        animatorSet.duration = 300
        val scaleXAnimator = ObjectAnimator.ofFloat(contentView!!, "scaleX", startScale, endScale)
        scaleXAnimator.addUpdateListener(onUpdateListener)
        animatorSet.interpolator = LinearInterpolator()
        animatorSet.playTogether(
                scaleXAnimator,
                ObjectAnimator.ofFloat(contentView!!, "scaleY", startScale, endScale)
        )
        animatorSet.start()
    }

    fun exitAnimator() {
        val location = Action.locationOnScreen
        val oriWidth = Action.viewWidth
        val oriHeight = Action.viewHeight
        val nowWidth = (contentView!!.width * currentScale).toInt()
        val nowHeight = (contentView!!.height * currentScale).toInt()

        scView = Action.createImageView(context!!, parent!!, (contentView!!.width - nowWidth) / 2F,
                (contentView!!.height - nowHeight) / 2F, nowWidth, nowHeight, true)

        val size = Action.getScreenWidth(context!!)
        val startX = scView!!.x
        val startY = scView!!.y
        var endX = startX
        var endY = startY
        val halfWidth = size[0] / 2F
        val halfHeight = size[1] / 2F

        if (halfWidth != (oriWidth / 2 + startX)) { //中心点
            endX = startX + (location[0] - (halfWidth - oriWidth / 2))
        }
        if (halfHeight != (oriHeight / 2 + startY)) {
            endY = startY + (location[1] - (halfHeight - oriHeight / 2)) - Action.getStatusBarHeight(context!!)
        }

        animator(scView!!, startX, endX, startY, endY, 1F,
                oriWidth * 1F / nowWidth, 1F, oriHeight * 1F / nowHeight, true)
    }

    private fun enterAnimator() {
        val size = Action.getScreenWidth(context!!)
        val oriWidth = Action.viewWidth
        val oriHeight = Action.viewHeight
        val location = Action.locationOnScreen
        val startX = location[0].toFloat()
        val startY = location[1].toFloat()
        var endX = startX
        var endY = startY
        val halfWidth = size[0] / 2F
        val halfHeight = size[1] / 2F
        if (halfWidth != (oriWidth / 2 + startX)) {
            endX = (halfWidth - oriWidth / 2)
        }
        if (halfHeight != (oriHeight / 2 + startY)) {
            endY = (halfHeight - oriHeight / 2)
        }
        animator(scView!!, startX, endX, startY, endY, 1F, Action.scaleX, 1F, Action.scaleY, false)
    }

    private fun animator(target: View, startX: Float, endX: Float, startY: Float, endY: Float,
                         startSX: Float, endSX: Float, startSY: Float, endSY: Float, isFinish: Boolean) {
        val animatorSet = AnimatorSet()
        val scaleXAnimator = ObjectAnimator.ofFloat(target, "scaleY", startSY, endSY)
        if (isFinish) {
            parent!!.setBackgroundColor(Color.TRANSPARENT)
        }
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", startSX, endSX),
                scaleXAnimator
                , ObjectAnimator.ofFloat(target, "translationX", startX, endX)
                , ObjectAnimator.ofFloat(target, "translationY", startY, endY)
        )

        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                if (isFinish) {
                    if (context != null) {
                        context!!.finish()
                        context!!.overridePendingTransition(0, 0)
                    }
                    reset()
                } else {
                    contentView!!.visibility = View.VISIBLE
                    contentView!!.postDelayed({
                        parent!!.removeView(scView)
                        Action.drawable = null
                    }, 100)
                }
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
                contentView!!.visibility = View.GONE
            }

        })
        animatorSet.duration = 800
        animatorSet.start()
    }

    private fun reset() {
        context = null
        parent = null
        scView = null
        contentView = parent
        currentScale = 1f
        alpha = 255
    }


}
