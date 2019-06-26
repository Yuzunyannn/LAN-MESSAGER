package client.event;

import event.Event;

public class EventLogState extends Event {

	/** 在线 */
	public static int ONLINE = 1;
	/** 离线 */
	public static int OFFLINE = 2;

	/** 当前状态 */
	public final int satate;

	public EventLogState(int satate) {
		this.satate = satate;
	}
}
