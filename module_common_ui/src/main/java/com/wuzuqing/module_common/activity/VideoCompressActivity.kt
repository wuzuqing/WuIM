package com.example.tome.module_common.activity

import android.app.Activity
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.bumptech.glide.Glide
import com.wuzuqing.component_base.base.mvc.BaseVcActivity
import com.wuzuqing.component_base.util.FileUtils
import com.wuzuqing.component_base.util.LogUtils
import com.wuzuqing.component_base.util.ToastUtils
import com.wuzuqing.component_data.d_arouter.IntentKV
import com.wuzuqing.module_common.R
import com.wuzuqing.module_common.utils.GetPathByUri
import com.yovenny.videocompress.MediaController
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_video_compress.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class VideoCompressActivity : BaseVcActivity(), View.OnClickListener, OnItemClickListener {
    override fun getLayout(): Int {
        return R.layout.activity_video_compress
    }

    //    @BindView(R2.id.videoplayer)
    //    JCVideoPlayerStandard mVideoplayer;
    //    @BindView(R2.id.delete_iv)
    //    ImageView mDeleteIv;
    //    @BindView(R2.id.layout_videoplayer)
    //    RelativeLayout mLayoutVideoplayer;
    //    @BindView(R2.id.tv_size)
    //    TextView mTvSize;
    //    @BindView(R2.id.tv_compress_size)
    //    TextView mTvCompressSize;
    //    @BindView(R2.id.tv_add_video)
    //    TextView mTvAddVideo;
    //    @BindView(R2.id.tv_compress_video)
    //    TextView mTvCompressVideo;

    private var mIntent: Intent? = null
    private var mvideoUri: Uri? = null
    private var mTmpFile: File? = null
    private val mMediasPlayFlag = false
    private var mMediasUrl: String? = null
    private var disposable1: Disposable? = null
    private var mOutPath: String? = null

    private val mCropUri: Uri? = null
    private val mOriginalFile: File? = null



    override fun initView() {
        delete_iv.setOnClickListener(this)
        tv_add_video.setOnClickListener(this)
        tv_compress_video.setOnClickListener(this)
        //设置宽高
        val dm = resources.displayMetrics
        val screenWidth = dm.widthPixels // 屏幕宽（像素，如：3200px）
        val paramjc = videoplayer.getLayoutParams()
        paramjc.width = screenWidth
        paramjc.height = screenWidth
        //LogUtils.d("长度:" + screenWidth);

        videoplayer.setLayoutParams(paramjc)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.delete_iv) {

        } else if (v.id == R.id.tv_add_video) {
            AlertView(null, null, "取消", null,
                    arrayOf("录制视频", "从相册选择"),
                    this, AlertView.Style.ActionSheet, this).show()

        } else if (v.id == R.id.tv_compress_video) {
            //视频压缩
            videoCompres()
        }
    }


    override fun onItemClick(o: Any, position: Int) {
        if (position == 0) {
            //6.0权限问题
            //录制视频
            recordVideo()
        } else if (position == 1) {

            //先更新媒体库，发送广播，系统接收到广播就去扫描媒体库
            val localUri = Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/avatar/")
            // LogUtils.d("file://"+mediaFile+ File.separator+file);
            val localIntent = Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE")
            localIntent.data = localUri
            sendBroadcast(localIntent)

            //从相册选择视频
            openVideo()

        }
    }

    //录制视频
    private fun recordVideo() {
        try {
            // 开启相机录像应用程序获取并返回录像文件
            mIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            // Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
            //创建文件
            FileUtils.createOrExistsDir(Environment.getExternalStorageDirectory().toString() + "avatar")
            // 獲取拍照的圖片的uri
            mvideoUri = FileUtils.getImageUri(Environment.getExternalStorageDirectory().toString() + "avatar", "235.MP4")

            mIntent!!.putExtra(MediaStore.EXTRA_OUTPUT, mvideoUri)                //指定要保存的位置。
            //captureImageCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, );            //设置拍摄的质量
            mIntent!!.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10)            //限制持续时长,单位秒


            // 指明存储图片或视频的地址URI
            // mIntent.putExtra(MediaStore.EXTRA_OUTPUT, mvideoUri);
            startActivityForResult(mIntent, IntentKV.FLAG_IMAGE_video)
        } catch (e: Exception) {
            Toast.makeText(this@VideoCompressActivity, "相机无法启动，请先开启相机权限", Toast.LENGTH_LONG).show()
        }

    }

    //打开手机视频
    private fun openVideo() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "video/*" //String VIDEO_UNSPECIFIED = "video/*";
        val wrapperIntent = Intent.createChooser(intent, null)
        startActivityForResult(wrapperIntent, IntentKV.FLAG_IMAGE_video2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (IntentKV.FLAG_IMAGE_video == requestCode && resultCode == Activity.RESULT_OK) {
            //相册视频
            val data1 = data!!.data
            LogUtils.d("返回路径:" + data1 + "," + data1!!.toString().length)

            try {
                val videoAsset = contentResolver.openAssetFileDescriptor(data.data!!, "r")
                val fis = videoAsset!!.createInputStream()

                mTmpFile = File(Environment.getExternalStorageDirectory(), "/avatar/" + "VideoFile.mp4")
                FileUtils.createFileByDeleteOldFile(mTmpFile)
                val fos = FileOutputStream(mTmpFile!!)

                val buf = ByteArray(1024)
                var len = 0
                while (true) {
                    fis.read(buf)
                    if (len <= 0) break
                    fos.write(buf, 0, len)
                }
                fis.close()
                fos.close()
            } catch (io_e: IOException) {
                // TODO: handle error
            }

            LogUtils.d("录像返回:" + mTmpFile + "," + mTmpFile!!.length() + "," + mTmpFile)

            // Uri parseUri = Uri.parse("content:/"+mTmpFile.toString());
            // Uri parseUri = Uri.fromFile("content:/"+mTmpFile.toString());
            //File file = new File("content:/"+mTmpFile.toString());
            val parseUri = Uri.fromFile(mTmpFile)

            // Uri parseUri = getImageContentUri(this, mTmpFile);

            //获取选中的视频uri路径(4.4以后的版本)
            val vidioUri2 = GetPathByUri.getPathByUri4kitkat(this, parseUri)

            //返回的是路径:/storage/emulated/0/avatar/VideoFile.mp4 ,file:///storage/emulated/0/avatar/VideoFile.mp4
            LogUtils.d("返回路径uri:$vidioUri2,$parseUri")
            //LogUtils.d("返回路径uri:" + "," + parseUri);

            //播放视频
            mediasPlayer(vidioUri2)

        } else if (IntentKV.FLAG_IMAGE_video2 == requestCode) {
            //打开录制视频
            if (data == null) {
                ToastUtils.showShort(this@VideoCompressActivity, "相冊视频內容為空")
            } else {
                val uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                LogUtils.d("uri:$uri2")

                // cropPic(data.getData());//裁剪圖片,获得的视频路径:content://com.android.providers.media.documents/document/video%3A65973
                LogUtils.d("获取视频:" + data.data!!)
                val uri = data.data

                //获取选中的视频uri路径(4.4以后的版本),返回:/storage/emulated/0/DCIM/Camera/V009116.mp4
                val vidioUri = GetPathByUri.getPathByUri4kitkat(this, uri)
                LogUtils.d("录像返回2:" + vidioUri!!)
                //播放视频
                mediasPlayer(vidioUri)
            }
        }
    }

    //视频播放
    private fun mediasPlayer(medias: String?) {
        LogUtils.d("视频长度:" + FileUtils.getFileLength(medias) + "")
        tv_size.setText("视频大小(压缩前):" + FileUtils.getFileLength(medias) / 1024 / 1024 + "M")

        mMediasUrl = medias
        if (mMediasPlayFlag) {

            val retriever = MediaMetadataRetriever()
            try {
                //根据url获取缩略图
                retriever.setDataSource(medias, HashMap())
                //获得第一帧图片
                val bitmap = retriever.frameAtTime
                videoplayer.thumbImageView.setImageBitmap(bitmap)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } finally {
                retriever.release()
            }

            /* String thumb = mProductItemData.getThumb();
            //设置图片
            Picasso.with(this)
                    .load(thumb)  //视频截图
                    .placeholder(R.mipmap.details_icon_play)
                    .into(mVideoplayer.thumbImageView);*/
            sName = "测试视频"

        } else {
            //本地视频的uri
            mMediasUrl = medias

            if (sThumbPath != null) {

                val file = File(sThumbPath)
                //设置图片
                Glide.with(this)
                        .load(file)  //视频截图
                        //.placeholder(R.mipmap.details_icon_play)
                        .into(videoplayer.thumbImageView)
            } else {
                //直接录制的视频
                val mmr = MediaMetadataRetriever()
                mmr.setDataSource(medias)
                val bitmap = mmr.frameAtTime//获取第一帧图片
                videoplayer.thumbImageView.setImageBitmap(bitmap)
                LogUtils.d("本地录制视频的第一帧:" + bitmap.toString())
                //mVideoplayer.thumbImageView.setImageResource(R.mipmap.details_icon_play);
            }
        }
        if (sName == null) {
            sName = "VideoFile.mp4"
        }

        LogUtils.d("播放视频路径:" + medias!!)

        // mVideoplayer.setUp("/storage/emulated/0/DCIM/Camera/V009116.mp4",JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"nihao");
        // mVideoplayer.setUp(medias, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, sName);
        videoplayer.setUp(medias, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, sName)
        //文件的路径转为bitmap
        //Bitmap bitmap = ImageUtils.getBitmap(sThumbPath, 1080, 1080);

    }

    //视频压缩
    private fun videoCompress(reqCallBack: ReqCallBack) {
        // mMediasUrl
        if (mMediasUrl != null) {

            val size = (1024 * 1024 * 2).toLong()
            LogUtils.d("视频长度:" + FileUtils.getFileLength(mMediasUrl) + "")
            if (FileUtils.getFileLength(mMediasUrl) > size) {

                var number = 1245
                number++

                mOutPath = Environment.getExternalStorageDirectory().toString() + "/avatar/" + "V" + number + ".mp4"

                //视频的名称
                sName = "V$number.mp4"
                disposable1 = Observable.create(ObservableOnSubscribe<Boolean> { e ->
                    val success = MediaController
                            .getInstance()
                            .convertVideo(mMediasUrl,
                                    mOutPath)

                    LogUtils.d("压缩状态:$success")
                    e.onNext(success)
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { s ->
                            // ViewHelper.getInstance().toastCenter(mActivity, s ? "压缩成功" : "压缩失败");
//                            if (s == "压缩成功") {
//                             LogUtils.d("压缩成功")
//                            } else if (s == "压缩失败") {
//                             LogUtils.d("压缩失败")
//                            } else {
//                             LogUtils.d("压缩")
//                            }
                            LogUtils.d("视频长度2:" + FileUtils.getFileLength(mOutPath) + "")


                            /* Message msg = new Message();
                                msg.what = 100;
                                msg.obj = mOutPath ;
                                mHandler.sendMessage(msg);*/
                            reqCallBack.reqCompress(mOutPath)

                            //取消进度条
                            //mDialog1.cancel();
                        }


            } else {
                //Toast.makeText(this, "视频大小超出限制", Toast.LENGTH_SHORT).show();
                LogUtils.d("不压缩")

                reqCallBack.reqNoCompress()

            }

        }
    }

    //定义接口
    interface ReqCallBack {

        fun reqCompress(string: String?)

        fun reqNoCompress()

    }

    private fun videoCompres() {

        if (mMediasUrl != null) {
            //压缩视频
            videoCompress(object : ReqCallBack {       //压缩
                override fun reqCompress(string: String?) {
                    //获取压缩后的视频路径
                    val file = File(string!!)

                    LogUtils.d("视频大小(压缩后):" + file.length() / 1024 / 1024 + "M")
                    tv_compress_size.setText("视频大小(压缩后):" + file.length() / 1024 / 1024 + "M")

                }

                override fun reqNoCompress() {       //不压缩

                    val file = File(mMediasUrl!!)
                    LogUtils.d("视频大小(不压缩):" + file.length() / 1024 / 1024 + "M")

                }
            })

        } else {
            ToastUtils.show(this, "请选选择视频", 2)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mMediasUrl = null
        //mVideoplayer.release(); // 释放视频
    }

    companion object {
        private val sThumbPath: String? = null
        private var sName: String? = null
    }
}
