package com.wuzuqing.component_im.common.base;


import com.wuzuqing.component_im.common.packets.Command;
import com.wuzuqing.component_im.common.tcp.Protocol;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author WChao 
 *
 */
public class ImPacket {
	private static final long serialVersionUID = 2000118564569232098L;

	protected Status status;//包状态码;
	protected byte version = 0;
	protected byte mask = 0;
	protected byte[] body;//消息体;
	
	private Command command;//消息命令;
	private Integer synSeq=0;
	private static final AtomicLong ID_ATOMICLONG = new AtomicLong();
	private Long id;
	private int byteCount;
	private Long respId;
	private boolean isBlockSend;
	private Meta meta;
	private boolean isFromCluster;
	private ByteBuffer preEncodedByteBuffer;
	private boolean isSslEncrypted;

	public ImPacket(byte[] body){
		this.body = body;
	}

	public ImPacket(Command command, byte[] body)
	{
		this(body);
		this.setCommand(command);
	}

	public ImPacket(Command command)
	{
		this(command,null);
	}

	public static byte encodeEncrypt(byte bs,boolean isEncrypt){
		if(isEncrypt){
			return (byte) (bs | Protocol.FIRST_BYTE_MASK_ENCRYPT);
		}else{
			return (byte)(Protocol.FIRST_BYTE_MASK_ENCRYPT & 0b01111111);
		}
	}

	public static boolean decodeCompress(byte version)
	{
		return (Protocol.FIRST_BYTE_MASK_COMPRESS & version) != 0;
	}

	public static byte encodeCompress(byte bs, boolean isCompress)
	{
		if (isCompress)
		{
			return (byte) (bs | Protocol.FIRST_BYTE_MASK_COMPRESS);
		} else
		{
			return (byte) (bs & (Protocol.FIRST_BYTE_MASK_COMPRESS ^ 0b01111111));
		}
	}

	public static boolean decodeHasSynSeq(byte maskByte)
	{
		return (Protocol.FIRST_BYTE_MASK_HAS_SYNSEQ & maskByte) != 0;
	}

	public static byte encodeHasSynSeq(byte bs, boolean hasSynSeq)
	{
		if (hasSynSeq)
		{
			return (byte) (bs | Protocol.FIRST_BYTE_MASK_HAS_SYNSEQ);
		} else
		{
			return (byte) (bs & (Protocol.FIRST_BYTE_MASK_HAS_SYNSEQ ^ 0b01111111));
		}
	}

	public static boolean decode4ByteLength(byte version)
	{
		return (Protocol.FIRST_BYTE_MASK_4_BYTE_LENGTH & version) != 0;
	}

	public static byte encode4ByteLength(byte bs, boolean is4ByteLength)
	{
		if (is4ByteLength)
		{
			return (byte) (bs | Protocol.FIRST_BYTE_MASK_4_BYTE_LENGTH);
		} else
		{
			return (byte) (bs & (Protocol.FIRST_BYTE_MASK_4_BYTE_LENGTH ^ 0b01111111));
		}
	}

	public static byte decodeVersion(byte version)
	{
		return (byte) (Protocol.FIRST_BYTE_MASK_VERSION & version);
	}

	/**
	 * 计算消息头占用了多少字节数
	 * @return
	 *
	 * @author: wchao
	 * 2017年1月31日 下午5:32:26
	 *
	 */
	public int calcHeaderLength(boolean is4byteLength)
	{
		int ret = Protocol.LEAST_HEADER_LENGHT;
		if (is4byteLength)
		{
			ret += 2;
		}
		if (this.getSynSeq() > 0)
		{
			ret += 4;
		}
		return ret;
	}
	public Command getCommand()
	{
		return command;
	}

	public void setCommand(Command type)
	{
		this.command = type;
	}

	/**
	 * @return the body
	 */
	public byte[] getBody()
	{
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(byte[] body)
	{
		this.body = body;
	}

	/**
	 *
	 * @return
	 * @author: wchao
	 * 2017年2月22日 下午3:15:18
	 *
	 */
	public String logstr()
	{
		return this.command == null ? Command.COMMAND_UNKNOW.name() : this.command.name();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getSynSeq() {
		return this.synSeq;
	}

	public byte getMask() {
		return mask;
	}

    public byte getVersion() {
        return version;
    }

	public void setMask(byte mask) {
		this.mask = mask;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public static class Meta {
		private Boolean isSentSuccess = new Boolean(false);
		private CountDownLatch countDownLatch = null;

		public Meta() {
		}

		public Boolean getIsSentSuccess() {
			return this.isSentSuccess;
		}

		public void setIsSentSuccess(Boolean isSentSuccess) {
			this.isSentSuccess = isSentSuccess;
		}

		public CountDownLatch getCountDownLatch() {
			return this.countDownLatch;
		}

		public void setCountDownLatch(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}
	}
	public ImPacket() {
		this.id = ID_ATOMICLONG.incrementAndGet();
		this.byteCount = 0;
		this.respId = null;
		this.isBlockSend = false;
		this.meta = null;
		this.isFromCluster = false;
		this.synSeq = 0;
		this.preEncodedByteBuffer = null;
		this.isSslEncrypted = false;
	}


	public int getByteCount() {
		return this.byteCount;
	}

	public Long getId() {
		return this.id;
	}


	public ByteBuffer getPreEncodedByteBuffer() {
		return this.preEncodedByteBuffer;
	}

	public Long getRespId() {
		return this.respId;
	}

	public boolean isBlockSend() {
		return this.isBlockSend;
	}

	public void setBlockSend(boolean isBlockSend) {
		this.isBlockSend = isBlockSend;
	}

	public void setByteCount(int byteCount) {
		this.byteCount = byteCount;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public void setPreEncodedByteBuffer(ByteBuffer preEncodedByteBuffer) {
		this.preEncodedByteBuffer = preEncodedByteBuffer;
	}

	public void setRespId(Long respId) {
		this.respId = respId;
	}

	public void setSynSeq(Integer synSeq) {
		this.synSeq = synSeq;
	}

	public boolean isFromCluster() {
		return this.isFromCluster;
	}

	public void setFromCluster(boolean isFromCluster) {
		this.isFromCluster = isFromCluster;
	}

	public boolean isSslEncrypted() {
		return this.isSslEncrypted;
	}

	public void setSslEncrypted(boolean isSslEncrypted) {
		this.isSslEncrypted = isSslEncrypted;
	}

	public Meta getMeta() {
		return this.meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}
}
