package client.event;

import java.util.ArrayList;

import event.Event;
import user.User;
/**
 * 当用户列表需要发生变化*/
public class EventULChange extends Event {
	public ArrayList<User> ul;

	public EventULChange() {
	}

	public EventULChange(ArrayList<User> ul) {
		this.ul = ul;
	}
}
