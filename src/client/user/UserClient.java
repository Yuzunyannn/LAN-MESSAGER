package client.user;

import network.Connection;
import user.User;

public class UserClient extends User {

	/** 记录到服务器的连接 */
	static public Connection toServer = null;;

	public UserClient(String userName) {
		super(userName);
	}

}
