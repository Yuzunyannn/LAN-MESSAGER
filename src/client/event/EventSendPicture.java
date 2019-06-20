package client.event;

import client.frame.utility.BubbleType;
import event.Event;

public class EventSendPicture extends Event {
	public String picName;
	public BubbleType type;
	
	public EventSendPicture(String name, BubbleType type) {
		// TODO Auto-generated constructor stub
		super(false, false);
		this.picName = name;
		this.type = type;
	}
}
