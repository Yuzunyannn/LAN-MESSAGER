package server.user.event;

import event.Event;
import server.user.UserServer;

/** 在用户上线之后调用 */
public class EventLoginOnline extends Event {

	public final UserServer user;

	public EventLoginOnline(UserServer user) {
		super(true, true);
		this.user = user;
	}

}
