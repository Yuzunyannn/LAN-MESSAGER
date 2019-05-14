package server.user;

import network.Connection;
import user.User;

public class UserServer extends User {
	/** 记录用户的连接 */
	Connection con;

	public UserServer(String userName) {
		super(userName);
	}

}
