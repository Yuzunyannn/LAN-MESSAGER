package network;

import java.io.IOException;
import java.nio.ByteBuffer;

import log.Logger;
import network.event.EventValidationSuccess;

public class RecvDealValidation implements IRecvDeal {

	/** 发送验证数据 */
	static void sendValidation(Connection con) {
		Logger.log.impart(con, "发送验证信息！");
		ByteBuffer buff = ByteBuffer.allocate(1024 * 100);
		buff.put((byte) 0);
		buff.put((byte) 0);
		buff.put(Network.VALIDATION.getBytes());
		con.send(buff);
	}

	/** 检测是校验成功 */
	static public boolean check(Connection con) {
		return !(con.deal instanceof RecvDealValidation);
	}

	@Override
	public void deal(Connection con) throws IOException {
		int flags = con.input.read();
		// 检测是否验证成功
		boolean check = flags == 0 && this.checkValidation(con);
		if (check) {
			// 验证成功
			Logger.log.impart(con, "验证验证成功！");
			Network.eventHandle.post(new EventValidationSuccess(con));
			if (con.deal == this)
				con.setRecvDeal(new RecvDealMessage());
			if (con.isClient())
				RecvDealValidation.sendValidation(con);
		} else {
			if (flags == -1)
				Logger.log.warn(con + "对面拒绝接受！" + "Side:" + con.side.ordinal());
			else
				Logger.log.warn(con + "验证验证失败！" + "Side:" + con.side.ordinal());
			con.close();
		}
		// 唤醒等待验证成功的线程
		synchronized (con) {
			con.notify();
		}
	}

	/** 特殊检查 */
	private boolean checkValidation(Connection con) throws IOException {
		int flags = con.input.read();
		if (flags == 0) {
			byte[] check = Network.VALIDATION.getBytes();
			byte[] b = new byte[check.length];
			con.input.read(b);
			for (int i = 0; i < b.length; i++) {
				// 如果存在任何一个不一样的
				if (b[i] != check[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
