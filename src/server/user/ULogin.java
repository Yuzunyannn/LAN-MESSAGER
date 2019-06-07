package server.user;

import event.SubscribeEvent;
import log.Logger;
import network.Connection;
import network.IMessage;
import network.RecvDealMessage;
import network.event.EventValidationSuccess;
import user.message.MessageLogin;

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
		if (!(msg instanceof MessageLogin)) {
			Logger.log.warn("连接" + con + "未登录，却发送了别的报文！");
			return;
		}
		super.execute(msg, con);
	}

	static public boolean login(String username, String password) {
		if(username.equals("guest")){
			return true;
		}
		return false;
	}
}
