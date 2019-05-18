package server.user;

import event.EventBusSynchronized;
import event.IEventBus;
import log.Logger;
import network.Connection;
import network.Network;
import network.RecvDealMessage;
import network.Side;
import user.UOnline;
import user.User;
import user.message.MessageLogin;

public class UOnlineServer extends UOnline {
	public static IEventBus eventHandle = new EventBusSynchronized();

	/** 登录一个用户 */
	@Override
	public void login(String username, String password, Connection con) {
		if (this.isOnline(username)) {
			Logger.log.warn("一个已经在线的用户再次发来登录报文！用户" + username);
			return;
		}
		if (!ULogin.login(username, password)) {
			RecvDealMessage.send(con, new MessageLogin(username, "error", Side.SERVER));
			return;
		}
		UserServer user = this.userOnline(username);
		boolean ok = UOnlineServer.eventHandle.post(new server.user.event.EventLoginOnline(user));
		if (ok) {
			con.setRecvDeal(new RecvDealMessage());
			RecvDealMessage.send(con, new MessageLogin(username, Network.VALIDATION, Side.SERVER));
		} else {
			this.userOffline(username);
		}
	}

	/** 添加一个用户 */
	@Override
	protected UserServer userOnline(String username) {
		UserServer user = new UserServer(username);
		users.put(user.getUserName(), user);
		return user;
	}

	/** 删除一个用户 */
	@Override
	protected User userOffline(String username) {
		return users.remove(username);
	}
}
