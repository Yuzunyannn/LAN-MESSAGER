package client.event;

import java.util.Collection;
import event.Event;
import user.User;

public class EventSearchRequest extends Event {
	public final Collection<User> name;
	public final boolean source;
	//source=true，本地，source=false远程
	public EventSearchRequest(Collection<User> name,boolean source) {
		this.name = name;
		this.source=source;
	}
	public EventSearchRequest(Collection<User> name) {
		this.name = name;
		this.source=false;
	}
}
