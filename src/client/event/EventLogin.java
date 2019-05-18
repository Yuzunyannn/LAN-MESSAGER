package client.event;

import event.Event;

public class EventLogin extends Event {

	public final String username;
	public final String info;

	public EventLogin(String username,String info) {
		super(false, false);
		this.username = username;
		this.info=info;
	}
}
