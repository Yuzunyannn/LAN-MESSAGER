package client.event;

import event.Event;
import user.User;

public class EventSearch extends Event {
	public final String search;
public  EventSearch(String name) {
	super(false,false);
	this.search=name;
}
}
