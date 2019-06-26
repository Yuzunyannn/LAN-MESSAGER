package client.event;

import java.util.ArrayList;
import java.util.List;

import event.Event;

public class EventGroupCreate extends Event {
	public final List<String> groupUsers = new ArrayList<String>();
	
	public EventGroupCreate() {
		// TODO Auto-generated constructor stub
		
	}
	
}
