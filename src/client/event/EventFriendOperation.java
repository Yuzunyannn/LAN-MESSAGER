package client.event;

import event.Event;

public class EventFriendOperation extends Event {
	public static final String ADDFRIEND="添加好友";
	public static final String DELETEFRIEND="删除好友";
	public final String username;
	public final String  type;
public EventFriendOperation(String username,String type) {
	this.username=username;
	this.type=type;
}
}
