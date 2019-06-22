package client.event;

import event.Event;
import user.User;

/** 当接收到别人发来的消息 */
abstract public class EventRecv extends Event {

	public final User from;

	public EventRecv(User from) {
		super(false, false);
		this.from = from;
	}

	/** 当接收到别人发来的字符串消息 */
	public static class EventRecvString extends EventRecv {

		public final String str;

		public EventRecvString(User from, String str) {
			super(from);
			this.str = str;
		}

	}

}
