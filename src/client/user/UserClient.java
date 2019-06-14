package client.user;

import network.Connection;
import network.IMessage;
import network.RecvDealMessage;
import user.User;

public class UserClient extends User {

	/** 记录到服务器的连接 */
	static public Connection toServer = null;
	/** 客户端用户 */
	static private UserClient user = null;

	public UserClient(String userName) {
		super(userName);
	}

	public static boolean sendToServer(IMessage msg) {
		return RecvDealMessage.send(UserClient.toServer, msg);
	}

	public static String getClientUsername() {
		return toServer.getName();
	}

	public static UserClient getClientUser() {
		if (user == null)
			user = new UserClient(toServer.getName());
		return user;
	}

	@Override
	public boolean sendMesage(IMessage msg) {
		return UserClient.sendToServer(msg);
	}

}
