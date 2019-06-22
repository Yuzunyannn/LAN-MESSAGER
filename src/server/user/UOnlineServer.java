package server.user;

import event.EventBusSynchronized;
import event.IEventBus;
import event.SubscribeEvent;
import log.Logger;
import network.Connection;
import network.Network;
import network.RecvDealMessage;
import network.Side;
import network.event.EventConnectionEnd;
import user.UOnline;
import user.User;
import user.message.MessageLogin;

public class UOnlineServer extends UOnline {
	public final static IEventBus eventHandle = new EventBusSynchronized();

	/** 用户掉线后，发送下线消息 */
	@SubscribeEvent
	public void onDisconnection(EventConnectionEnd e) {
		UserServer user = (UserServer) this.getUser(e.con.getName());
		UOnlineServer.eventHandle.post(new server.user.event.EventLogoutOnline(user));
	}

	/** 处理logout */
	@SubscribeEvent
	public void onLogout(server.user.event.EventLogoutOnline e) {
		this.userOffline(e.user.getUserName());
	}

	/** 登录一个用户 */
	@Override
	public void login(String username, String password, Connection con) {
		if (this.isOnline(username)) {
			Logger.log.warn("一个已经在线的用户再次发来登录报文！用户" + username);
			con.wake();
			return;
		}
		if (!ULogin.login(username, password)) {
			RecvDealMessage.send(con, new MessageLogin(username, "error", Side.SERVER));
			con.wake();
			return;
		}
		UserServer user = this.userOnline(username, con);
		boolean ok = UOnlineServer.eventHandle.post(new server.user.event.EventLoginOnline(user));
		if (ok) {
			con.setName(username);
			con.setRecvDeal(new RecvDealMessage());
			RecvDealMessage.send(con, new MessageLogin(username, Network.VALIDATION, Side.SERVER));
		} else {
			this.userOffline(username);
		}
		con.wake();
	}

	/** 添加一个用户 */
	private UserServer userOnline(String username, Connection con) {
		UserServer user = new UserServer(username, con);
		users.put(user.getUserName(), user);
		return user;
	}

	/** 删除一个用户 */
	public User userOffline(String username) {
		return users.remove(username);
	}

	@Override
	public User getUser(String username) {
		if (this.isOnline(username)) {
			return users.get(username);
		} else {
			return new UserServer(username, null);
		}
	}
}
