package server.user;

import event.SubscribeEvent;
import network.Connection;
import network.IMessage;
import network.RecvDealMessage;
import network.event.EventValidationSuccess;

public class ULogin extends RecvDealMessage {

	/** 校验成功的连接分配等待登录的处理 */
	@SubscribeEvent
	public static void toWaitLogin(EventValidationSuccess event) {
		if (event.isClient())
			return;
		event.con.setRecvDeal(new ULogin());
	}

	/** 分离消息，确保是登录消息 */
	@Override
	protected void execute(IMessage msg, Connection con) {
		super.execute(msg, con);
	}
}
