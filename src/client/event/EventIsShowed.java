package client.event;

import event.Event;
import user.User;

/**
 * 当对方点击,我方接受到的事件 用于处理已读
 * 
 * @param user 对面用户
 */
public class EventIsShowed extends Event {
	private User user;

	public EventIsShowed() {
	}

	public EventIsShowed(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
