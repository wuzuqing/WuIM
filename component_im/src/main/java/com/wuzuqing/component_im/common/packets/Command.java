package com.wuzuqing.component_im.common.packets;



public enum Command {
    /**
     * <code>COMMAND_UNKNOW = 0;</code>
     */
    COMMAND_UNKNOW(0),
    /**
     * <pre>
     * 握手请求，含http的websocket握手请求
     * </pre>
     *
     * <code>COMMAND_HANDSHAKE_REQ = 1;</code>
     */
    COMMAND_HANDSHAKE_REQ(919),
    /**
     * <pre>
     * 握手响应，含http的websocket握手响应
     * </pre>
     *
     * <code>COMMAND_HANDSHAKE_RESP = 2;</code>
     */
    COMMAND_HANDSHAKE_RESP(998),
    /**
     * <pre>
     * 鉴权请求
     * </pre>
     *
     * <code>COMMAND_AUTH_REQ = 3;</code>
     */
    COMMAND_AUTH_REQ(997),
    /**
     * <pre>
     * 鉴权响应
     * </pre>
     *
     * <code>COMMAND_AUTH_RESP = 4;</code>
     */
    COMMAND_AUTH_RESP(996),
    /**
     * <pre>
     * 登录请求
     * </pre>
     *
     * <code>COMMAND_LOGIN_REQ = 5;</code>
     */
    COMMAND_LOGIN_REQ(995),
    /**
     * <pre>
     * 登录响应
     * </pre>
     *
     * <code>COMMAND_LOGIN_RESP = 6;</code>
     */
    COMMAND_LOGIN_RESP(994),
    /**
     * <pre>
     * 申请进入群组
     * </pre>
     *
     * <code>COMMAND_JOIN_GROUP_REQ = 7;</code>
     */
    COMMAND_JOIN_GROUP_REQ(993),
    /**
     * <pre>
     * 申请进入群组响应
     * </pre>
     *
     * <code>COMMAND_JOIN_GROUP_RESP = 8;</code>
     */
    COMMAND_JOIN_GROUP_RESP(992),
    /**
     * <pre>
     * 进入群组通知
     * </pre>
     *
     * <code>COMMAND_JOIN_GROUP_NOTIFY_RESP = 9;</code>
     */
    COMMAND_JOIN_GROUP_NOTIFY_RESP(991),
    /**
     * <pre>
     * 退出群组通知
     * </pre>
     *
     * <code>COMMAND_EXIT_GROUP_NOTIFY_RESP = 10;</code>
     */
    COMMAND_EXIT_GROUP_NOTIFY_RESP(990),

    /**
     * <pre>
     * 关闭请求
     * </pre>
     *
     * <code>COMMAND_CLOSE_REQ = 14;</code>
     */
    COMMAND_CLOSE_REQ(989),
    /**
     * <pre>
     * 发出撤消消息指令(管理员可以撤消所有人的消息，自己可以撤消自己的消息)
     * </pre>
     *
     * <code>COMMAND_CANCEL_MSG_REQ = 15;</code>
     */
    COMMAND_CANCEL_MSG_REQ(988),
    /**
     * <pre>
     * 收到撤消消息指令
     * </pre>
     *
     * <code>COMMAND_CANCEL_MSG_RESP = 16;</code>
     */
    COMMAND_CANCEL_MSG_RESP(987),
    /**
     * <pre>
     * 获取用户信息;
     * </pre>
     *
     * <code>COMMAND_GET_USER_REQ = 17;</code>
     */
    COMMAND_GET_USER_REQ(986),
    /**
     * <pre>
     * 获取用户信息响应;
     * </pre>
     *
     * <code>COMMAND_GET_USER_RESP = 18;</code>
     */
    COMMAND_GET_USER_RESP(985),
    /**
     * <pre>
     * 获取聊天消息;
     * </pre>
     *
     * <code>COMMAND_GET_MESSAGE_REQ = 19;</code>
     */
    COMMAND_GET_MESSAGE_REQ(984),
    /**
     * <pre>
     * 获取聊天消息响应;
     * </pre>
     *
     * <code>COMMAND_GET_MESSAGE_RESP = 20;</code>
     */
    COMMAND_GET_MESSAGE_RESP(983),
    /**
     * <pre>
     * 获取历史聊天消息请求;
     * </pre>
     * <p>
     * <code>COMMAND_GET_OFFLINE_MESSAGE_RESP = 21;</code>
     */
    COMMAND_GET_OFFLINE_MESSAGE_REQ(982),
    /**
     * <pre>
     * 获取历史聊天消息响应;
     * </pre>
     * <p>
     * <code>COMMAND_GET_OFFLINE_MESSAGE_RESP = 21;</code>
     */
    COMMAND_GET_OFFLINE_MESSAGE_RESP(981),


