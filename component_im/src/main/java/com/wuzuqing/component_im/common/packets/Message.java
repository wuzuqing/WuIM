/**
 * 
 */
package com.wuzuqing.component_im.common.packets;


import com.wuzuqing.component_im.common.utils.JsonKit;

import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年7月26日 上午11:32:57
 */
public class Message implements Serializable {
	@Transient
	private static final long serialVersionUID = -6375331164604259933L;
//	protected Long createTime /*= new Date().getTime()*/;//消息创建时间;
	protected String id /*= UUIDSessionIdGenerator.instance.sessionId(null)*/;//消息id，全局唯一;
	@Transient
	protected Integer cmd ;//消息命令;

//	public Long getCreateTime() {
//		return createTime;
//	}
//
//	public void setCreateTime(Long createTime) {
//		this.createTime = createTime;
//	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCmd() {
		return cmd;
	}

	public void setCmd(Integer cmd) {
		this.cmd = cmd;
	}

	public String toJsonString() {
		return JsonKit.toJSONString(this);
	}
	
	public byte[] toByte(){
		return JsonKit.toJsonBytes(this);
	}
}
