package client.event;

import java.util.ArrayList;

import user.User;

public class EventSearchRequest extends EventRecv {
	public final User name;
	public EventSearchRequest(User from,User name ) {
		super(from);
		this.name=name;
		
	}

}
