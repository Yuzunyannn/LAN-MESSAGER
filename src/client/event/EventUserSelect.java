package client.event;

import event.Event;

public class EventUserSelect extends Event {
	private String username;
	public EventUserSelect(String username)
	{
		this.username=username;
	}
	public String getUsername()
	{
		return username;
	}
}
