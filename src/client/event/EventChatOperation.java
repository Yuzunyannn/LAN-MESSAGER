package client.event;

import client.frame.info.ListScrollPanel;
import event.Event;

public class EventChatOperation extends Event {
	public static final String DELETECHAT = "删除聊天";
	public static final String FIXEDCHAT = "置顶聊天";
	public static final String CANELFIXEDCHAT = "取消置顶";
	public static final String ADDCHAT = "添加聊天";

	public final String optype;
	public final String username;
	public final String recvpanel;

	public EventChatOperation(String username, String optype) {
		this.optype = optype;
		this.username = username;
		recvpanel = ListScrollPanel.FRIENDPANEL;

	}

	public EventChatOperation(String username, String optype, String recvPanel) {
		this.optype = optype;
		this.username = username;
		this.recvpanel = recvPanel;
	}
}
