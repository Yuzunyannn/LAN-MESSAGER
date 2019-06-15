package client.event;

import event.Event;

public class EventChatOperation  extends Event{
	public static final String DELETECHAT="删除聊天";
	public static final String FIXEDCHAT="置顶聊天";
	public static final String CANELFIXEDCHAT="取消置顶";
public final String type;
public final String username;
public EventChatOperation(String username,String type) {
	this.type=type;
	this.username=username;
	
}
}
