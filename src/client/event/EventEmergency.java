package client.event;

import event.Event;

public class EventEmergency extends Event {
	public final int level;
	public final String info;

	public EventEmergency(int lv, String info) {
		super(false, false);
		this.level = lv;
		this.info = info;
	}
	
	@Override
	public String toString() {
		return info;
	}
}