    /**
     * <pre>
     * 心跳请求
     * </pre>
     *
     * <code>COMMAND_HEARTBEAT_REQ = 213;</code>
     */
    COMMAND_HEARTBEAT_REQ(213),


    /**
     * <pre>
     *  打开直播
     *  {"cmd":"004","code":250,"data":{"roomid":23335,"rtmpurl":"","videotype":1},"msg":""}
     * </pre>
     *
     * <code>COMMAND_LIVE_START = 4;</code>
     */
    COMMAND_LIVE_START(4),

    /**
     * <pre>
     *  关播
     *  {"cmd":"005","code":250,"data":{"rtmpurl":"","videotype":2},"msg":"直播已关闭"
     * </pre>
     *
     * <code>COMMAND_LIVE_CLOSE = 5;</code>
     */
    COMMAND_LIVE_CLOSE(5),

    /**
     * <pre>
     * 聊天请求
     *  {"cmd":"002","code":250,"data":{"content":"喜欢你，宝宝","headimage":"","lv":1,"platform":"IOS","roomid":23334,"sendernickname":"走开、暧昧","whid":2227},"whid":2227}
     * </pre>
     * <code>COMMAND_CHAT_REQ = 11;</code>
     */
    COMMAND_CHAT_REQ(2),
    /**
     * <pre>
     * 消息返回
     * {"cmd":"012","code":252,"data":null,"msg":"发言不能太快"}
     * </pre>
     *
     * <code>COMMAND_CHAT_RESP = 12;</code>
     */
    COMMAND_CHAT_RESP(12),


    /**
     * <pre>
     * 开奖
     * </pre>
     *
     * <code>COMMAND_OPEN_LOTTERY = 801;</code>
     */
    COMMAND_OPEN_LOTTERY(801),

    /**
     * <pre>
     * 未中奖
     * {"cmd":"802","code":250,"data":{"channel":1,"headimage":"","lotterytype":0,"nickname":"慰春病","whid":1949},"msg":"未中奖"}
     * </pre>
     *
     * <code>COMMAND_NO_LOTTERY = 802;</code>
     */
    COMMAND_NO_LOTTERY(802),

    /**
     * <pre>
     * 休息
     * {"cmd":"803","code":250,"data":{"channel":1},"msg":"休息"}
     * </pre>
     *
     * <code>COMMAND_REST = 803;</code>
     */
    COMMAND_REST(803),

    /**
     * <pre>
     *  正在刮奖
     *  {"cmd":"804","code":250,"data":{"channel":1,"headimage":"","lotterytype":0,"nickname":"慰春病","whid":1949},"msg":"正在刮奖"}
     * </pre>
     *
     * <code>COMMAND_OPENING_LOTTERY = 804;</code>
     */
    COMMAND_OPENING_LOTTERY(804),
    /**
     * <pre>
     *  正在刮奖
     * </pre>
     *
     * <code>COMMAND_WAITTING = 805;</code>
     */
    COMMAND_WAITTING(805),

    /**
     * <pre>
     *  进入房间
     *  {"cmd":"008","code":250,"data":{"count":1,"gamescore":0,"isflow":0,"lbscore":2255988,"lv":5,"rname":"彩票2号直播间","roomid":23334,"rtmpurl":"","thumbsup":832428,"videotype":2},"msg":""}
     * </pre>
     *
     * <code>COMMAND_INTO_ROOM_RECEIVE = 8;</code>
     */
    COMMAND_INTO_ROOM_RECEIVE(8),

    /**
     * <pre>
     *  没有登录状态进入房间
     * </pre>
     *
     * <code>COMMAND_GO_INTO_CHAT_ROOM_NOTLOGIN = 9;</code>
     */
    COMMAND_GO_INTO_CHAT_ROOM_NOTLOGIN(9),

    /**
     * <pre>
     *  离开聊天室
     * </pre>
     *
     * <code>COMMAND_EXIT_LIVE_ROOM = 10;</code>
     */
    COMMAND_EXIT_LIVE_ROOM(10),

    /**
     * <pre>
     *  赠送礼物
     * </pre>
     *
     * <code>COMMAND_SEND_GIFT = 13;</code>
     */
    COMMAND_SEND_GIFT(13),


    /**
     * <pre>
     * 发礼物返回
     * </pre>
     *
     * <code>COMMAND_RE_SEND_GIFT = 23;</code>
     */
    COMMAND_RE_SEND_GIFT(23),


    /**
     * <pre>
     * 玩家送礼物的时候礼物信息
     * </pre>
     *
     * <code>COMMAND_ANCHOR_GIFT = 23;</code>
     */
    COMMAND_ANCHOR_GIFT(33),

