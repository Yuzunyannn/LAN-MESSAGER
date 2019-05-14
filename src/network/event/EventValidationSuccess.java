package network.event;

import event.Event;
import network.Connection;

/** 效验成功后调用 */
public class EventValidationSuccess extends Event {
	public final Connection con;

	public EventValidationSuccess(Connection con) {
		super(false, false);
		this.con = con;
	}

	public boolean isClient() {
		return this.con.isClient();
	}
}
