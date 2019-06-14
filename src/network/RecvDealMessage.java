package network;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import log.Logger;
import util.Cast;

public class RecvDealMessage implements IRecvDeal {

	/** 处理 */
	@Override
	public void deal(Connection con) throws Exception {
		InputStream input = con.input;
		// 读一个字节
		int flags = input.read();
		// 如果-1
		if (flags == -1)
			throw new EOFException("socket已关闭!");
		// 根据定义的协议完成处理
		switch (flags) {
		case PROTOCOL1:
			this.deal1(con, input);
			break;
		default:
			Logger.log.impart(con, "接收到的协议错误！", "Side:", con.side.ordinal());
			con.close();
			break;
		}
	}

	/** 读并检查 */
	private void readAndCheck(InputStream input, byte[] bytes) throws IOException {
		int length = 0;
		int realAt = 0;
		while (realAt < bytes.length) {
			if (realAt == 0) {
				length = input.read(bytes);
			} else {
				byte[] tmp = new byte[bytes.length - realAt];
				length = input.read(tmp);
				this.check(length);
				System.arraycopy(tmp, 0, bytes, realAt, length);
			}
			realAt += length;
			this.check(length);
		}
	}

	private void check(int length) throws IOException {
		if (length == -1)
			throw new EOFException("socket已关闭!");
	}

	/**
	 * 第一种协议 |4字节：message编号|2字节：数据大小|N字节数据|
	 */
	final static public byte PROTOCOL1 = 1;

	/** 注册的Message<ID,Class Message> */
	public static Map<Integer, Class<? extends IMessage>> msgMap = Collections
			.synchronizedMap(new HashMap<Integer, Class<? extends IMessage>>());

	/**
	 * 注册一个信息
	 * 
	 * @param msgClass 确保有无参的构造函数
	 */
	public static void registerMessage(Integer msgId, Class<? extends IMessage> msgClass) {
		if (msgId == null)
			throw new RuntimeException("注册是ID不能为null");
		if (msgClass == null)
			throw new RuntimeException("注册的类不能为null");
		if (msgMap.containsKey(msgId))
			throw new RuntimeException("重复的注册：" + msgId);
		msgMap.put(msgId, msgClass);
	}

	/** 注册一个信息，确保有无参的构造函数 */
	public static void registerMessage(String msgName, Class<? extends IMessage> msgClass) {
		int SEED = 131;
		int hash = 0;
		for (int i = 0; i < msgName.length(); i++) {
			char ch = msgName.charAt(i);
			hash = hash * SEED + ch;
		}
		registerMessage(hash, msgClass);
	}

	/**
	 * 通过Message类，获取注册名
	 * 
	 * @return null为找不到
	 */
	public static Integer getRegisterName(Class<? extends IMessage> msgClass) {
		Set<Entry<Integer, Class<? extends IMessage>>> set = msgMap.entrySet();
		Iterator<Entry<Integer, Class<? extends IMessage>>> it = set.iterator();
		while (it.hasNext()) {
			Entry<Integer, Class<? extends IMessage>> entry = it.next();
			if (entry.getValue().equals(msgClass)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static boolean send(Connection con, IMessage msg) {
		if (con.isClosed())
			return false;
		// 寻找消息对应的名称
		Integer msgId = RecvDealMessage.getRegisterName(msg.getClass());
		if (msgId == null) {
			Logger.log.warn("发送的msg类型错误：" + msg.getClass());
			return false;
		}
		// 初始化buffer
		byte[] bytesId = Cast.toBytes(msgId);
		ByteBuffer buffer = ByteBuffer.allocate(1 + bytesId.length + 2 + 256 * 256);
		// 放入协议字节
		buffer.put(PROTOCOL1);
		buffer.put(bytesId);
		// 设置内容
		int at = buffer.position();
		buffer.put((byte) 0);
		buffer.put((byte) 0);
		// 设置buffer
		try {
			msg.toBytes(buffer);
		} catch (IOException e) {
			Logger.log.warn("在发送信息的时候，转化bytes时出现异常！", e);
			return false;
		}
		// 获取数据大小
		int size = buffer.position() - 2 - at;
		buffer.array()[at] = (byte) (size & 0xff);
		buffer.array()[at + 1] = (byte) ((size >> 8) & 0xff);
		// 发送
		return con.send(buffer);
	}

	/** 协议1处理 */
	private void deal1(Connection con, InputStream input) throws Exception {
		// 读取message的ID
		byte[] bytes = new byte[4];
		this.readAndCheck(input, bytes);
		int msgId = Cast.toInt(bytes);
		// 内容长度
		bytes[0] = (byte) input.read();
		bytes[1] = (byte) input.read();
		this.check(bytes[0]);
		this.check(bytes[1]);
		int size = bytes[0] | bytes[1] << 8;
		// 创建buff
		ByteBuffer buffer = ByteBuffer.allocate(size);
		// 读取
		if (size != 0) {
			this.readAndCheck(input, buffer.array());
		}
		// 寻找对应的message
		Class<? extends IMessage> msgClass = msgMap.get(msgId);
		if (msgClass == null) {
			throw new RuntimeException("未注册的消息：" + msgId);
		}
		IMessage msg;
		try {
			msg = msgClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("无法实例化的消息：" + msgClass.getName() + "，注意是否有无参的构造函数！");
		}
		// 恢复
		msg.fromBytes(buffer);
		// 执行
		this.execute(msg, con);
	}

	protected void execute(IMessage msg, Connection con) {
		msg.execute(con);
	}

}