    /**
     * <pre>
     *  更新聊天室内人数
     *  {"cmd":"014","code":250,"data":{"count":692,"headimage":"","lv":1,"nickname":"转身⑩、告别","platform":"web","whid":2153},"msg":"进入房间","whid":2153}
     * </pre>
     *
     * <code>COMMAND_CHAT_ROOM_INFO = 14;</code>
     */
    COMMAND_CHAT_ROOM_INFO(14),

    /**
     * <pre>
     *  系统消息
     * </pre>
     *
     * <code>COMMAND_SYSTEM_MSG = 20;</code>
     */
    COMMAND_SYSTEM_MSG(20),

    /**
     * <pre>
     *  系统公告
     * </pre>
     *
     * <code>COMMAND_SYSTEM_NOTICE = 501;</code>
     */
    COMMAND_SYSTEM_NOTICE(501),

    /**
     * <pre>
     *  点赞
     *  {"cmd":"022","code":250,"data":{"roomid":23334,"thumbsup":832431},"msg":"点赞","whid":2251}
     * </pre>
     *
     * <code>COMMAND_PRISE_RECEIVE = 22;</code>
     */
    COMMAND_PRISE_RECEIVE(22),

    /**
     * <pre>
     *  合买结果
     * </pre>
     *
     * <code>COMMAND_HE_RESULT = 999;</code>
     */
    COMMAND_HE_RESULT(999),

    /**
     * <pre>
     *  首页进入只看直播
     * </pre>
     *
     * <code>COMMAND_GO_INTO_CHAT_ROOM_AT_HOME = 19;</code>
     */
    COMMAND_GO_INTO_CHAT_ROOM_AT_HOME(19),


    /**
     * <pre>
     *  取消准备
     * </pre>
     *
     * <code>COMMAND_ANCHOR_REDAY_CANCEL = 107;</code>
     */
    COMMAND_ANCHOR_REDAY_CANCEL(107),

    /**
     * <pre>
     *  主播进入房间
     * </pre>
     *
     * <code>COMMAND_ANCHOR_ENTER_ROOM = 108;</code>
     */
    COMMAND_ANCHOR_ENTER_ROOM(108),

    /**
     * <pre>
     *  主播准备开播，服务进行生成推流地址
     *  {"cmd":"109","code":250,"data":{"rtmpurl":"rtmp:","streamname":"lblive1334"},"msg":"准备成功"}
     * </pre>
     *
     * <code>COMMAND_ANCHOR_REDAY_OPEN = 109;</code>
     */
    COMMAND_ANCHOR_REDAY_OPEN(109),

    /**
     * <pre>
     *  主播开播，所有房间收到 _OPEN_VIDEO = "004"; 开播的命令，手机端的准备按钮文本变成“已开播”，开播按钮文本“关播”
     *   {"cmd":"110","code":250,"data":{"roomid":1334,"rtmpurl":"rtmp://pili-live-rtmp.lboog.com/lboog/lblive1334","videotype":1},"msg":"直播已开启"}
     * </pre>
     *
     * <code>COMMAND_ANCHOR_OPEN_VIDEO = 110;</code>
     */
    COMMAND_ANCHOR_OPEN_VIDEO(110),

    /**
     * <pre>
     *  主播关播，所有房间收到 _OPEN_VIDEO = "005"; 关播的命令，手机端的准备按钮文本变成“准备”，关播按钮文本“开播”，开播流程重新操作
     * {"cmd":"111","code":250,"data":{},"msg":"直播已关闭"}
     * </pre>
     *
     * <code>COMMAND_ANCHOR_CLOSE_VIDEO = 111;</code>
     */
    COMMAND_ANCHOR_CLOSE_VIDEO(111),

    /**
     * <pre>
     *  主播直播状态
     * </pre>
     *
     * <code>COMMAND_ANCHOR_STATE = 112;</code>
     */
    COMMAND_ANCHOR_STATE(112),

    /**
     * <pre>
     *  主播被踢出
     * </pre>
     *
     * <code>COMMAND_PICK_ANCHOR = 101;</code>
     */
    COMMAND_PICK_ANCHOR(101),

    /**
     * <pre>
     *  用户控制 踢人 禁言 等,用于web与app传输
     * </pre>
     *
     * <code>COMMAND_USER_CONTROL = 301;</code>
     */
    COMMAND_USER_CONTROL(301),

    /**
     * <pre>
     * 场控禁言
     * </pre>
     *
     * <code>COMMAND_USER_STOP_TALK = 302;</code>
     */
    COMMAND_USER_STOP_TALK(302),

    /**
     * <pre>
     * 场控解禁言
     * </pre>
     *
     * <code>COMMAND_USER_USER_OPEN_TALK = 303;</code>
     */
    COMMAND_USER_USER_OPEN_TALK(303),


    /**
     * <pre>
     * 全场控禁言
     * </pre>
     *
     * <code>COMMAND_ALL_STOP_TALK = 312</code>
     */
    COMMAND_ALL_STOP_TALK(312),

