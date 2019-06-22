package client.event;

import event.Event;

public class EventSelectDownload extends Event {
	public int pos = 0;
	
	public EventSelectDownload(int pos) {
		// TODO Auto-generated constructor stub
		super();
		this.pos = pos;
	}
}
