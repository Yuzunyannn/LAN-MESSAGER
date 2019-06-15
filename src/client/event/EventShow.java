package client.event;

import event.Event;
import user.User;

/** 点击条目事件，用于显示右边的工具框，是聊天，是其他的等 */
public class EventShow extends Event {
	/** 描述当前用户的记录信息 */
	public final User user;
	/** 工具框的种类ID */
	public final String toolId;

	public EventShow(String toolId, User choose) {
		super(false, false);
		this.user = choose;
		this.toolId = toolId;
	}
}
