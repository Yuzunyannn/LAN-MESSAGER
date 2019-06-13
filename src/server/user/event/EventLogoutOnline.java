package server.user.event;

import event.Event;
import server.user.UserServer;

public class EventLogoutOnline extends Event {
	public final UserServer user;

	public EventLogoutOnline(UserServer user) {
		super(false, true);
		this.user = user;
	}
}
