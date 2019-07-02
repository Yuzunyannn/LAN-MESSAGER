package client.event;

import java.util.List;

import event.Event;
import user.User;

/** 点击条目事件，用于显示右边的工具框，是聊天，是其他的等 */
public class EventShow extends Event {
	/** 描述当前的种类的标识 */
	public final String id;
	/** 工具框的种类ID */
	public final String toolId;
	public final List<User> userlist;
	public final User boss;
	public EventShow(String toolId, String id) {
		super(false, false);
		this.id = id;
		this.toolId = toolId;
		userlist=null;
		boss=null;
	}
	public EventShow(String toolId, String id,List<User> user,User boss) {
		super(false, false);
		this.id = id;
		this.toolId = toolId;
		userlist=user;
		this.boss=boss;
	}
}
