package client.event;

import event.Event;
import user.User;
import user.UserSpecial;

/** 当接收到别人发来的消息 */
abstract public class EventRecv extends Event {

	public final User from;
	public final UserSpecial sp;

	public EventRecv(User from) {
		super(false, false);
		this.from = from;
		this.sp = null;
	}

	public EventRecv(User from, UserSpecial sp) {
		super(false, false);
		this.from = from;
		this.sp = sp;
	}

	/** 当接收到别人发来的字符串消息 */
	public static class EventRecvString extends EventRecv {

		public final String str;

		public EventRecvString(User from, String str) {
			super(from);
			this.str = str;
		}

		public EventRecvString(User from, UserSpecial sp, String str) {
			super(from, sp);
			this.str = str;
		}

	}

}
