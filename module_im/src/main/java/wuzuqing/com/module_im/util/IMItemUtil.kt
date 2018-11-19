package wuzuqing.com.module_im.util

import com.wuzuqing.component_base.util.ObjectUtils
import java.util.*

object IMItemUtil {

    fun parseAvatar(chatType: Int, avatar: String?): List<String?> {
        var avatars: MutableList<String?> = ArrayList()
        when (chatType) {
            1 -> if (ObjectUtils.isNotEmpty(avatar)) {
                val split = avatar!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                avatars = Arrays.asList(*split)
            }
            2 -> avatars.add(avatar)
        }
        return avatars
    }

    fun parseContent(msgType: Int, content: String): String {
        if (ObjectUtils.isEmpty(content)) return ""
        when (msgType) {
            0 -> return content
            1 -> {
            }
            2 -> {
            }
            3 -> {
            }
            4 -> {
            }
        }

        return content
    }
}
