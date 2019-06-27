package client.user;

import client.event.EventsBridge;
import event.SubscribeEvent;
import network.event.EventConnectionEnd;
import user.UOnline;
import user.User;

public class UOnlineClient extends UOnline {

	@Override
	public boolean isOnline(String username) {
		return super.isOnline(username);
	}

	@Override
	public User getUser(String username) {
		User user = super.getUser(username);
		return user == null ? new UserClient(username) : user;
	}

	@SubscribeEvent
	public static void onDisconnection(EventConnectionEnd e) {
		EventsBridge.frontendEventHandle.post(new client.event.EventLogout());
	}
}
