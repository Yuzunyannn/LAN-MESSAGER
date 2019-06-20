package server.user;

import network.Connection;
import network.IMessage;
import network.RecvDealMessage;
import user.User;

public class UserServer extends User {
	/** 记录用户的连接 */
	Connection con;

	public UserServer(String userName, Connection con) {
		super(userName);
		this.con = con;
	}

	@Override
	synchronized public boolean sendMesage(IMessage msg) {
		if (this.con == null)
			return false;
		return RecvDealMessage.send(this.con, msg);
	}

	@Override
	public boolean isOnline() {
		return this.con != null;
	}

}
