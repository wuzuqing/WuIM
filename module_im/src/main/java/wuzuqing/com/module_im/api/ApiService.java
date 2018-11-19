package wuzuqing.com.module_im.api;


import com.wuzuqing.component_data.bean.BaseObj;
import com.wuzuqing.component_data.bean.GroupMemberBean;
import com.wuzuqing.component_im.bean.ContactsBean;
import com.wuzuqing.component_im.bean.GroupBean;
import com.wuzuqing.component_im.common.packets.ChatBody;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @Created by TOME .
 * @时间 2018/5/15 18:18
 * @描述 ${TODO}
 */

public interface ApiService {

    @GET("example/chat/getFriend/{id}")
    Observable<BaseObj<List<ContactsBean>>> getFriend(@Path("id") int size);

    @GET("example/chat/getGroupMemberList/{groupId}/{userId}")
    Observable<BaseObj<List<GroupMemberBean>>> getGroupMemberList(@Path("groupId") String groupId, @Path("userId") int userId);

    @GET("example/chat/getGroupList/{userId}")
    Observable<BaseObj<List<GroupBean>>> getGroupList(@Path("userId") int userId);

    @GET("example/chat/getOffLineMsg/{userId}/{lastTime}")
    Observable<BaseObj<List<ChatBody>>> getOffLineMsg(@Path("userId") String userId, @Path("lastTime") Long lastTime);



}
