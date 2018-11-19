package com.wuzuqing.component_im.common.utils;


import com.wuzuqing.component_im.common.exception.LengthOverflowException;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * 
 * @author tanyaowu 
 * 2017年10月19日 上午9:41:00
 */
public class ByteBufferUtils {
	/**
	 * 组合两个bytebuffer，把可读部分的组合成一个新的bytebuffer
	 * @param byteBuffer1
	 * @param byteBuffer2
	 * @return
	 * @author: tanyaowu
	 */
	public static ByteBuffer composite(ByteBuffer byteBuffer1, ByteBuffer byteBuffer2) {
		int capacity = byteBuffer1.limit() - byteBuffer1.position() + byteBuffer2.limit() - byteBuffer2.position();
		ByteBuffer ret = ByteBuffer.allocate(capacity);

		ret.put(byteBuffer1);
		ret.put(byteBuffer2);

		ret.position(0);
		ret.limit(ret.capacity());
		return ret;
	}
	public static ByteBuffer composite(ByteBuffer byteBuffer1, byte[] buff, int len) throws LengthOverflowException {
		int capacity = byteBuffer1.limit() - byteBuffer1.position() + len;
		if (capacity<0){
			throw new LengthOverflowException("服务器断开链接");
		}
		ByteBuffer ret = ByteBuffer.allocate(capacity);
		for(int i=byteBuffer1.position(),end=byteBuffer1.limit();i<end;i++){
			ret.put(byteBuffer1.get());
		}
		for (int i=0;i<len;i++){
			ret.put(buff[i]);
		}
		ret.position(0);
		ret.limit(ret.capacity());
		return ret;
	}
    public static ByteBuffer cut(ByteBuffer byteBuffer1) {
        int capacity = byteBuffer1.limit() - byteBuffer1.position();
        if(capacity==byteBuffer1.capacity()){
            return byteBuffer1;
        }
        ByteBuffer ret = ByteBuffer.allocate(capacity);
        for(int i=byteBuffer1.position(),end=byteBuffer1.limit();i<end;i++){
            ret.put(byteBuffer1.get());
        }
        ret.position(0);
        ret.limit(ret.capacity());
        return ret;
    }
	/**
	 * 
	 * @param src
	 * @param srcStartindex
	 * @param dest
	 * @param destStartIndex
	 * @param length
	 */
	public static void copy(ByteBuffer src, int srcStartindex, ByteBuffer dest, int destStartIndex, int length) {
		System.arraycopy(src.array(), srcStartindex, dest.array(), destStartIndex, length);
	}

	/**
	 *
	 * @param src
	 * @param startindex 从0开始
	 * @param endindex
	 * @return
	 *
	 * @author: tanyaowu
	 *
	 */
	public static ByteBuffer copy(ByteBuffer src, int startindex, int endindex) {
		int size = endindex - startindex;
		byte[] dest = new byte[size];
		System.arraycopy(src.array(), startindex, dest, 0, dest.length);
		ByteBuffer newByteBuffer = ByteBuffer.wrap(dest);
		return newByteBuffer;
	}

	/**
	 * 
	 * @param src
	 * @param unitSize 每个单元的大小
	 * @return 如果不需要拆分，则返回null
	 */
	public static ByteBuffer[] split(ByteBuffer src, int unitSize) {
		int limit = src.limit();
		if (unitSize >= limit) {
			return null;//new ByteBuffer[] { src };
		}

//		return null;

		int size =  (int)(Math.ceil((double) src.limit() / (double) unitSize));
		ByteBuffer[] ret = new ByteBuffer[size];
		int srcIndex = 0;
		for (int i = 0; i < size; i++) {
			int bufferSize = unitSize;
			if (i == size - 1) {
				bufferSize = src.limit() % unitSize;
			}
			
			byte[] dest = new byte[bufferSize];
			System.arraycopy(src.array(), srcIndex, dest, 0, dest.length);
			srcIndex = srcIndex + bufferSize;
			
			ret[i] = ByteBuffer.wrap(dest);
			ret[i].position(0);
			ret[i].limit(ret[i].capacity());
		}
		
		return ret;
	}
	
	
//	public static Packet[] split(Packet packet, int unitSize) {
//		
//	}

	public static void main(String[] args) {
		System.out.println(Math.ceil((double) 3 / (double) 2));
		System.out.println(Math.ceil((double) 6 / (double) 2));
		System.out.println(Math.ceil((double) 7 / (double) 2));
		
		
		System.out.println((int) Math.ceil((double) 4434 / (double) 3000));
		
		System.out.println(7 % 4);

	}

	/**
	 *
	 * @param buffer
	 * @return
	 * @author: tanyaowu
	 */
	public static int lineEnd(ByteBuffer buffer) throws LengthOverflowException {
		return lineEnd(buffer, Integer.MAX_VALUE);
	}

