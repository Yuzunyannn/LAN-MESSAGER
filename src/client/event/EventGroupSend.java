package client.event;

import java.util.List;

import event.Event;

public class EventGroupSend extends Event {
	private List<String> users;
	private final String msg;
	
	public EventGroupSend(List<String> users, String msg) {
		this.users = users;
		this.msg = msg;
	}

	public List<String> getUsers() {
		return users;
	}

	public String getMsg() {
		return msg;
	}
	
}
