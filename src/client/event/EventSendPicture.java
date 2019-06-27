package client.event;

import client.frame.utility.BubbleType;
import event.Event;

public class EventSendPicture extends Event {
	public String picName;
	public BubbleType type;
	public String toUser;
	
	public EventSendPicture(String name, BubbleType type, String toUser) {
		// TODO Auto-generated constructor stub
		super(false, false);
		this.picName = name;
		this.type = type;
		this.toUser = toUser;
	}
}
