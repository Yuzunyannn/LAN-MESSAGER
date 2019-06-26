package client.event;

import java.util.List;

import event.Event;
import user.User;

public class EventSearchRequest extends Event {
	public final List<User> name;

	public EventSearchRequest(List<User> name) {
		this.name = name;

	}
}
