package com.wuzuqing.component_im.common.utils

import com.wuzuqing.component_im.common.packets.ChatBody

object F {
    fun find(data: MutableList<ChatBody>, chatBody: ChatBody): Int {
        var index = -1
        for (i in data.indices.reversed()) {
            if (chatBody.id == data[i].id) {
                index = i
                break
            }
        }
        if (index != -1) {
            data[index] = chatBody
        }
        return index
    }
}
