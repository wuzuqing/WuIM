package wuzuqing.com.module_im.fragment

import wuzuqing.com.module_im.R

enum class SelectItems private constructor( val id: Int,  val title: String,  val icon: Int) {
    PHOTO(0, "相册", R.mipmap.ic_func_pic),
    TAKES(1, "拍摄", R.mipmap.ic_func_shot),
    LOCATION(2, "位置", R.mipmap.ic_func_location),
    PICKET(3, "红包", R.mipmap.ic_func_red_pack),
    ID_CARD(4, "名片", R.mipmap.ic_func_business_card),
    COLLECT(5, "我的收藏", R.mipmap.ic_func_collectioin),
    MEDIA_CALL(6, "语音通话", R.mipmap.ic_func_video),
    TRANSFER(7, "转账", R.mipmap.ic_func_transfer)
}
