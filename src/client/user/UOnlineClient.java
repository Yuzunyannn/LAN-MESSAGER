package client.user;

import user.UOnline;
import user.User;

public class UOnlineClient extends UOnline {

	@Override
	public boolean isOnline(String username) {
		return super.isOnline(username);
	}

	@Override
	public User getUser(String username) {
		User user = super.getUser(username);
		return user == null ? new UserClient(username) : user;
	}
}
