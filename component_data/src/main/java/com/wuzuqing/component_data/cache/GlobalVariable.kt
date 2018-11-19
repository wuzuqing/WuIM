package com.wuzuqing.component_data.cache

import com.wuzuqing.component_data.bean.GroupMemberBean
import com.wuzuqing.component_data.bean.UserInfoBean

class GlobalVariable {

    var user: UserInfoBean? = null //自己
    var targetId = ""
    var targetNick: String? = ""
    var targetAvatar: String? = ""
    var groupMembers: Map<Int, GroupMemberBean>? = null

    val userId: Int
        get() = if (user == null) 0 else user!!.id

    val targetIdInt: Int
        get() = targetId.toInt()

    companion object {
        private val globalVariable = GlobalVariable()

        fun get(): GlobalVariable {
            return globalVariable
        }

        fun toChatParams(targetId: Int, nick: String?, avatar: String?) {
            get().targetId = targetId.toString()
            get().targetNick = nick
            get().targetAvatar = avatar
        }

        fun checkChat(from: Int, to: Int): Boolean {
            return (from == get().userId && to == get().targetIdInt) || (to == get().userId && from == get().targetIdInt)
        }

        fun checkChat(from: String?, to: String?, chatType: Int, fromGroupId: String?, toGroupId: String?, isPrivate: Boolean): Boolean {
            return if (isPrivate && chatType == 2 && to!!.isNotEmpty()) {
                checkChat(from!!.toInt(), to.toInt())
            } else {
                !isPrivate && chatType == 1 && fromGroupId == toGroupId
            }
        }
    }
}
