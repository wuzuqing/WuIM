package wuzuqing.com.module_im.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.wuzuqing.component_base.rxbus.RxManager
import com.wuzuqing.component_base.util.SPUtils
import com.wuzuqing.component_data.d_arouter.RxTag
import com.wuzuqing.component_im.common.utils.TcpManager
import com.wuzuqing.component_im.db.DbCore
import wuzuqing.com.module_im.activity.ChatActivity

class TcpService : Service() {

    var rxManager: RxManager? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onCreate() {
        super.onCreate()
        DbCore.init(applicationContext, String.format("%d_im.db", SPUtils.getInt("userId")))
        TcpManager.get().setChatClass(ChatActivity::class.java)
        TcpManager.get().init()
        rxManager = RxManager()
        rxManager!!.on<Boolean>(RxTag.NET_STATE_CHANGE) {
            if (it) {
                TcpManager.get().connect()
            } else {
                TcpManager.get().disconnect(false)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        TcpManager.get().connect()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        TcpManager.get().disconnect(true)
        if (rxManager!=null){
            rxManager!!.clear()
            rxManager = null
        }
    }
}