	/**
	 *
	 * @param buffer
	 * @param maxlength
	 * @return
	 * @author: tanyaowu
	 */
	public static int lineEnd(ByteBuffer buffer, int maxlength) throws LengthOverflowException {
		boolean lastIsR = false;
		//		int startPosition = buffer.position();
		int count = 0;
		while (buffer.hasRemaining()) {
			byte b = buffer.get();
			count++;
			if (count > maxlength) {
				throw new LengthOverflowException("maxlength is " + maxlength);
			}
			if (b == '\r') {
				lastIsR = true;
			} else {
				if (b == '\n') {
					int endPosition = buffer.position();
					if (lastIsR) {
						return endPosition - 2;
					} else {
						return endPosition - 1;
					}
				} else {
					lastIsR = false;
				}
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param buffer
	 * @param endChar 结束
	 * @param maxlength
	 * @return
	 * @throws LengthOverflowException
	 * @author tanyaowu
	 */
	public static int lineEnd(ByteBuffer buffer, char endChar, int maxlength) throws LengthOverflowException {
		//		int startPosition = buffer.position();
		int count = 0;
		//		int i = 0;
		while (buffer.hasRemaining()) {
			byte b = buffer.get();
			count++;
			if (count > maxlength) {
				throw new LengthOverflowException("maxlength is " + maxlength);
			}
			//			if (i == 22) {
			//				log.error("{}-{}", (char)b, b);
			//			}
			//			log.error("{}、{}-{}", i++, (char)b, b);

			if ((char) b == endChar) {
				int endPosition = buffer.position();
				return endPosition - 1;
			}
		}
		return -1;
	}

	public static byte[] readBytes(ByteBuffer buffer, int length) {
		byte[] ab = new byte[length];
		buffer.get(ab);
		return ab;
	}

	/**
	 *
	 * @param buffer
	 * @param charset
	 * @return
	 * @author: tanyaowu
	 */
	public static String readLine(ByteBuffer buffer, String charset) throws LengthOverflowException {
		return readLine(buffer, charset, Integer.MAX_VALUE);
	}

	/**
	 *
	 * @param buffer
	 * @param charset
	 * @param maxlength
	 * @return
	 * @author: tanyaowu
	 */
	public static String readLine(ByteBuffer buffer, String charset, Integer maxlength) throws LengthOverflowException {
		//		boolean canEnd = false;
		int startPosition = buffer.position();
		int endPosition = lineEnd(buffer, maxlength);

		if (endPosition > startPosition) {
			byte[] bs = new byte[endPosition - startPosition];
			System.arraycopy(buffer.array(), startPosition, bs, 0, bs.length);
			if (StringUtils.isNotBlank(charset)) {
				try {
					return new String(bs, charset);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			} else {
				return new String(bs);
			}

		} else if (endPosition == -1) {
			return null;
		} else if (endPosition == startPosition) {
			return "";
		}
		return null;
	}

	public static String readLine(ByteBuffer buffer, String charset, char endChar, Integer maxlength) throws LengthOverflowException {
		//		boolean canEnd = false;
		int startPosition = buffer.position();
		int endPosition = lineEnd(buffer, endChar, maxlength);

		if (endPosition > startPosition) {
			byte[] bs = new byte[endPosition - startPosition];
			System.arraycopy(buffer.array(), startPosition, bs, 0, bs.length);
			if (StringUtils.isNotBlank(charset)) {
				try {
					return new String(bs, charset);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			} else {
				return new String(bs);
			}

		} else if (endPosition == -1) {
			return null;
		} else if (endPosition == startPosition) {
			return "";
		}
		return null;
	}

	public static int readUB1(ByteBuffer buffer) {
		int ret = buffer.get() & 0xff;
		return ret;
	}

	public static int readUB2(ByteBuffer buffer) {
		int ret = buffer.get() & 0xff;
		ret |= (buffer.get() & 0xff) << 8;
		return ret;
	}

	public static int readUB2WithBigEdian(ByteBuffer buffer) {
		int ret = (buffer.get() & 0xff) << 8;
		ret |= buffer.get() & 0xff;
		return ret;
	}

	public static long readUB4(ByteBuffer buffer) {
		long ret = buffer.get() & 0xff;
		ret |= (long) (buffer.get() & 0xff) << 8;
		ret |= (long) (buffer.get() & 0xff) << 16;
		ret |= (long) (buffer.get() & 0xff) << 24;
		return ret;
	}

	public static long readUB4WithBigEdian(ByteBuffer buffer) {
		long ret = (long) (buffer.get() & 0xff) << 24;
		ret |= (long) (buffer.get() & 0xff) << 16;
		ret |= (long) (buffer.get() & 0xff) << 8;
		ret |= buffer.get() & 0xff;

		return ret;
	}

	public static final void writeUB2(ByteBuffer buffer, int i) {
		buffer.put((byte) (i & 0xff));
		buffer.put((byte) (i >>> 8));
	}

	public static final void writeUB2WithBigEdian(ByteBuffer buffer, int i) {
		buffer.put((byte) (i >>> 8));
		buffer.put((byte) (i & 0xff));
	}

	public static final void writeUB4(ByteBuffer buffer, long l) {
		buffer.put((byte) (l & 0xff));
		buffer.put((byte) (l >>> 8));
		buffer.put((byte) (l >>> 16));
		buffer.put((byte) (l >>> 24));
	}

	public static final void writeUB4WithBigEdian(ByteBuffer buffer, long l) {
		buffer.put((byte) (l >>> 24));
		buffer.put((byte) (l >>> 16));
		buffer.put((byte) (l >>> 8));
		buffer.put((byte) (l & 0xff));
	}
}
