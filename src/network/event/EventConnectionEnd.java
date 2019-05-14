package network.event;

import event.Event;
import network.Connection;

/** 连接结束，该事件是connect的接受线程退出时传递的 */
public class EventConnectionEnd extends Event {
	public final Connection con;

	public EventConnectionEnd(Connection con) {
		super(false, false);
		this.con = con;
	}
}
