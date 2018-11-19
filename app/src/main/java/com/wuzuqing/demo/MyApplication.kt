package com.wuzuqing.demo


import android.widget.ImageView
import com.wuzuqing.component_base.constants.BaseApplication
import com.wuzuqing.component_base.util.ImageLoadUtils
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.AlbumLoader
import java.util.*


/**
 * @Created by TOME .
 * @时间 2018/5/14 17:40
 * @描述 ${应用的application}
 */

class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        Album.initialize(AlbumConfig.newBuilder(this)
                .setAlbumLoader(object : AlbumLoader {
                    override fun load(imageView: ImageView?, albumFile: AlbumFile?) {
                        load(imageView, albumFile!!.path)
                    }

                    override fun load(imageView: ImageView?, url: String?) {
                        ImageLoadUtils.display(imageView, url)
                    }
                })
                .setLocale(Locale.getDefault())
                .build())

    }

    companion object {

        var instance: MyApplication? = null
            private set
    }
}
