package client.event;

import event.Event;

//infoPanel状态变化
public class EventIPC extends Event {
	public final static int FRIENDS = 1;
	public final static int SEARCH = 2;
	public int state;
	public EventIPC(int state)
	{
		this.state=state;
	}
}
