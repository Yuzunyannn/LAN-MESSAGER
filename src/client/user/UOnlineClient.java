package client.user;

import user.UOnline;
import user.User;

public class UOnlineClient extends UOnline {

	@Override
	public boolean isOnline(String username) {
		return super.isOnline(username);
	}

	/** 用户上线 */
	@Override
	protected User userOnline(String username) {
		User user = new UserClient(username);
		users.put(user.getUserName(), user);
		return user;
	}

	/** 用户下线 */
	@Override
	protected User userOffline(String username) {
		return users.remove(username);
	}
}
