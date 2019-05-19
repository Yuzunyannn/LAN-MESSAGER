package client.event;

import event.Event;
import user.User;

public class EventRecv extends Event {

	public final User from;

	public EventRecv(User from) {
		super(false, false);
		this.from = from;
	}

	public static class EventRecvString extends EventRecv {

		public final String str;

		public EventRecvString(User from, String str) {
			super(from);
			this.str = str;
		}

	}

}
