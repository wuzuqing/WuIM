/**
 * 
 */
package com.wuzuqing.component_im.common.tcp;


import com.wuzuqing.component_im.common.base.ImPacket;
import com.wuzuqing.component_im.common.packets.Command;

/**
 * 版本: [1.0]
 * 功能说明: 
 * 作者: WChao 创建时间: 2017年8月21日 下午3:51:05
 */
public class TcpPacket extends ImPacket {
	
	private static final long serialVersionUID = -4283971967100935982L;
	
	public TcpPacket(){}
	
	public TcpPacket(Command command, byte[] body){
		super(command, body);
	}
	public TcpPacket(byte[] body){
		super(body);
	}
	public byte getVersion() {
		return version;
	}
	public void setVersion(byte version) {
		this.version = version;
	}
	public byte getMask() {
		return mask;
	}
	public void setMask(byte mask) {
		this.mask = mask;
	}
}
