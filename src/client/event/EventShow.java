package client.event;

import event.Event;

/** 点击条目事件，用于显示右边的工具框，是聊天，是其他的等 */
public class EventShow extends Event {
	/** 描述当前的种类的标识 */
	public final String id;
	/** 工具框的种类ID */
	public final String toolId;

	public EventShow(String toolId, String id) {
		super(false, false);
		this.id = id;
		this.toolId = toolId;
	}
}
