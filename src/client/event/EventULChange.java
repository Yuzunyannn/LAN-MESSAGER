package client.event;

import java.util.ArrayList;

import event.Event;
import user.User;

public class EventULChange extends Event{
public ArrayList<User>ul;
	public EventULChange() 
	{
	}
	public EventULChange(ArrayList<User>ul) 
	{
		this.ul=ul;
	}
}
