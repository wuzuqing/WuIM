package com.wuzuqing.component_im.common.packets;

/**
 * @author WChao
 * @date 2018年4月10日 下午3:18:06
 */
public class OffLineMessageReqBody extends Message {
	private static final long serialVersionUID = -10113316720258444L;
	private Long  lastTime;
	private Integer userId;

	public OffLineMessageReqBody( Integer userId,Long lastTime) {
		this.lastTime = lastTime;
		this.userId = userId;
	}

	public Long getLastTime() {
		return lastTime;
	}

	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

    @Override
    public String toString() {
        return "OffLineMessageReqBody{" +
                "lastTime=" + lastTime +
                ", userId=" + userId +
                ", cmd=" + cmd +
                '}';
    }
}
