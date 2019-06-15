package client.event;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import client.frame.utility.JPanelUtility;
import event.Event;
import user.User;

/** 当用户列表需要发生变化 */
public class EventULChange extends Event {

	public final static int ADD = 0x01;
	public final static int REMOVE = 0x02;

	public List<ChangeInfo> infos = new LinkedList<ChangeInfo>();

	public EventULChange() {
	}

	public EventULChange(ArrayList<User> users, int flags) {
		for (User u : users)
			infos.add(new ChangeInfo(u, JPanelUtility.TOOLID_CHATING, flags));
	}

	static public class ChangeInfo {
		public final User user;
		public final String toolId;
		public final int flags;

		public ChangeInfo(User u, String t, int f) {
			this.user = u;
			this.toolId = t;
			this.flags = f;
		}
	}
}
