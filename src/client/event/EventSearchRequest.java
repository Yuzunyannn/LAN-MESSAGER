package client.event;

import java.util.ArrayList;

import event.Event;
import user.User;

public class EventSearchRequest extends Event {
	public final ArrayList<User> name;
	public EventSearchRequest(ArrayList<User> name ) {
		this.name=name;
		
	}
}
