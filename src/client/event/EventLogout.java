package client.event;

import client.user.UserClient;

/** 用户登出，除了手动登出，还有断线 */
public class EventLogout extends EventRecv {

	public EventLogout() {
		super(UserClient.getClientUser());
	}

}
