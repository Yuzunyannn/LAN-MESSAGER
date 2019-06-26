package client.event;

import java.util.List;

import event.Event;
import user.User;
import user.UserSpecial;

public class EventGroupInfoGet extends Event {

	public final List<User> users;
	public final User boss;
	public final UserSpecial sp;

	public EventGroupInfoGet(User boss, List<User> users, UserSpecial sp) {
		this.users = users;
		this.boss = boss;
		this.sp = sp;
	}

}