    /**
     * <pre>
     * 全场控解禁言
     * </pre>
     *
     * <code>COMMAND_ALL_OPEN_TALK = 313;</code>
     */
    COMMAND_ALL_OPEN_TALK(313),

    /**
     * <pre>
     * 场控踢人
     * </pre>
     *
     * <code>COMMAND_USER_PICK = 304;</code>
     */
    COMMAND_USER_PICK(304),

    /**
     * <pre>
     * 主播信息
     * </pre>
     *
     * <code>COMMAND_ANCHOR_MSG = 314;</code>
     */
    COMMAND_ANCHOR_MSG(314),;


    public final int getNumber() {
        return value;
    }

    public static Command valueOf(int value) {
        return forNumber(value);
    }

    public static Command forNumber(int value) {
        switch (value) {
            case 0:
                return COMMAND_UNKNOW;
            case 919:
                return COMMAND_HANDSHAKE_REQ;
            case 998:
                return COMMAND_HANDSHAKE_RESP;
            case 997:
                return COMMAND_AUTH_REQ;
            case 996:
                return COMMAND_AUTH_RESP;
            case 995:
                return COMMAND_LOGIN_REQ;
            case 994:
                return COMMAND_LOGIN_RESP;
            case 993:
                return COMMAND_JOIN_GROUP_REQ;
            case 992:
                return COMMAND_JOIN_GROUP_RESP;
            case 991:
                return COMMAND_JOIN_GROUP_NOTIFY_RESP;
            case 990:
                return COMMAND_EXIT_GROUP_NOTIFY_RESP;
            case 2:
                return COMMAND_CHAT_REQ;
            case 12:
                return COMMAND_CHAT_RESP;
            case 213:
                return COMMAND_HEARTBEAT_REQ;
            case 989:
                return COMMAND_CLOSE_REQ;
            case 988:
                return COMMAND_CANCEL_MSG_REQ;
            case 987:
                return COMMAND_CANCEL_MSG_RESP;
            case 986:
                return COMMAND_GET_USER_REQ;
            case 985:
                return COMMAND_GET_USER_RESP;
            case 984:
                return COMMAND_GET_MESSAGE_REQ;
            case 983:
                return COMMAND_GET_MESSAGE_RESP;
            case 982:
                return COMMAND_GET_OFFLINE_MESSAGE_REQ;
            case 981:
                return COMMAND_GET_OFFLINE_MESSAGE_RESP;

            case 801:
                return COMMAND_OPEN_LOTTERY;
            case 802:
                return COMMAND_NO_LOTTERY;
            case 803:
                return COMMAND_REST;
            case 804:
                return COMMAND_OPENING_LOTTERY;
            case 805:
                return COMMAND_WAITTING;
            case 8:
                return COMMAND_INTO_ROOM_RECEIVE;
            case 9:
                return COMMAND_GO_INTO_CHAT_ROOM_NOTLOGIN;
            case 10:
                return COMMAND_EXIT_LIVE_ROOM;
            case 13:
                return COMMAND_SEND_GIFT;
            case 23:
                return COMMAND_RE_SEND_GIFT;
            case 33:
                return COMMAND_ANCHOR_GIFT;
            case 14:
                return COMMAND_CHAT_ROOM_INFO;
            case 20:
                return COMMAND_SYSTEM_MSG;
            case 501:
                return COMMAND_SYSTEM_NOTICE;
            case 22:
                return COMMAND_PRISE_RECEIVE;
            case 999:
                return COMMAND_HE_RESULT;
            case 19:
                return COMMAND_GO_INTO_CHAT_ROOM_AT_HOME;
            case 107:
                return COMMAND_ANCHOR_REDAY_CANCEL;
            case 108:
                return COMMAND_ANCHOR_ENTER_ROOM;
            case 109:
                return COMMAND_ANCHOR_REDAY_OPEN;

            case 110:
                return COMMAND_ANCHOR_OPEN_VIDEO;
            case 111:
                return COMMAND_ANCHOR_CLOSE_VIDEO;
            case 112:
                return COMMAND_ANCHOR_STATE;
            case 101:
                return COMMAND_PICK_ANCHOR;
            case 301:
                return COMMAND_USER_CONTROL;
            case 302:
                return COMMAND_USER_STOP_TALK;
            case 303:
                return COMMAND_USER_USER_OPEN_TALK;
            case 312:
                return COMMAND_ALL_STOP_TALK;
            case 313:
                return COMMAND_ALL_OPEN_TALK;
            case 304:
                return COMMAND_USER_PICK;
            case 314:
                return COMMAND_ANCHOR_MSG;
            default:
                return null;
        }
    }

    private final int value;

    private Command(int value) {
        this.value = value;
    }
}

