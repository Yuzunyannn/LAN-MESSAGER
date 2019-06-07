package client.event;

import user.UOnline;

public class EventLogin extends EventRecv {

	public final String info;
	public final String username;

	public EventLogin(String username, String info) {
		super(UOnline.getInstance().getUser(username));
		this.username = username;
		this.info = info;
	}
}
